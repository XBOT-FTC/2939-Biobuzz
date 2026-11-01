package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake {
    private Mode mode = Mode.OFF;
    public static double intakePower = -1;
    public static double outtakePower = 1;
    public static double offPower = 0;
    // getCurrent() is a separate hub call (not in the bulk read), ~2ms per loop; enable only when debugging.
    public static boolean logCurrent = false;

    private final DcMotorEx intakeMotor;
    private final Telemetry telemetry;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, Constants.intakeMotorName);
        this.telemetry = telemetry;
    }

    public void update(boolean intakeHeld, boolean outtakeHeld) {
        // Outtake wins if both are held.
        if (outtakeHeld) {
            mode = Mode.OUTTAKE;
        } else if (intakeHeld) {
            mode = Mode.INTAKE;
        } else {
            mode = Mode.OFF;
        }

        switch (mode) {
            case INTAKE:
                intakeMotor.setPower(intakePower);
                break;
            case OUTTAKE:
                intakeMotor.setPower(outtakePower);
                break;
            case OFF:
                intakeMotor.setPower(offPower);
                break;
        }

        telemetry.addData("Intake Mode", mode);
        telemetry.addData("Intake Velocity", intakeMotor.getVelocity());
        if (logCurrent) {
            telemetry.addData("Intake Current", intakeMotor.getCurrent(CurrentUnit.MILLIAMPS));
        }
    }

    enum Mode {
        INTAKE,
        OUTTAKE,
        OFF
    }
}
