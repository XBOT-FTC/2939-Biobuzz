package org.firstinspires.ftc.teamcode.procedures.octo;

import com.pedropathing.tuning.autotune.Inputs;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;

import java.util.List;

public class OctoQuadTuner extends Procedure {
    enum PodType {
        SWING_ARM,
        FOUR_BAR,
        CUSTOM
    }

    public static double SWING_ARM = 336.877962768;
    public static double FOUR_BAR = 505.316944406;

    public OctoQuadTuner() {
        super("OctoQuad Tuner", "A procedure for tuning the OctoQuad localizer.");
    }

    @Override
    public void run() throws InterruptedException {
        Inputs inputs = inputs("Setup", "Set OctoQuad HardwareMap Name and Odometry Pod Type");
        Inputs.Field<String> octoquadName = inputs.s("HardwareMap Name").withDefault("octoquad");
        Inputs.Field<Integer> xPort =  inputs.i("Forward Pod Port").withDefault(0);
        Inputs.Field<Integer> yPort =  inputs.i("Strafe Pod Port").withDefault(1);
        Inputs.Field<PodType> podType = inputs.e("Odometry Pod Type", PodType.class).withDefault(PodType.FOUR_BAR);
        Inputs.Field<OctoQuad.I2cRecoveryMode> recoveryMode = inputs.e("Recovery Mode", OctoQuad.I2cRecoveryMode.class).withDefault(OctoQuad.I2cRecoveryMode.MODE_1_PERIPH_RST_ON_FRAME_ERR);
        awaitInputs(inputs);

        double customPodScalar = 0;

        Inputs inputsHeadingScalar = inputs("Custom Scalar Identification Turns", "Set the number of times you will turn your robot.");
        Inputs.Field<Integer> turns = inputsHeadingScalar.i("Turns").withDefault(10);
        awaitInputs(inputsHeadingScalar);
        double headingScalar = runOpMode(new OctoQuadHeadingScalar(octoquadName.get(), turns.get(), xPort.get(), yPort.get()));

        if (podType.get() == PodType.CUSTOM) {
            Inputs inputsCustom = inputs("Custom Scalar Identification Push Distance", "Set the distance you will push your robot forward in inches");
            Inputs.Field<Double> distance = inputsCustom.d("Distance").withDefault(48.0);
            awaitInputs(inputsCustom);
            customPodScalar = runOpMode(new OctoQuadCustomPodScalar(distance.get(), octoquadName.get(), xPort.get(), yPort.get()));
        }

        boolean forwardPodReversed = runOpMode(new OctoQuadForwardDirection(octoquadName.get(), podType.get(), customPodScalar, headingScalar, xPort.get(), yPort.get()));
        boolean strafePodReversed = runOpMode(new OctoQuadStrafeDirection(octoquadName.get(), podType.get(), customPodScalar, headingScalar, xPort.get(), yPort.get()));

        List<Double> offsets = runOpMode(new OctoQuadOffsets(octoquadName.get(), podType.get(), customPodScalar, forwardPodReversed, strafePodReversed, headingScalar, xPort.get(), yPort.get()));

        result("name", octoquadName.get());
        result("headingScalar", headingScalar);

        if (podType.get() == PodType.CUSTOM) {
            result("podType", "Custom");
            result("ticksPerUnit", customPodScalar);
        } else {
            result("podType", podType.get() == PodType.SWING_ARM ? SWING_ARM : FOUR_BAR);
        }

        result("xPodDirection", forwardPodReversed ? OctoQuad.EncoderDirection.REVERSE : OctoQuad.EncoderDirection.FORWARD);
        result("yPodDirection", strafePodReversed ? OctoQuad.EncoderDirection.REVERSE : OctoQuad.EncoderDirection.FORWARD);
        result("xPodOffset", offsets.get(0));
        result("yPodOffset", offsets.get(1));

        code(Language.JAVA,"public static OctoQuadConfig localizerConfig = new OctoQuadConfig(c -> {\n" +
                "    c.name.set(\"" + octoquadName.get() + "\");\n" +
                "    c.xPodPort.set(" + xPort.get() + ");\n" +
                "    c.yPodPort.set(" + yPort.get() + ");\n" +
                (podType.get() == PodType.CUSTOM ? "    c.ticksPerUnit.set(" + customPodScalar + ");\n" : "    c.ticksPerUnit.set(" + (podType.get() == PodType.SWING_ARM ? SWING_ARM : FOUR_BAR) + ");\n") +
                "    c.xPodOffset.set(" + offsets.get(0) + ");\n" +
                "    c.yPodOffset.set(" + offsets.get(1) + ");\n" +
                "    c.xPodDirection.set(" + (forwardPodReversed ? "OctoQuad.EncoderDirection.REVERSE" : "OctoQuad.EncoderDirection.FORWARD") + ");\n" +
                "    c.yPodDirection.set(" + (strafePodReversed ? "OctoQuad.EncoderDirection.REVERSE" : "OctoQuad.EncoderDirection.FORWARD") + ");\n" +
                "    c.globalDistanceUnit.set(DistanceUnit.INCH);\n" +
                "    c.offsetUnits.set(DistanceUnit.INCH);\n" +
                "    c.i2cRecoveryMode.set(OctoQuad.I2cRecoveryMode." + recoveryMode.get() + ");\n" +
                "    c.headingScalar.set(" + headingScalar + ");\n" +
                "});");
    }
}

