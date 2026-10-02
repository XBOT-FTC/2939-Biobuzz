package org.firstinspires.ftc.teamcode.procedures.threeWheelTuner;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.pedropathing.tuning.autotune.TuningOpMode;

import java.util.List;

class ThreeWheelResolution extends TuningOpMode<List<Double>> {

    String pod;
    double distance;

    ThreeWheelResolution(String pod, double distance) {
        super(pod + " Encoder Resolution and Direction",
                "After Start, push the robot " + (pod.equals("Strafe") ? "left " : "forward ") +
                        distance + " inches exactly without turning. Stop moving, press Stop to save this measurement.", true);
        this.pod = pod;
        this.distance = distance;
    }

    @Override
    protected List<Double> runTuningOpMode() {
        ThreeWheelConfig config = ThreeWheelTuner.config(!pod.equals("Right"), 1.0, 1.0,
                Encoder.FORWARD, Encoder.FORWARD, Encoder.FORWARD);
        ThreeWheelLocalizer localizer = ThreeWheelTuner.localizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        Pose position = null;

        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
            position = localizer.pose();
        }

        if (position == null) {
            return null;
        }
        double movement = pod.equals("Strafe") ? position.y() : position.x();
        if (movement == 0.0) {
            return null;
        }
        return List.of(Math.abs(movement / distance), movement < 0 ? Encoder.REVERSE : Encoder.FORWARD);
    }
}


