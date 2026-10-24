package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TeamDriveTrain {

    private final DcMotor leftFront;
    private final DcMotor leftRear;
    private final DcMotor rightFront;
    private final DcMotor rightRear;

    public double leftFrontPower;
    public double leftRearPower;
    public double rightFrontPower;
    public double rightRearPower;

    public TeamDriveTrain(HardwareMap hwMap) {
        leftFront = hwMap.get(DcMotor.class, Constants.leftFront());
        leftRear = hwMap.get(DcMotor.class, Constants.leftRear());
        rightFront = hwMap.get(DcMotor.class, Constants.rightFront());
        rightRear = hwMap.get(DcMotor.class, Constants.rightRear());

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);

        setAllZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        stop();
    }

    public void setPowers(
            double lf, double lr, double rf, double rr) {

        double max = Math.max(1.0, Math.max(
                Math.max(Math.abs(lf), Math.abs(lr)),
                Math.max(Math.abs(rf), Math.abs(rr))
        ));

        leftFrontPower = lf / max;
        leftRearPower = lr / max;
        rightFrontPower = rf / max;
        rightRearPower = rr / max;

        leftFront.setPower(leftFrontPower);
        leftRear.setPower(leftRearPower);
        rightFront.setPower(rightFrontPower);
        rightRear.setPower(rightRearPower);
    }

    public void setAllZeroPowerBehavior(
            DcMotor.ZeroPowerBehavior behavior) {
        leftFront.setZeroPowerBehavior(behavior);
        leftRear.setZeroPowerBehavior(behavior);
        rightFront.setZeroPowerBehavior(behavior);
        rightRear.setZeroPowerBehavior(behavior);
    }

    public void stop() {
        setPowers(0, 0, 0, 0);
    }
}