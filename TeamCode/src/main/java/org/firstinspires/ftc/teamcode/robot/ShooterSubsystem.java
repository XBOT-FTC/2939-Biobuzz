package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class ShooterSubsystem {
    DcMotorEx nectarShooter;
    Telemetry telemetry;
    PIDFCoefficients pollenPid = new PIDFCoefficients(0.005, 0, 0, 1);

    int nectarShooterMaintainVelocity = 500;
    int nectarShooterShootVelocity = 1000;

    public ShooterSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        hardwareMap.get(DcMotorEx.class, Constants.pollenShooterMotor());
        this.telemetry = telemetry;
        nectarShooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pollenPid);
    }

    public void shoot() {
        nectarShooter.setVelocity(nectarShooterShootVelocity);
        telemetry.addData("Shooter speed: ",  nectarShooterShootVelocity);
    }

    public void maintain() {
        nectarShooter.setVelocity(nectarShooterMaintainVelocity);
        telemetry.addData("Shooter speed: ",  nectarShooterMaintainVelocity);
    }
}
