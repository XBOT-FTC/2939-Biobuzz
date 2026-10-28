package org.firstinspires.ftc.teamcode;


import com.pedropathing.algorithm.Foresight;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

import org.firstinspires.ftc.teamcode.procedures.ForesightTuner;
import org.firstinspires.ftc.teamcode.procedures.mecanumTuner.MecanumTuner;
import org.firstinspires.ftc.teamcode.procedures.pinpoint.PinpointTuner;
import org.firstinspires.ftc.teamcode.procedures.tests.Tests;
import org.firstinspires.ftc.teamcode.procedures.threeWheelTuner.ThreeWheelTuner;

public class Tuning {
    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }

    @Tuner
    public static Procedure pinpointTuner() {
        return new PinpointTuner();
    }
    @Tuner
    public static Procedure threeWheelTuner() {
        return new ThreeWheelTuner();
    }

    @Tuner
    public static Procedure foresightTuner() {
        return new ForesightTuner(
                (hardwareMap) -> new PinpointLocalizer(hardwareMap, Constants.localizerConfig),
                (hardwareMap) -> new Mecanum(hardwareMap, Constants.driveConfig)
        );
    }

    @Tuner
    public static Procedure tests() {
        return new Tests(
                hardwareMap -> new Mecanum(hardwareMap, Constants.driveConfig),
                (hardwareMap -> new PinpointLocalizer(hardwareMap, Constants.localizerConfig)),
                () -> new Foresight(Constants.foresightConfig)
        );
    }}
