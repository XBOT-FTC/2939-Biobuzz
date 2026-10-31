package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Mecanum TeleOp Bootleg", group = "Op Mode")
public class MecanumTeleOpBootleg extends OpMode {
    private TeamDriveTrain drive;

    @Override
    public void init() {
        drive = new TeamDriveTrain(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double leftFront = y + x + rx;
        double leftRear = y - x + rx;
        double rightFront = y - x - rx;
        double rightRear = y + x - rx;

        drive.setPowers(leftFront, leftRear, rightFront, rightRear);

        telemetry.addData("Powers", "LF %.2f LR %.2f RF %.2f RR %.2f",
                drive.leftFrontPower,
                drive.leftRearPower,
                drive.rightFrontPower,
                drive.rightRearPower);
        telemetry.update();
    }
}
