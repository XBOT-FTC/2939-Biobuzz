package org.firstinspires.ftc.teamcode.procedures.octo;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.OctoQuadConfig;
import com.pedropathing.revhub.localizers.OctoQuadLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.List;

class OctoQuadOffsets extends TuningOpMode<List<Double>> {
    String name;
    OctoQuadTuner.PodType podType;
    double customPodScalar;
    boolean forwardPodReversed, strafePodReversed;
    double headingScalar;
    Pose previous;
    int xPodPort, yPodPort;

    public OctoQuadOffsets(String name, OctoQuadTuner.PodType podType, double customPodScalar, Boolean forwardPodReversed, Boolean strafePodReversed, double headingScalar,
                           int xPodPort, int yPodPort) {
        super("OctoQuadOffsets Identification",
                "Automatically identifies the offsets for your OctoQuad localizer. \n"
                        + "Spin your robot in place 180 degrees counterclockwise and then stop the Opmode",
                true);
        this.name = name;
        this.podType = podType;
        this.customPodScalar = customPodScalar;
        this.forwardPodReversed = forwardPodReversed;
        this.strafePodReversed = strafePodReversed;
        this.headingScalar = headingScalar;
        this.xPodPort = xPodPort;
        this.yPodPort = yPodPort;
    }

    @Override
    protected List<Double> runTuningOpMode() {
        OctoQuadConfig config = new OctoQuadConfig(c -> {
            c.name.set(name);
            c.xPodPort.set(xPodPort);
            c.yPodPort.set(yPodPort);
            c.xPodDirection.set(forwardPodReversed ? OctoQuad.EncoderDirection.REVERSE : OctoQuad.EncoderDirection.FORWARD);
            c.yPodDirection.set(strafePodReversed ? OctoQuad.EncoderDirection.REVERSE : OctoQuad.EncoderDirection.FORWARD);
            if (podType.equals(OctoQuadTuner.PodType.CUSTOM)) {
                c.encoderResolutionUnit.set(DistanceUnit.INCH);
                c.ticksPerUnit.set(customPodScalar);
            } else {
                c.encoderResolutionUnit.set(DistanceUnit.INCH);
                c.ticksPerUnit.set(podType == OctoQuadTuner.PodType.SWING_ARM ? OctoQuadTuner.SWING_ARM : OctoQuadTuner.FOUR_BAR);
            }
            c.xPodOffset.set(0.0);
            c.yPodOffset.set(0.0);
            c.headingScalar.set(headingScalar);
        });
        OctoQuadLocalizer localizer = new OctoQuadLocalizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        localizer.update();

        waitForStart();

        while (!isStopRequested()) {
            previous = localizer.pose();
            localizer.update();
            telemetry.addData("heading", localizer.pose().heading());
            telemetry.update();
        }

        if (localizer.pose().x() != Pose.zero().x() || localizer.pose().y() != Pose.zero().y()) {
            previous =  localizer.pose();
        }

        return List.of(((-previous.y()) / 2.0), ((-previous.x()) / 2.0));
    }
}
