package org.firstinspires.ftc.teamcode.procedures.tests;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Pose;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.pedropathing.utils.Angle;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.function.Function;

class TestsOdometry extends TuningOpMode<Boolean> {
    Function<HardwareMap, Drivetrain> drivetrainFunction;
    Function<HardwareMap, Localizer> localizerFunction;

    public enum Test {
        FORWARD,
        LEFT,
        TURN,
        IDLE
    }

    private Test test = Test.FORWARD;
    public static double POWER = 0.5;

    double totalHeading;
    double prevHeading;

    private final ElapsedTime timer = new ElapsedTime();

    private boolean passedX = false;
    private boolean passedY = false;
    private boolean passedHeading = false;

    public TestsOdometry(Function<HardwareMap, Drivetrain> drivetrainFunction, Function<HardwareMap, Localizer> localizerFunction) {
        super("Localization Test", "Verifies localization and manual control.", true);
        this.drivetrainFunction = drivetrainFunction;
        this.localizerFunction = localizerFunction;
    }

    @Override
    public Boolean runTuningOpMode() throws InterruptedException {
        Localizer localizer = localizerFunction.apply(hardwareMap);
        Drivetrain drivetrain = drivetrainFunction.apply(hardwareMap);

        localizer.setPose(Pose.zero());

        Thread.sleep(1000);
        waitForStart();
        localizer.setPose(Pose.zero());

        timer.reset();

        while (opModeIsActive()) {
            localizer.update();

            if (localizer.pose().x() != Pose.zero().x() || localizer.pose().y() != Pose.zero().y() || localizer.pose().heading() != Pose.zero().heading()) {
                double currentHeading = localizer.pose().heading();
                totalHeading += Angle.normalizeSigned(currentHeading - prevHeading);
                prevHeading = currentHeading;
            }

            switch (test) {
                case FORWARD:
                    drivetrain.drive(new DrivePowers(POWER, 0, 0), false);

                    if (timer.seconds() > 1) {
                        test = Test.LEFT;
                        timer.reset();
                    }
                    break;
                case LEFT:
                    drivetrain.drive(new DrivePowers(0, POWER, 0), false);

                    if (timer.seconds() > 1) {
                        test = Test.TURN;
                        timer.reset();

                        prevHeading = localizer.pose().heading();
                        totalHeading = 0;
                    }
                    break;
                case TURN:
                    drivetrain.drive(new DrivePowers(0, 0, POWER), false);

                    if (timer.seconds() > 1) {
                        test = Test.IDLE;
                        timer.reset();
                    }
                    break;
                case IDLE:
                    drivetrain.drive(new DrivePowers(0, 0, 0), true);

                    telemetry.addData("Test", "Completed");

                    Pose pose = localizer.pose();

                    if (pose.x() < 0)
                        telemetry.addData("xPod Direction", "Flipped");
                    else if (pose.x() < 2)
                        telemetry.addData("xPod Resolution", "Too high");
                    else if (pose.x() > 144)
                        telemetry.addData("xPod Resolution", "Too low");
                    else {
                        telemetry.addData("xPod", "Good");
                        passedX = true;
                    }

                    if (pose.y() < 0)
                        telemetry.addData("yPod Direction", "Flipped");
                    else if (pose.y() < 2)
                        telemetry.addData("yPod Resolution", "Too high");
                    else if (pose.y() > 144)
                        telemetry.addData("yPod Resolution", "Too low");
                    else {
                        telemetry.addData("yPod", "Good");
                        passedY = true;
                    }

                    if (totalHeading < 0)
                        telemetry.addData("Heading Direction", "Flipped");
                    else if (totalHeading < 0.02)
                        telemetry.addData("Heading Resolution", "Too high");
                    else if (totalHeading > 2 * Math.PI)
                        telemetry.addData("Heading Resolution", "Too low");
                    else {
                        telemetry.addData("Heading", "Good");
                        passedHeading = true;
                    }

                    break;
            }

            telemetry.addData("Pose", localizer.pose());
            telemetry.update();
        }
        return passedX && passedY && passedHeading;
    }
}


