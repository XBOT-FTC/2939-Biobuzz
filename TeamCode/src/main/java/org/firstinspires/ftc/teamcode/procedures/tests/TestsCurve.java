package org.firstinspires.ftc.teamcode.procedures.tests;

import static com.pedropathing.api.Paths.curve;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.Function;

class TestsCurve extends TuningOpMode<Boolean> {
    Function<HardwareMap, Follower> followerFunction;
    double distance;

    public TestsCurve(Function<HardwareMap, Follower> followerFunction, double distance) {
        super("Curve Test", "Tests the Follower's ability to follow a curve.", true);
        this.followerFunction = followerFunction;
        this.distance = distance;
    }

    @Override
    public Boolean runTuningOpMode() throws InterruptedException {
        Follower follower = followerFunction.apply(hardwareMap);
        follower.setPose(Pose.zero());

        double distance = 48;
        boolean forward = true;

        Path path1 = curve(Pose.zero(), new Pose(distance + 0,0), new Pose(distance,distance)).tangent();
        Path path2 = curve(new Pose(distance,distance), new Pose(distance,0), Pose.zero()).tangent();

        Thread.sleep(1000);
        waitForStart();
        follower.setPose(Pose.zero());
        follower.update();
        follower.follow(path1);

        while (opModeIsActive()) {
            follower.update();
            if (follower.atParametricEnd()) {
                if (forward) {
                    follower.follow(path2);
                } else {
                    follower.follow(path1);
                }
                forward = !forward;
            }
        }
        return true;
    }
}


