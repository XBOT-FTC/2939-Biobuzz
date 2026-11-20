package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class ShooterSubsystem {
    DcMotorEx pollenShooter;
    Telemetry telemetry;
    PIDFCoefficients shooterPid = new PIDFCoefficients(0.005, 0, 0, 1);

    int pollenShooterMaintainVelocity = 500;
    int pollenShooterShootVelocity = 1000;

    public ShooterSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        pollenShooter = hardwareMap.get(DcMotorEx.class, Constants.pollenShooterMotor());
        this.telemetry = telemetry;
        pollenShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, shooterPid);
    }

    public void shoot() {
        pollenShooter.setVelocity(pollenShooterShootVelocity);
        telemetry.addData("Shooter speed: ",  pollenShooterShootVelocity);
    }

    public void maintain() {
        pollenShooter.setVelocity(pollenShooterMaintainVelocity);
        telemetry.addData("Shooter speed: ",  pollenShooterMaintainVelocity);
    }
}
