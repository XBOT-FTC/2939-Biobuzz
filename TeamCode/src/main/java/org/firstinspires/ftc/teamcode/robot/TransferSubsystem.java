package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

public class TransferSubsystem {
    Servo nectarSwing;
    Servo pollenSwing;

    Telemetry telemetry;

    private double pollenSwingFeedPosition = 0;
    private double pollenSwingResetPosition = 0;

    private double nectarSwingResetPosition = 0;
    private double nectarSwingFeedPosition = 0;

    public TransferSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        nectarSwing = hardwareMap.get(Servo.class, Constants.nectarSwingServo());
        pollenSwing = hardwareMap.get(Servo.class, Constants.pollenSwingServo());

        this.telemetry = telemetry;
    }

    public void execute() {
        telemetry.addData("nectar servo position", nectarSwing.getPosition());
        telemetry.addData("pollen servo position", pollenSwing.getPosition());
    }

    public void startFeedNectar() {
        nectarSwing.setPosition(nectarSwingFeedPosition);
    }

    public void startFeedPollen() {
        pollenSwing.setPosition(pollenSwingFeedPosition);
    }

    public void endFeedNectar() {
        nectarSwing.setPosition(nectarSwingResetPosition);
    }

    public void endFeedPollen() {
        pollenSwing.setPosition(pollenSwingResetPosition);
    }

}
