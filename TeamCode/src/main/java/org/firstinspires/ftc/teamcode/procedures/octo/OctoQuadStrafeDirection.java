package org.firstinspires.ftc.teamcode.procedures.octo;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.OctoQuadConfig;
import com.pedropathing.revhub.localizers.OctoQuadLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;

class OctoQuadStrafeDirection extends TuningOpMode<Boolean> {
    String name;
    OctoQuadTuner.PodType podType;
    double customPodScalar;
    double headingScalar;
    int xPodPort, yPodPort;

    public OctoQuadStrafeDirection(String name, OctoQuadTuner.PodType podType, double customPodScalar, double headingScalar,
                                   int xPodPort, int yPodPort) {
        super("Strafe Direction Identification",
                "Determines if your strafe pod needs to be reversed. \n"
                        + "Push your robot to the left and then stop the Opmode",
                true);
        this.name = name;
        this.podType = podType;
        this.customPodScalar = customPodScalar;
        this.headingScalar = headingScalar;
        this.xPodPort = xPodPort;
        this.yPodPort = yPodPort;
    }

    @Override
    protected Boolean runTuningOpMode() {
        OctoQuadConfig config = new OctoQuadConfig(c -> {
            c.name.set(name);
            c.xPodPort.set(xPodPort);
            c.yPodPort.set(yPodPort);
            c.xPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
            c.yPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
            c.xPodOffset.set(0.0);
            c.yPodOffset.set(0.0);
            c.headingScalar.set(headingScalar);

            if (podType == OctoQuadTuner.PodType.CUSTOM) {
                c.ticksPerUnit.set(customPodScalar);
            } else {
                c.ticksPerUnit.set(podType == OctoQuadTuner.PodType.SWING_ARM ? OctoQuadTuner.SWING_ARM : OctoQuadTuner.FOUR_BAR);
            }
        });
        OctoQuadLocalizer localizer = new OctoQuadLocalizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
        }

        return localizer.pose().y() < 0;
    }
}

