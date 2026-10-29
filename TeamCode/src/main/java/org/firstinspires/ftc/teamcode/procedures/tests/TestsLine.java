package org.firstinspires.ftc.teamcode.procedures.tests;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.Function;

class TestsLine extends TuningOpMode<Boolean> {
    Function<HardwareMap, Follower> followerFunction;
    double distance;

    public TestsLine(Function<HardwareMap, Follower> followerFunction, double distance) {
        super("Line Test", "Tests the Follower's ability to follow a line.", true);
        this.followerFunction = followerFunction;
        this.distance = distance;
    }

    @Override
    public Boolean runTuningOpMode() throws InterruptedException {
        Follower follower = followerFunction.apply(hardwareMap);
        follower.setPose(Pose.zero());

        double distance = 48;
        boolean forward = true;

        Path path1 = line(Pose.zero(), new Pose(distance,0, 0)).constant(0);
        Path path2 = line(new Pose(distance,0, 0), Pose.zero()).constant(0);

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


