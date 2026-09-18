package org.firstinspires.ftc.teamcode.procedures.twoWheelTuner;

import com.pedropathing.revhub.localizers.RevHubIMU;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.pedropathing.tuning.autotune.Inputs;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import java.util.List;

public class TwoWheelTuner extends Procedure {

    public TwoWheelTuner() {
        super("Two Wheel Tuner", "A procedure for tuning the Two Wheel localizer.");
    }

    @Override
    public void run() throws InterruptedException {
        Inputs setup = inputs("Setup", "Set encoder, IMU, and Control Hub orientation");
        Inputs.Field<String> forwardPodName = setup.s("Forward Encoder Motor Name").withDefault("lf");
        Inputs.Field<String> strafePodName = setup.s("Strafe Encoder Motor Name").withDefault("rr");
        Inputs.Field<String> imuName = setup.s("IMU HardwareMap Name").withDefault("imu");
        Inputs.Field<RevHubOrientationOnRobot.LogoFacingDirection> logoDirection =
                setup.e("Logo Facing Direction", RevHubOrientationOnRobot.LogoFacingDirection.class)
                        .withDefault(RevHubOrientationOnRobot.LogoFacingDirection.UP);
        Inputs.Field<RevHubOrientationOnRobot.UsbFacingDirection> usbDirection =
                setup.e("USB Facing Direction", RevHubOrientationOnRobot.UsbFacingDirection.class)
                        .withDefault(RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD);
        awaitInputs(setup);

        Inputs resolution = inputs("Encoder Resolution Identification", "Set the exact distance you will push the robot in inches");
        Inputs.Field<Double> distance = resolution.d("Distance").withDefault(48.0);
        awaitInputs(resolution);

        TwoWheelSetup values = new TwoWheelSetup(
                forwardPodName.get(),
                strafePodName.get(),
                imuName.get(),
                logoDirection.get(),
                usbDirection.get()
        );

        Double forwardTicksPerInchResult = runOpMode(new TwoWheelForwardResolution(values, distance.get()));
        Double strafeTicksPerInchResult = runOpMode(new TwoWheelStrafeResolution(values, distance.get()));

        if (forwardTicksPerInchResult == null || strafeTicksPerInchResult == null
                || forwardTicksPerInchResult == 0.0 || strafeTicksPerInchResult == 0.0) {
            abort("Encoder resolution measurement was zero. Complete both pushes before pressing Stop.");
            return;
        }

        double forwardTicksPerInch = forwardTicksPerInchResult;
        double strafeTicksPerInch = strafeTicksPerInchResult;

        double forwardTicksToInches = 1.0 / forwardTicksPerInch;
        double strafeTicksToInches = 1.0 / strafeTicksPerInch;

        boolean forwardPodReversed = runOpMode(
                new TwoWheelForwardDirection(values, forwardTicksToInches, strafeTicksToInches)
        );

        boolean strafePodReversed = runOpMode(
                new TwoWheelStrafeDirection(values, forwardTicksToInches, strafeTicksToInches)
        );

        List<Double> offsets = runOpMode(
                new TwoWheelOffsets(
                        values,
                        forwardTicksToInches,
                        strafeTicksToInches,
                        forwardPodReversed,
                        strafePodReversed
                )
        );

        result("xPodName", values.forwardPodName);
        result("yPodName", values.strafePodName);
        result("imuName", values.imuName);
        result("logoDirection", values.logoDirection);
        result("usbDirection", values.usbDirection);
        result("forwardTicksPerInch", forwardTicksPerInch);
        result("strafeTicksPerInch", strafeTicksPerInch);
        result("forwardTicksToInches", forwardTicksToInches);
        result("strafeTicksToInches", strafeTicksToInches);
        result("xPodDirection", forwardPodReversed ? "REVERSED" : "FORWARD");
        result("yPodDirection", strafePodReversed ? "REVERSED" : "FORWARD");
        result("xPodOffset", offsets.get(0));
        result("yPodOffset", offsets.get(1));

        code(Language.JAVA,
                "public static TwoWheelConfig localizerConfig = new TwoWheelConfig(c -> {\n" +
                        "    c.xPodName.set(\"" + values.forwardPodName + "\");\n" +
                        "    c.yPodName.set(\"" + values.strafePodName + "\");\n" +
                        "    c.imuName.set(\"" + values.imuName + "\");\n" +
                        "    c.xPodOffset.set(" + offsets.get(0) + ");\n" +
                        "    c.yPodOffset.set(" + offsets.get(1) + ");\n" +
                        "    c.forwardTicksToInches.set(" + forwardTicksToInches + ");\n" +
                        "    c.strafeTicksToInches.set(" + strafeTicksToInches + ");\n" +
                        "    c.xPodDirection.set(" +
                        (forwardPodReversed ? "Encoder.REVERSE" : "Encoder.FORWARD") +
                        ");\n" +
                        "    c.yPodDirection.set(" +
                        (strafePodReversed ? "Encoder.REVERSE" : "Encoder.FORWARD") +
                        ");\n" +
                        "    c.imu.set(new RevHubIMU(new RevHubOrientationOnRobot(\n" +
                        "            RevHubOrientationOnRobot.LogoFacingDirection." + values.logoDirection.name() + ",\n" +
                        "            RevHubOrientationOnRobot.UsbFacingDirection." + values.usbDirection.name() + "\n" +
                        "    )));\n" +
                        "});"
        );
    }

    static TwoWheelConfig config(
            TwoWheelSetup values,
            double forwardTicksToInches,
            double strafeTicksToInches,
            double xPodDirection,
            double yPodDirection,
            double xPodOffset,
            double yPodOffset
    ) {
        return new TwoWheelConfig(c -> {
            c.xPodName.set(values.forwardPodName);
            c.yPodName.set(values.strafePodName);
            c.imuName.set(values.imuName);
            c.xPodOffset.set(xPodOffset);
            c.yPodOffset.set(yPodOffset);
            c.forwardTicksToInches.set(forwardTicksToInches);
            c.strafeTicksToInches.set(strafeTicksToInches);
            c.xPodDirection.set(xPodDirection);
            c.yPodDirection.set(yPodDirection);
            c.imu.set(new RevHubIMU(new RevHubOrientationOnRobot(values.logoDirection, values.usbDirection)));
        });
    }
}

