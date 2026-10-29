package org.firstinspires.ftc.teamcode.procedures.threeWheelTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

import java.util.List;

class ThreeWheelOffsets extends TuningOpMode<List<Double>> {

    boolean left;
    double forward;
    double strafe;
    double leftDirection;
    double rightDirection;
    double strafeDirection;

    ThreeWheelOffsets(boolean left, double forward, double strafe,
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
        ThreeWheelConfig config = ThreeWheelTuner.config(left, forward, strafe,
                leftDirection, rightDirection, strafeDirection);
        ThreeWheelLocalizer localizer = ThreeWheelTuner.localizer(hardwareMap, config);
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
    }
}


