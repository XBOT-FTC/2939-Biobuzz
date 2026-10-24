package org.firstinspires.ftc.teamcode;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "2026TeleOp_2939", group="Op Mode")
public class MecanumTeleOp2939 extends OpMode {
    private TeamDriveTrain drive;
    private Follower follower;

    private Gamepad driverGamepad;
    private Gamepad opGamepad;

    public void init() {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void loop() {

        driverGamepad = gamepad1;
        opGamepad = gamepad2;

        DrivePowers powers = ManualDrive.fieldCentric(
                -driverGamepad.left_stick_y,
                driverGamepad.left_stick_x,
                driverGamepad.right_stick_x,
                follower.pose().heading()
        );

        ManualDrive.driveOrHold(follower, powers);
        follower.update();
//        Pose robotPose = follower.pose(); Add this when we have a pinpoint

//        if (gamepad1.rightTriggerWasPressed()) {
//            // Command here
//        }

    }
}