package org.firstinspires.ftc.teamcode.procedures.octo;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.OctoQuadConfig;
import com.pedropathing.revhub.localizers.OctoQuadLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.pedropathing.utils.Angle;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;

class OctoQuadHeadingScalar extends TuningOpMode<Double> {
    String name;
    int turns;
    double totalHeading = 0;
    double prevHeading = 0;
    int xPodPort;
    int yPodPort;

    public OctoQuadHeadingScalar(String name, int turns, int xPodPort, int yPodPort) {
        super("Heading Scalar Identification",
                "Determines the scalar for the custom pods of the OctoQuad localizer. \n"
                        + "Turn your robot " + turns * 360 + " degrees exactly ("+ turns + " times) exactly and then stop the OpMode.",
                true);
        this.name = name;
        this.turns = turns;
        this.xPodPort = xPodPort;
        this.yPodPort = yPodPort;
    }

    @Override
    protected Double runTuningOpMode() throws InterruptedException {
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
        localizer.update();
        waitForStart();
        while (!isStopRequested()) {
            localizer.update();

            if (localizer.pose().x() != Pose.zero().x() || localizer.pose().y() != Pose.zero().y() || localizer.pose().heading() != Pose.zero().heading()) {
                double currentHeading = localizer.pose().heading();
                totalHeading += Angle.normalizeSigned(currentHeading - prevHeading);
                prevHeading = currentHeading;
            }
        }
        return Math.abs((turns * Math.PI * 2 / totalHeading));
    }
}

