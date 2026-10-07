package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name = "2026TeleOp_2939", group="OpMode")
public class MecanumTeleOp_2939 extends OpMode {
    private Drivetrain drivetrain;
    private Localizer localizer;
    private Foresight foresight;

    private Follower follower;

    private final Gamepad driverGamepad = new Gamepad();
    private final Gamepad opGamepad = new Gamepad();

    public void init() {
        follower = Constants.createFollower(hardwareMap,drivetrain,localizer,foresight);

    }

    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -driverGamepad.left_stick_y,
                driverGamepad.left_stick_x,
                driverGamepad.right_stick_x,
                follower.pose().heading()
        );

        ManualDrive.driveOrHold(follower, powers);
        follower.update();
        Pose robotPose = follower.pose();

        if (opGamepad.rightTriggerWasPressed()) {
            // example code
        }

    }
}