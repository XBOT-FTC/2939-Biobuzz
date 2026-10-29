package org.firstinspires.ftc.teamcode.procedures.twoWheelTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.pedropathing.revhub.localizers.TwoWheelLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

class TwoWheelForwardDirection extends TuningOpMode<Boolean> {

    TwoWheelSetup values;
    double forwardTicksToInches;
    double strafeTicksToInches;

    TwoWheelForwardDirection(TwoWheelSetup values, double forwardTicksToInches, double strafeTicksToInches) {
        super(
                "Forward Direction Identification",
                "Determines if your forward pod needs to be reversed.\n"
                        + "Push your robot forward and then stop the Opmode",
                true
        );
        this.values = values;
        this.forwardTicksToInches = forwardTicksToInches;
        this.strafeTicksToInches = strafeTicksToInches;
    }

    @Override
    protected Boolean runTuningOpMode() {
        TwoWheelConfig config = TwoWheelTuner.config(
                values,
                forwardTicksToInches,
                strafeTicksToInches,
                Encoder.FORWARD,
                Encoder.FORWARD,
                0.0,
                0.0
        );

        TwoWheelLocalizer localizer = new TwoWheelLocalizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));

        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
        }

        return localizer.pose().x() < 0;
    }
}

