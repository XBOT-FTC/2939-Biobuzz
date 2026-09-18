package org.firstinspires.ftc.teamcode.procedures.threeWheelTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

class ThreeWheelTurn extends TuningOpMode<Double> {

    ThreeWheelConfig config;

    ThreeWheelTurn(ThreeWheelConfig config) {
        super("Turn Multiplier Identification",
                "After Start, rotate exactly 360 degrees counterclockwise. " +
                        "Stop moving, press Stop to save this measurement.", true);
        this.config = config;
    }

    @Override
    protected Double runTuningOpMode() {
        ThreeWheelLocalizer localizer = ThreeWheelTuner.localizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        localizer.update();
        double startHeading = localizer.getTotalHeading();
        Double heading = null;

        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
            heading = localizer.getTotalHeading();
        }

        if (heading == null || heading <= startHeading) {
            return null;
        }
        return config.turnTicksToRadians.get() * (2.0 * Math.PI) / (heading - startHeading);
    }
}


