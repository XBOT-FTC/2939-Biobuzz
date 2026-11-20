package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class IntakeSubsystem {

    DcMotor intake;

    Telemetry telemetry;
    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        hardwareMap.get(Constants.intakeMotor());
        this.telemetry = telemetry;
    }

    public void intake() {
       intake.setPower(0.5);
       telemetry.addData("intake state:", "intaking");


    }

    public void eject() {
        intake.setPower(-0.5);
        telemetry.addData("intake state:", "ejecting");
    }

    public void stop() {
        intake.setPower(0);
        telemetry.addData("intake state:", "stopped");
    }

}
