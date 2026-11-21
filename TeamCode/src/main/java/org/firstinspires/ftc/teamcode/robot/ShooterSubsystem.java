package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class ShooterSubsystem {
    DcMotorEx pollenShooter;
    Telemetry telemetry;
    PIDFCoefficients shooterPid = new PIDFCoefficients(2, 0, 0, 1);

    double pollenShooterMaintainVelocity = 0.5;
    double pollenShooterShootVelocity = 1;

    public ShooterSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        pollenShooter = hardwareMap.get(DcMotorEx.class, Constants.pollenShooterMotor());
        this.telemetry = telemetry;
        pollenShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, shooterPid);
        pollenShooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void shoot() {
        pollenShooter.setPower(1);
        telemetry.addData("Shooter speed power: ",  -1);
    }

    public void stop() {
        pollenShooter.setPower(0);
        telemetry.addData("Shooter speed power: ",  0);
    }

    private void maintain() {
        pollenShooter.setVelocity(pollenShooterMaintainVelocity);
        telemetry.addData("Shooter speed: ",  pollenShooterMaintainVelocity);
    }
}
