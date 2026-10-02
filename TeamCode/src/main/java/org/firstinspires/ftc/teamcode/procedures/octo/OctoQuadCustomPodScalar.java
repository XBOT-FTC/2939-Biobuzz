package org.firstinspires.ftc.teamcode.procedures.octo;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.OctoQuadConfig;
import com.pedropathing.revhub.localizers.OctoQuadLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;

class OctoQuadCustomPodScalar extends TuningOpMode<Double> {
    String name;
    double distance;
    int xPodPort, yPodPort;

    public OctoQuadCustomPodScalar(Double distance, String name, int xPodPort, int yPodPort) {
        super("Custom Scalar Identification",
                "Determines the scalar for the custom pods of the OctoQuad localizer. \n"
                        + "Push your robot forward " + distance + " inches exactly and then stop the Opmode",
                true);
        this.name = name;
        this.distance = distance;
        this.xPodPort = xPodPort;
        this.yPodPort = yPodPort;
    }

    @Override
    protected Double runTuningOpMode() {
        OctoQuadConfig config = new OctoQuadConfig(c -> {
            c.name.set(name);
            c.xPodPort.set(xPodPort);
            c.yPodPort.set(yPodPort);
            c.ticksPerUnit.set(1.0);
            c.xPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
            c.yPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
            c.xPodOffset.set(0.0);
            c.yPodOffset.set(0.0);
        });
        OctoQuadLocalizer localizer = new OctoQuadLocalizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        waitForStart();

        double startTicks = localizer.octoQuad.readAllEncoderData().positions[xPodPort];
        double lastLastTicksPerInch = 0;
        double lastTicksPerInch = 0;

        while (!isStopRequested()) {
            localizer.update();

            double pos = localizer.octoQuad.readAllEncoderData().positions[xPodPort];
            lastLastTicksPerInch = lastTicksPerInch;
            lastTicksPerInch = Math.abs(pos - startTicks) / distance;
        }

        return lastLastTicksPerInch;
    }
}

