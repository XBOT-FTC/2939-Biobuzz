package org.firstinspires.ftc.teamcode.procedures.mecanumTuner;

import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

class SpinMotor extends TuningOpMode<Void> {
    private final String name;

    public SpinMotor(String displayName, String hardwareName) {
        super(displayName, "The " + displayName.toLowerCase() + " motor will spin. The interactive diagram shows which way is forward. Click stop when you know if it is spinning forward or reversed.", true);
        this.name = hardwareName;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected Void runTuningOpMode() {
        DcMotor motor = hardwareMap.dcMotor.get(name);
        waitForStart();
        motor.setPower(0.5);
        while (opModeIsActive()) {
        }
        motor.setPower(0);
        return null;
    }
}