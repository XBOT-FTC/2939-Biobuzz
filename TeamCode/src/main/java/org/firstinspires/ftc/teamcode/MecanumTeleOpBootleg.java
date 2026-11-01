package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Mecanum TeleOp Bootleg", group = "Op Mode")
public class MecanumTeleOpBootleg extends OpMode {
    private TeamDriveTrain drive;
    private Intake intake;

    @Override
    public void init() {
        drive = new TeamDriveTrain(hardwareMap);
        intake = new Intake(hardwareMap, telemetry);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;

        double leftFront = y + rx;
        double leftRear = y + rx;
        double rightFront = y - rx;
        double rightRear = y - rx;

        drive.setPowers(leftFront, leftRear, rightFront, rightRear);

        intake.update(gamepad1.x, gamepad1.b);

        telemetry.addData("Powers", "LF %.2f LR %.2f RF %.2f RR %.2f",
                drive.leftFrontPower,
                drive.leftRearPower,
                drive.rightFrontPower,
                drive.rightRearPower);
        telemetry.update();
    }
}
