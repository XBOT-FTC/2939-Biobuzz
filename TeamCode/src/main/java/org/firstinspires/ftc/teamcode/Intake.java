package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private final DcMotor intakeMotor;

    public Intake(HardwareMap hwMap) {
        this.intakeMotor = hwMap.dcMotor.get("intakeMotor");
        this.intakeMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    public void startIntake(Gamepad gamepad) {
        if (gamepad.right_trigger > 0.1) {
            intakeMotor.setPower(gamepad.right_trigger);
        } else if (gamepad.left_trigger > 0.1) {
            intakeMotor.setPower(-gamepad.left_trigger);
        } else {
            intakeMotor.setPower(0);
        }
    }

    //Below are autonomous command features regarding intake.

    public void runIntake() {
        intakeMotor.setPower(1.0); // Run intake at full power
    }

    public void reverseIntake() {
        intakeMotor.setPower(-1.0); // Reverse intake at full power
    }

    public void stopIntake() {
        intakeMotor.setPower(0); // Stop the intake
    }

    public double getIntakePower() {
        return intakeMotor.getPower();
    }
}
