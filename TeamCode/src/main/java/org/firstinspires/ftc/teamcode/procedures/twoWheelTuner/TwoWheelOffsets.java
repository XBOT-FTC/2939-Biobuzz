package org.firstinspires.ftc.teamcode.procedures.twoWheelTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.pedropathing.revhub.localizers.TwoWheelLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

import java.util.List;

class TwoWheelOffsets extends TuningOpMode<List<Double>> {

    TwoWheelSetup values;
    double forwardTicksToInches;
    double strafeTicksToInches;
    boolean forwardPodReversed;
    boolean strafePodReversed;
    Pose previous = Pose.zero();

    TwoWheelOffsets(
            TwoWheelSetup values,
            double forwardTicksToInches,
            double strafeTicksToInches,
            boolean forwardPodReversed,
            boolean strafePodReversed
    ) {
        super(
                "Two Wheel Offset Identification",
                "Automatically identifies the offsets for your Two Wheel localizer.\n"
                        + "Spin your robot in place 180 degrees counterclockwise and then stop the Opmode",
                true
        );
        this.values = values;
        this.forwardTicksToInches = forwardTicksToInches;
        this.strafeTicksToInches = strafeTicksToInches;
        this.forwardPodReversed = forwardPodReversed;
        this.strafePodReversed = strafePodReversed;
    }

    @Override
    protected List<Double> runTuningOpMode() {
        TwoWheelConfig config = TwoWheelTuner.config(
                values,
                forwardTicksToInches,
                strafeTicksToInches,
                forwardPodReversed ? Encoder.REVERSE : Encoder.FORWARD,
                strafePodReversed ? Encoder.REVERSE : Encoder.FORWARD,
                0.0,
                0.0
        );

        TwoWheelLocalizer localizer = new TwoWheelLocalizer(hardwareMap, config);
        localizer.setPose(Pose.zero());
        localizer.update();

        waitForStart();

        localizer.setPose(Pose.zero());

        while (!isStopRequested()) {
            previous = localizer.pose();
            localizer.update();

            telemetry.addData("heading", localizer.pose().heading());
            telemetry.addData("pose", localizer.pose());
            telemetry.addData("previous", previous);
            telemetry.update();
        }

        if (localizer.pose().x() != Pose.zero().x() || localizer.pose().y() != Pose.zero().y()) {
            previous = localizer.pose();
        }

        return List.of(((-previous.y()) / 2.0), ((-previous.x()) / 2.0));
    }
}
