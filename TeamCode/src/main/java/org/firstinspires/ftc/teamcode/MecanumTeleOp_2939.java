package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "2026TeleOp_2939", group="Linear OpMode")
public class MecanumTeleOp_2939 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrain drive = new DriveTrain(hardwareMap);
        waitForStart();


        if (isStopRequested()) return;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        boolean wasOpYPressed = gamepad2.y;
        boolean wasOpXPressed = gamepad2.x;
        boolean wasOpAPressed = gamepad2.a;
        boolean wasOpBPressed = gamepad2.b;

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        previousGamepad2.copy(currentGamepad2);
        currentGamepad2.copy(gamepad2);

        //Shooting Code
        if (wasOpXPressed) {
        // Action
        } else if (wasOpAPressed) {
            // Action
        } else if (wasOpBPressed) {
            // Action
        } else if (wasOpYPressed) {
            // Action
        } else {
            // Action
        }
    }
}
