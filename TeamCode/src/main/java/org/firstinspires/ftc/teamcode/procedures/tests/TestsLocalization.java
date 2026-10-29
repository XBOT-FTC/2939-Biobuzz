package org.firstinspires.ftc.teamcode.procedures.tests;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Pose;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.Function;

class TestsLocalization extends TuningOpMode<Boolean> {
    Function<HardwareMap, Drivetrain> drivetrainFunction;
    Function<HardwareMap, Localizer> localizerFunction;

    public TestsLocalization(Function<HardwareMap, Drivetrain> drivetrainFunction, Function<HardwareMap, Localizer> localizerFunction) {
        super("Localization Test", "Verifies localization and manual control.", true);
        this.drivetrainFunction = drivetrainFunction;
        this.localizerFunction = localizerFunction;
    }

    @Override
    public Boolean runTuningOpMode() throws InterruptedException {
        Localizer localizer = localizerFunction.apply(hardwareMap);
        Drivetrain drivetrain = drivetrainFunction.apply(hardwareMap);

        localizer.setPose(Pose.zero());

        Thread.sleep(1000);
        waitForStart();
        localizer.setPose(Pose.zero());

        while (opModeIsActive()) {
            drivetrain.drive(new DrivePowers(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x), true);
            localizer.update();
            telemetry.addData("Pose", localizer.pose());
            telemetry.update();
        }
        return true;
    }
}


