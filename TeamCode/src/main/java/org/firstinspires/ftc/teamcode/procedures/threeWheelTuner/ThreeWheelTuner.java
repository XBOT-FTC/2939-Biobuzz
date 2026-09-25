package org.firstinspires.ftc.teamcode.procedures.threeWheelTuner;

import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.pedropathing.tuning.autotune.Inputs;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class ThreeWheelTuner extends Procedure {

    private static String leftEncoderName = "lf";
    private static String rightEncoderName = "rr";
    private static String strafeEncoderName = "lr";

    public ThreeWheelTuner() {
        super("Three Wheel Tuner", "Tune three odometry pods");
    }

    @Override
    public void run() throws InterruptedException {
        Inputs setup = inputs("Encoder Setup",
                "Set the motor ports that the three odometry encoders are plugged into.");
        Inputs.Field<String> leftEncoder = setup.s("Left Encoder Motor Name").withDefault("lf");
        Inputs.Field<String> rightEncoder = setup.s("Right Encoder Motor Name").withDefault("rr");
        Inputs.Field<String> strafeEncoder = setup.s("Strafe Encoder Motor Name").withDefault("lr");
        awaitInputs(setup);
        leftEncoderName = leftEncoder.get();
        rightEncoderName = rightEncoder.get();
        strafeEncoderName = strafeEncoder.get();

        Inputs resolution = inputs("Encoder Resolution Identification",
                "Set a positive push distance in inches. Keep the robot straight during each push.");
        Inputs.Field<Double> distance = resolution.d("Distance").withDefault(48.0);
        awaitInputs(resolution);
        if (!(distance.get() > 0.0)) {
            abort("Enter a positive distance in inches.");
            return;
        }

        List<Double> left = measure("Left", distance.get());
        if (left == null) {
            return;
        }
        List<Double> right = measure("Right", distance.get());
        if (right == null) {
            return;
        }
        List<Double> strafe = measure("Strafe", distance.get());
        if (strafe == null) {
            return;
        }

        double leftTicksPerInch = left.get(0);
        double rightTicksPerInch = right.get(0);
        double strafeTicksPerInch = strafe.get(0);
        double forwardTicksPerInch = 2.0 / (1.0 / leftTicksPerInch + 1.0 / rightTicksPerInch);

        double forward = 1.0 / forwardTicksPerInch;
        double lateral = 1.0 / strafeTicksPerInch;

        List<Double> leftOffsets = runOpMode(new ThreeWheelOffsets(
                true, forward, lateral, left.get(1), right.get(1), strafe.get(1)));
        if (leftOffsets == null) {
            abort("Left stage ended without parallel pod travel. Rotate 180 degrees CCW, then press Stop.");
            return;
        }
        List<Double> rightOffsets = runOpMode(new ThreeWheelOffsets(
                false, forward, lateral, left.get(1), right.get(1), strafe.get(1)));

        if (rightOffsets == null) {
            abort("Right stage ended without parallel pod travel. Rotate 180 degrees CCW, then press Stop.");
            return;
        }
        ThreeWheelConfig config = config(true, forward, lateral,
                left.get(1), right.get(1), strafe.get(1));
        config.leftPodY.set(leftOffsets.get(0));
        config.rightPodY.set(rightOffsets.get(0));
        config.turnTicksToRadians.set(forward);

        Double turn = runOpMode(new ThreeWheelTurn(config));
        if (turn == null) {
            abort("Turn stage ended without positive rotation. Rotate 360 degrees CCW, then press Stop.");
            return;
        }
        double strafeX = (leftOffsets.get(1) + rightOffsets.get(1)) / 2.0 * turn / lateral;

        result("leftEncoderName", leftEncoderName);
        result("rightEncoderName", rightEncoderName);
        result("strafeEncoderName", strafeEncoderName);
        result("leftPodY", leftOffsets.get(0));
        result("rightPodY", rightOffsets.get(0));
        result("strafePodX", strafeX);
        result("leftTicksPerInch", leftTicksPerInch);
        result("rightTicksPerInch", rightTicksPerInch);
        result("forwardTicksPerInch", forwardTicksPerInch);
        result("strafeTicksPerInch", strafeTicksPerInch);
        result("forwardTicksToInches", forward);
        result("strafeTicksToInches", lateral);
        result("turnTicksToRadians", turn);
        result("leftEncoderDirection", direction(left.get(1)));
        result("rightEncoderDirection", direction(right.get(1)));
        result("strafeEncoderDirection", direction(strafe.get(1)));

        code(Language.JAVA,
                "public static ThreeWheelConfig localizerConfig = new ThreeWheelConfig(c -> {\n" +
                        "    c.leftEncoderName.set(\"" + leftEncoderName + "\");\n" +
                        "    c.rightEncoderName.set(\"" + rightEncoderName + "\");\n" +
                        "    c.strafeEncoderName.set(\"" + strafeEncoderName + "\");\n" +
                        "    c.leftPodY.set(" + leftOffsets.get(0) + ");\n" +
                        "    c.rightPodY.set(" + rightOffsets.get(0) + ");\n" +
                        "    c.strafePodX.set(" + strafeX + ");\n" +
                        "    c.forwardTicksToInches.set(" + forward + ");\n" +
                        "    c.strafeTicksToInches.set(" + lateral + ");\n" +
                        "    c.turnTicksToRadians.set(" + turn + ");\n" +
                        "    c.leftEncoderDirection.set(" + direction(left.get(1)) + ");\n" +
                        "    c.rightEncoderDirection.set(" + direction(right.get(1)) + ");\n" +
                        "    c.strafeEncoderDirection.set(" + direction(strafe.get(1)) + ");\n" +
                        "});");
    }

    private List<Double> measure(String pod, double distance) throws InterruptedException {
        List<Double> measured = runOpMode(new ThreeWheelResolution(pod, distance));
        if (measured == null) {
            abort(pod + " stage ended without a nonzero measurement. Check the displayed ticks, complete the push, then press Stop.");
            return null;
        }
        return measured;
    }

    static String direction(double direction) {
        return direction == Encoder.REVERSE ? "Encoder.REVERSE" : "Encoder.FORWARD";
    }

    static ThreeWheelConfig config(boolean left, double forward, double strafe,
                                   double leftDirection, double rightDirection, double strafeDirection) {
        return new ThreeWheelConfig(c -> {
            c.leftEncoderName.set(leftEncoderName);
            c.rightEncoderName.set(rightEncoderName);
            c.strafeEncoderName.set(strafeEncoderName);
            c.leftPodY.set(left ? 0.0 : 1.0);
            c.rightPodY.set(left ? -1.0 : 0.0);
            c.strafePodX.set(0.0);
            c.forwardTicksToInches.set(forward);
            c.strafeTicksToInches.set(strafe);
            c.turnTicksToRadians.set(0.0);
            c.leftEncoderDirection.set(leftDirection);
            c.rightEncoderDirection.set(rightDirection);
            c.strafeEncoderDirection.set(strafeDirection);
        });
    }

    static ThreeWheelLocalizer localizer(HardwareMap map, ThreeWheelConfig config) {
        for (LynxModule hub : map.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        for (String name : new String[]{"lf", "lr", "rf", "rr"}) {
            DcMotorEx motor = map.get(DcMotorEx.class, name);
            motor.setPower(0);
            motor.setDirection(name.equals("lf") || name.equals("lr")
                    ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        return new ThreeWheelLocalizer(map, config);
    }
}


