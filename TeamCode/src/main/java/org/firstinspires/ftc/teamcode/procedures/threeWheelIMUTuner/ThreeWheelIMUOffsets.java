package org.firstinspires.ftc.teamcode.procedures.threeWheelIMUTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.ThreeWheelIMUConfig;
import com.pedropathing.revhub.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

import java.util.List;

class ThreeWheelIMUOffsets extends TuningOpMode<List<Double>> {

    boolean left;
    double forward;
    double strafe;
    double leftDirection;
    double rightDirection;
    double strafeDirection;

    ThreeWheelIMUOffsets(boolean left, double forward, double strafe,
                         double leftDirection, double rightDirection, double strafeDirection) {
        super((left ? "Left" : "Right") + " Pod Offset Identification",
                "After Start, rotate exactly 180 degrees counterclockwise about the robot center. " +
                        "Keep that center fixed. Stop moving, press Stop to save this measurement.", true);
        this.left = left;
        this.forward = forward;
        this.strafe = strafe;
        this.leftDirection = leftDirection;
        this.rightDirection = rightDirection;
        this.strafeDirection = strafeDirection;
    }

    @Override
    protected List<Double> runTuningOpMode() {
        ThreeWheelIMUConfig config = ThreeWheelIMUTuner.config(left, forward, strafe,
                leftDirection, rightDirection, strafeDirection);
        boolean previousUseIMU = ThreeWheelIMULocalizer.useIMU;
        ThreeWheelIMULocalizer.useIMU = false;
        try {
            ThreeWheelIMULocalizer localizer = ThreeWheelIMUTuner.localizer(hardwareMap, config);
            localizer.setPose(new Pose(0, 0));
            localizer.update();
            Pose position = null;

            waitForStart();
            while (!isStopRequested()) {
                localizer.update();
                position = localizer.pose();
            }

            if (position == null) {
                return null;
            }
            return List.of(-position.x() / Math.PI, position.y() / Math.PI);
        } finally {
            ThreeWheelIMULocalizer.useIMU = previousUseIMU;
        }
    }
}


