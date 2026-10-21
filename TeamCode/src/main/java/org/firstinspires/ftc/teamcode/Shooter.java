package org.firstinspires.ftc.teamcode;

import com.pedropathing.controllers.Controller;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static com.pedropathing.ivy.commands.Commands.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    public static final Controller shooterController = Controller.proportional(0.5);
    public final DcMotorEx  shooterMotor;

    private final Telemetry telemetry;
    private boolean enabled;

    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        this.telemetry = telemetry;
    }

    public Command start() {
        return instant(
            () -> enabled = true
        ).requiring(shooterMotor);
    }

    public Command stop() {
        return instant(
            () -> enabled = false
        ).requiring(shooterMotor);
    }

    public Command shoot() {
        return infinite(() -> {
            if (enabled) {
                shooterMotor.setPower(1);
            }
            else {
                shooterMotor.setPower(0);
            }
        });
    }
}