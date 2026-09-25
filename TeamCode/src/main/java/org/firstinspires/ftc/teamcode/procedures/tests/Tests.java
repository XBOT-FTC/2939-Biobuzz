package org.firstinspires.ftc.teamcode.procedures.tests;

import com.pedropathing.algorithm.Algorithm;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.tuning.autotune.DisplayName;
import com.pedropathing.tuning.autotune.Inputs;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.Function;
import java.util.function.Supplier;

public class Tests extends Procedure {
    enum Test {
        @DisplayName("Hold Test")
        HOLD,
        @DisplayName("Line Test")
        LINE,
        @DisplayName("Curve Test")
        CURVED,
        @DisplayName("Interpolation Test")
        INTERPOLATION_CURVED,
        @DisplayName("Localization Test")
        LOCALIZATION,
        @DisplayName("Odometry Test")
        ODOMETRY,
        @DisplayName("Driving Test")
        DRIVING,
        @DisplayName("Pose Test")
        POSE
    }
    Function<HardwareMap, Drivetrain> drivetrainFunction;
    Function<HardwareMap, Localizer> localizerFunction;
    Supplier<Algorithm> algorithmSupplier;
    Function<HardwareMap, Follower> followerFunction;

    public Tests(Function<HardwareMap, Drivetrain> drivetrainFunction, Function<HardwareMap, Localizer> localizerFunction, Supplier<Algorithm> algorithmSupplier) {
        super("Tests", "A procedure for testing the Follower.");
        this.drivetrainFunction = drivetrainFunction;
        this.localizerFunction = localizerFunction;
        this.algorithmSupplier = algorithmSupplier;
    }

    @Override
    public void run() throws InterruptedException {
        boolean completed = false;
        boolean algorithm = true, localizer = true, drivetrain = true;

        if (algorithmSupplier == null)
            algorithm = false;

        if (localizerFunction == null)
            localizer = false;

        if (drivetrainFunction == null)
            drivetrain = false;

        if (algorithm && localizer && drivetrain)
            followerFunction = (hardwareMap) -> new Follower(localizerFunction.apply(hardwareMap), drivetrainFunction.apply(hardwareMap), algorithmSupplier.get());

        Inputs inputs = inputs("Select", "Select");
        Inputs.Field<Test> selectedTest = inputs.e("Test", Test.class).withDefault(Test.LINE);
        Inputs.Field<Double> distance = inputs.d("Distance").withDefault(48.0);

        awaitInputs(inputs);

        switch (selectedTest.get()) {
            case HOLD:
                if (!algorithm)
                    throw new IllegalArgumentException("Algorithm is required for Hold Test.");
                completed = runOpMode(new TestsHold(followerFunction));
                break;
            case LINE:
                if (!algorithm)
                    throw new IllegalArgumentException("Algorithm is required for Hold Test.");
                completed = runOpMode(new TestsLine(followerFunction, distance.get()));
                break;
            case CURVED:
                if (!algorithm)
                    throw new IllegalArgumentException("Algorithm is required for Hold Test.");
                completed = runOpMode(new TestsCurve(followerFunction, distance.get()));
                break;
            case INTERPOLATION_CURVED:
                if (!algorithm)
                    throw new IllegalArgumentException("Algorithm is required for Hold Test.");
                completed = runOpMode(new TestsInterpolation(followerFunction, distance.get()));
                break;
            case LOCALIZATION:
                if (!drivetrain)
                    throw new IllegalArgumentException("Drivetrain is required for Localization Test.");
                if (!localizer)
                    throw new IllegalArgumentException("Localizer is required for Localization Test.");
                completed = runOpMode(new TestsLocalization(drivetrainFunction, localizerFunction));
                break;
            case ODOMETRY:
                if (!drivetrain)
                    throw new IllegalArgumentException("Drivetrain is required for Odometry Test.");
                if (!localizer)
                    throw new IllegalArgumentException("Localizer is required for Odometry Test.");
                completed = runOpMode(new TestsOdometry(drivetrainFunction, localizerFunction));
                if (!completed)
                    abort("Failed odometry test. Please check your odometry pods and ensure they are functioning correctly.");
                break;
            case POSE:
                if (!localizer)
                    throw new IllegalArgumentException("Localizer is required for Pose Test.");
                completed = runOpMode(new TestsPose(localizerFunction));
                break;
            case DRIVING:
                if (!drivetrain)
                    throw new IllegalArgumentException("Drivetrain is required for Driving Test.");
                completed = runOpMode(new TestsDriving(drivetrainFunction));
                break;
        }

        result("Completed", completed);
    }
}


