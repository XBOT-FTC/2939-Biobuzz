package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DriveTrain {
    private final DcMotor leftFront;
    private final DcMotor leftRear;
    private final DcMotor rightFront;
    private final DcMotor rightRear;

    public double leftFrontPower;
    public double leftRearPower;
    public double rightFrontPower;
    public double rightRearPower;

    public double getLeftFrontPower() {
        return leftFrontPower;
    }
    public double getLeftRearPower() {
        return leftRearPower;
    }
    public double getRightFrontPower() {
        return rightFrontPower;
    }
    public double getRightRearPower() {
        return rightRearPower;
    }

    public DriveTrain(HardwareMap hwMap) {
        this.leftFront = hwMap.dcMotor.get(Constants.leftFront());
        this.leftRear = hwMap.dcMotor.get(Constants.leftRear());
        this.rightFront = hwMap.dcMotor.get(Constants.rightFront());
        this.rightRear = hwMap.dcMotor.get(Constants.rightRear());
    }

    public void gamepadConfigurator(Gamepad gamepad) {
        // Implementation for gamepad configuration
        double y = gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double rx = -gamepad.right_stick_x;


        double inputNormalization = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        this.leftFrontPower = (y + x + rx) / inputNormalization;
        this.leftRearPower = (y - x + rx) / inputNormalization;
        this.rightFrontPower = (y - x - rx) / inputNormalization;
        this.rightRearPower = (y + x - rx) / inputNormalization;
    }
}
