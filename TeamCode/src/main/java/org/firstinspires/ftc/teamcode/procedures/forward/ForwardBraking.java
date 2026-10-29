package org.firstinspires.ftc.teamcode.procedures.forward;

import static com.pedropathing.utils.Utils.quadraticFit;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.pedropathing.utils.Angle;
import com.pedropathing.utils.Utils;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ForwardBraking extends TuningOpMode<List<Double>> {
    Function<HardwareMap, Localizer> localizerFunction;
    Function<HardwareMap, Drivetrain> drivetrainFunction;
    private final double headingLinear;
    private final double headingQuadratic;
    private final double headingKP;

    private double[] POWERS;
    public double MAX_BRAKE_TIME = 7.0;
    public int trials = 5;
    public double maxPower = 0.7;
    public double minPower = 0.3;
    public double bias = 1.5;
    public double brakingPower = 0.001;
    public double distance;
    public double IDLE_SECONDS = 1;

    private final ElapsedTime timer = new ElapsedTime();
    private final List<double[]> velocityToBrakingDistance = new ArrayList<>();
    private State state = State.DRIVE;
    private int iteration = 0;
    private int direction;
    private double power;
    private Vector2D startPosition;
    private double measuredVelocity;

    public ForwardBraking(Function<HardwareMap, Localizer> localizerFunction, Function<HardwareMap, Drivetrain> drivetrainFunction,
                          double headingLinear, double headingQuadratic, double headingKP, double distance) {
        super("Forward Braking", "A tuner for finding the Forward Braking Coefficients by driving forward and backward at various speeds. Please ensure that you have plenty of room at least " + distance + " inches ahead of the robot, but also tile space behind and laterally around the robot.", false);
        this.localizerFunction = localizerFunction;
        this.drivetrainFunction = drivetrainFunction;
        this.headingLinear = headingLinear;
        this.headingQuadratic = headingQuadratic;
        this.headingKP = headingKP;
        this.distance = distance;
    }

    @Override
    protected List<Double> runTuningOpMode() throws InterruptedException {
        Localizer localizer = localizerFunction.apply(hardwareMap);
        Drivetrain drivetrain = drivetrainFunction.apply(hardwareMap);

        localizer.setPose(Pose.zero());
        localizer.update();

        POWERS = biasedGradient(trials, maxPower, minPower, bias);

        List<Double> coefficients = Collections.emptyList();

        Thread.sleep(1000);
        waitForStart();
        localizer.setPose(Pose.zero());
        localizer.update();
        timer.reset();

        drivetrain.drive(new DrivePowers(maxPower,0,0), false);

        while (state != State.DONE && !isStopRequested()) {
            localizer.update();
            direction = (iteration % 2 == 0) ? 1 : -1;
            if (iteration < POWERS.length) {
                power = POWERS[iteration];
            }

            switch (state) {
                case DRIVE: {
                    if ((direction > 0 && Math.abs(localizer.pose().x()) >= distance) || (direction < 0 && Math.abs(localizer.pose().x()) <= 12)) {
                        startPosition = localizer.pose().toVector2D();
                        measuredVelocity = localizer.velocity().toVector2D().magnitude();

                        brake(drivetrain, localizer);
                        state = State.BRAKE;
                        timer.reset();
                        break;
                    }
                    drive(drivetrain, localizer);
                    break;
                }
                case BRAKE: {
                    if (localizer.velocity().toVector2D().magnitude() > 0.25 && timer.seconds() < MAX_BRAKE_TIME) {
                        brake(drivetrain, localizer);
                        break;
                    }

                    collectTrialData(localizer, drivetrain);
                    break;
                }
                case WAIT: {
                    drivetrain.stop();
                    if (timer.seconds() > IDLE_SECONDS) state = State.DRIVE;
                    break;
                }
                case DONE: {}
            }
        }

        if (state == State.DONE) {
            double[] c = quadraticFit(velocityToBrakingDistance);
            coefficients = List.of(c[0], c[1]);
        }

        return coefficients;
    }

    private double getHeadingPower(Localizer localizer) {
        double angularVel = localizer.velocity().omega;
        double brakeDist = headingLinear * angularVel +
                headingQuadratic * angularVel * angularVel * Math.signum(angularVel);
        double headingError = Angle.normalizeSigned(-localizer.pose().heading());
        double error = headingError - brakeDist;
        return Utils.clamp(headingKP * error, -0.3, 1.0) / 2;
    }


    private void drive(Drivetrain drivetrain, Localizer localizer) {
        drivetrain.drive(new DrivePowers(power * direction, 0.0, getHeadingPower(localizer)), false);
    }

    private void brake(Drivetrain drivetrain, Localizer localizer) {
        double headingPower = getHeadingPower(localizer);
        double brake = -brakingPower * direction;
        double minBrake = Math.abs(headingPower) + 0.001;

        if (direction > 0) {
            brake = Math.min(brake, -minBrake);
        } else {
            brake = Math.max(brake, minBrake);
        }

        drivetrain.drive(new DrivePowers(brake, 0, headingPower), false);
    }

    private void collectTrialData(Localizer localizer, Drivetrain drivetrain) {
        Vector2D endPosition = localizer.pose().toVector2D();
        double brakingDistance = endPosition.minus(startPosition).magnitude();

        velocityToBrakingDistance.add(new double[]{measuredVelocity, brakingDistance});

        iteration++;

        if (iteration >= POWERS.length) {
            drivetrain.stop();
            state = State.DONE;
        } else {
            state = State.WAIT;
            timer.reset();
        }
    }

    private enum State {
        DRIVE,
        BRAKE,
        WAIT,
        DONE
    }

    private static double[] biasedGradient(int count, double max, double min, double bias) {
        if (count < 2) return new double[]{max};
        double[] values = new double[count];
        for (int i = 0; i < count; i++) {
            double t = (double) i / (count - 1);
            double curved = 1 - Math.pow(t, bias);
            values[i] = min + curved * (max - min);
        }
        return values;
    }
}

