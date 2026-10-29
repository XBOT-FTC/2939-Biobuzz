package org.firstinspires.ftc.teamcode.procedures.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.Function;

class TestsHold extends TuningOpMode<Boolean> {
    Function<HardwareMap, Follower> followerFunction;

    public TestsHold(Function<HardwareMap, Follower> followerFunction) {
        super("Hold Test", "Tests the Follower's ability to hold a position.", true);
        this.followerFunction = followerFunction;
    }

    @Override
    public Boolean runTuningOpMode() throws InterruptedException {
        Follower follower = followerFunction.apply(hardwareMap);
        follower.setPose(Pose.zero());
        Thread.sleep(1000);
        waitForStart();
        follower.setPose(Pose.zero());
        follower.update();
        follower.hold(Pose.zero());
        while (opModeIsActive()) {
            follower.update();
        }
        return true;
    }
}


