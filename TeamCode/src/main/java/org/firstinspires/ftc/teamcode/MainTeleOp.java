package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Robot;

import java.util.List;

@TeleOp(name = "Main TeleOp", group = "TeleOp")
public class MainTeleOp extends OpMode {
    private Follower follower;
    private Intake intake;
    private List<LynxModule> hubs;

    @Override
    public void init() {
        // One bulk read per loop instead of one hub call per encoder/velocity read.
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        follower = Robot.createFollower(hardwareMap);
        follower.update();

        intake = new Intake(hardwareMap, telemetry);

        // Scheduler is static, so clear commands left over from the last OpMode.
        Scheduler.reset();
        Scheduler.schedule(intake.periodic());
    }

    @Override
    public void loop() {
        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }

        // Robot centric
        follower.manual(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                +gamepad1.right_stick_x
        );
        follower.update();

        // Hold to run. Any press/release re-picks from the buttons still held.
        // Non-short-circuit | so every edge flag gets read and cleared each loop.
        boolean intakeButtonsChanged =
                gamepad1.rightBumperWasPressed() | gamepad1.rightBumperWasReleased()
                | gamepad1.leftBumperWasPressed() | gamepad1.leftBumperWasReleased();
        if (intakeButtonsChanged) {
            if (gamepad1.left_bumper) intake.reverse().schedule();
            else if (gamepad1.right_bumper) intake.on().schedule();
            else intake.off().schedule();
        }
        if (gamepad1.left_trigger > 0.5) intake.slowDown(); else intake.speedUp();

        Scheduler.execute();

        telemetry.addData("Pose", follower.pose());
        telemetry.update();
    }
}
