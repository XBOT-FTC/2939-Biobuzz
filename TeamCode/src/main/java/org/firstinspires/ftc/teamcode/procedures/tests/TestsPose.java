package org.firstinspires.ftc.teamcode.procedures.tests;

import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Pose;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.Function;

class TestsPose extends TuningOpMode<Boolean> {
    Function<HardwareMap, Localizer> localizerFunction;

    public TestsPose(Function<HardwareMap, Localizer> localizerFunction) {
        super("Pose Test", "Verifies localizer output without a drivetrain.", true);
        this.localizerFunction = localizerFunction;
    }

    @Override
    public Boolean runTuningOpMode() throws InterruptedException {
        Localizer localizer = localizerFunction.apply(hardwareMap);
        Thread.sleep(1000);
        waitForStart();
        localizer.setPose(Pose.zero());
        localizer.update();
        while (opModeIsActive()) {
            localizer.update();
            telemetry.addData("Pose", localizer.pose());
            telemetry.update();
        }
        return true;
    }
}


