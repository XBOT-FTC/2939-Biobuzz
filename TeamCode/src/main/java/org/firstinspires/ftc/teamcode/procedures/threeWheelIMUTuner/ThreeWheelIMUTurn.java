package org.firstinspires.ftc.teamcode.procedures.threeWheelIMUTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.ThreeWheelIMUConfig;
import com.pedropathing.revhub.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

class ThreeWheelIMUTurn extends TuningOpMode<Double> {

    ThreeWheelIMUConfig config;

    ThreeWheelIMUTurn(ThreeWheelIMUConfig config) {
        super("Turn Multiplier Identification",
                "After Start, rotate exactly 360 degrees counterclockwise. " +
                        "Stop moving, press Stop to save this measurement.", true);
        this.config = config;
    }

    @Override
    protected Double runTuningOpMode() {
        boolean previousUseIMU = ThreeWheelIMULocalizer.useIMU;
        ThreeWheelIMULocalizer.useIMU = false;
        try {
            ThreeWheelIMULocalizer localizer = ThreeWheelIMUTuner.localizer(hardwareMap, config);
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
        } finally {
            ThreeWheelIMULocalizer.useIMU = previousUseIMU;
        }
    }
}


