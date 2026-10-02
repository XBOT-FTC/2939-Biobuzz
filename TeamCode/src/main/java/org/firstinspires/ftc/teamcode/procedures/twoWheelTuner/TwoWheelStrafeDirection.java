package org.firstinspires.ftc.teamcode.procedures.twoWheelTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.pedropathing.revhub.localizers.TwoWheelLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

class TwoWheelStrafeDirection extends TuningOpMode<Boolean> {

    TwoWheelSetup values;
    double forwardTicksToInches;
    double strafeTicksToInches;

    TwoWheelStrafeDirection(TwoWheelSetup values, double forwardTicksToInches, double strafeTicksToInches) {
        super(
                "Strafe Direction Identification",
                "Determines if your strafe pod needs to be reversed.\n"
                        + "Push your robot left and then stop the Opmode",
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

        return localizer.pose().y() < 0;
    }
}

