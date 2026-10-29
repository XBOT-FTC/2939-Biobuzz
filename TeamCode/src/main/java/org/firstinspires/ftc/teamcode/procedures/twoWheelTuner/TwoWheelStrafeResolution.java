package org.firstinspires.ftc.teamcode.procedures.twoWheelTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.TwoWheelConfig;
import com.pedropathing.revhub.localizers.TwoWheelLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

class TwoWheelStrafeResolution extends TuningOpMode<Double> {

    TwoWheelSetup values;
    double distance;

    TwoWheelStrafeResolution(TwoWheelSetup values, double distance) {
        super(
                "Strafe Encoder Resolution Identification",
                "Push your robot left " + distance + " inches exactly and then stop the Opmode",
                true
        );
        this.values = values;
        this.distance = distance;
    }

    @Override
    protected Double runTuningOpMode() {
        TwoWheelConfig config = TwoWheelTuner.config(
                values,
                1.0,
                1.0,
                Encoder.FORWARD,
                Encoder.FORWARD,
                0.0,
                0.0
        );

        TwoWheelLocalizer localizer = new TwoWheelLocalizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        Pose position = null;

        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
            position = localizer.pose();
        }

        if (position == null || position.y() == 0.0) {
            return null;
        }
        return Math.abs(position.y() / distance);
    }
}

