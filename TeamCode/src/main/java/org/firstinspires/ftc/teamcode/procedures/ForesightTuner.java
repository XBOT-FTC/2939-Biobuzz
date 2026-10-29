package org.firstinspires.ftc.teamcode.procedures;

import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.localization.Localizer;
import com.pedropathing.tuning.autotune.Inputs;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.procedures.Strafe.StrafeBraking;
import org.firstinspires.ftc.teamcode.procedures.Strafe.StrafeDeceleration;
import org.firstinspires.ftc.teamcode.procedures.Strafe.StrafeTranslational;
import org.firstinspires.ftc.teamcode.procedures.Strafe.StrafeVelocity;
import org.firstinspires.ftc.teamcode.procedures.forward.ForwardBraking;
import org.firstinspires.ftc.teamcode.procedures.forward.ForwardDeceleration;
import org.firstinspires.ftc.teamcode.procedures.forward.ForwardTranslational;
import org.firstinspires.ftc.teamcode.procedures.forward.ForwardVelocity;
import org.firstinspires.ftc.teamcode.procedures.heading.HeadingBraking;
import org.firstinspires.ftc.teamcode.procedures.heading.HeadingTuner;

import java.util.List;
import java.util.function.Function;

public class ForesightTuner extends Procedure {
    Function<HardwareMap, Localizer> localizerFunction;
    Function<HardwareMap, Drivetrain> drivetrainFunction;

    public ForesightTuner(Function<HardwareMap, Localizer> localizerFunction, Function<HardwareMap, Drivetrain> drivetrainFunction) {
        super("Foresight Tuner", "A procedure for tuning the Foresight Algorithm.");
        this.localizerFunction = localizerFunction;
        this.drivetrainFunction = drivetrainFunction;
    }

    @Override
    public void run() throws InterruptedException {
        Inputs distanceInput = inputs("Distance", "The distance to drive in inches for the Max Achievable Forward and Strafe Identifiers");
        Inputs.Field<Double> distance = distanceInput.d("Distance").withDefault(48.0);
        awaitInputs(distanceInput);

        double forwardVelocity = runOpMode(new ForwardVelocity(localizerFunction, drivetrainFunction, distance.get()));
        double strafeVelocity = runOpMode(new StrafeVelocity(localizerFunction, drivetrainFunction, distance.get()));

        Inputs velocityInput = inputs("Velocity", "The velocity to drive to in inches for the Max Achievable Forward and Strafe Deceleration Identifiers");
        Inputs.Field<Double> velocity = velocityInput.d("Velocity").withDefault(30.0);
        awaitInputs(velocityInput);

        double forwardDeceleration = runOpMode(new ForwardDeceleration(localizerFunction, drivetrainFunction, velocity.get()));
        double strafeDeceleration = runOpMode(new StrafeDeceleration(localizerFunction, drivetrainFunction, velocity.get()));

        List<Double> headingBraking = runOpMode(new HeadingBraking(localizerFunction, drivetrainFunction));
        double heading = runOpMode(new HeadingTuner(localizerFunction, drivetrainFunction));

        double headingLinear = headingBraking.get(0);
        double headingQuadratic = headingBraking.get(1);

        Inputs distanceBrakingInput = inputs("Distance", "The distance to drive in inches for the Forward and Strafe Braking Identifiers. Distance must be at least 15 inches for accurate results.");
        Inputs.Field<Double> distanceBraking = distanceBrakingInput.d("Distance").withDefault(36.0);
        awaitInputs(distanceBrakingInput);
        double safeDistanceBraking = Math.max(distanceBraking.get(), 15.0);

        List<Double> forwardBraking = runOpMode(new ForwardBraking(localizerFunction, drivetrainFunction, headingLinear, headingQuadratic, heading, safeDistanceBraking));
        List<Double> strafeBraking = runOpMode(new StrafeBraking(localizerFunction, drivetrainFunction, headingLinear, headingQuadratic, heading, safeDistanceBraking));

        double forwardLinear = forwardBraking.get(0);
        double forwardQuadratic = forwardBraking.get(1);
        double strafeLinear = strafeBraking.get(0);
        double strafeQuadratic = strafeBraking.get(1);

        List<Double> forwardTranslational = runOpMode(new ForwardTranslational(localizerFunction, drivetrainFunction));
        List<Double> strafeTranslational = runOpMode(new StrafeTranslational(localizerFunction, drivetrainFunction));

        double forwardTranslationalPrimary = forwardTranslational.get(0);
        double forwardTranslationalSecondary = forwardTranslational.get(1);
        double coast = forwardTranslational.get(2);
        double brake = forwardTranslational.get(3);

        double strafeTranslationalPrimary = strafeTranslational.get(0);
        double strafeTranslationalSecondary = strafeTranslational.get(1);

        result("maxAchievableForwardVelocity", forwardVelocity);
        result("maxAchievableStrafeVelocity", strafeVelocity);
        result("naturalForwardDeceleration", forwardDeceleration);
        result("naturalStrafeDeceleration", strafeDeceleration);
        result("headingBrakingLinearCoefficient", headingLinear);
        result("headingBrakingQuadraticCoefficient", headingQuadratic);
        result("heading kP", heading);
        result("forwardBrakingLinearCoefficient", forwardLinear);
        result("forwardBrakingQuadraticCoefficient", forwardQuadratic);
        result("strafeBrakingLinearCoefficient", strafeLinear);
        result("strafeBrakingQuadraticCoefficient", strafeQuadratic);
        result("forwardTranslational Primary kP", forwardTranslationalPrimary);
        result("forwardTranslational Secondary kP", forwardTranslationalSecondary);
        result("strafeTranslational Primary kP", strafeTranslationalPrimary);
        result("strafeTranslational Secondary kP", strafeTranslationalSecondary);
        result("coast kV", coast);
        result("brake kV", brake);

        code(Language.JAVA,
        "public static ForesightConfig foresightConfig = new ForesightConfig(\n" +
                "            c -> {\n" +
                "                Controller primaryTranslationalForward = Controller.proportional("+forwardTranslationalPrimary+");\n" +
                "                Controller secondaryTranslationalForward = Controller.proportional("+forwardTranslationalSecondary+");\n" +
                "                Controller primaryTranslationalLateral = Controller.proportional("+strafeTranslationalPrimary+");\n" +
                "                Controller secondaryTranslationalLateral = Controller.proportional("+strafeTranslationalSecondary+");\n" +
                "\n" +
                "                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));\n" +
                "                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));\n" +
                "\n" +
                "                c.coast.set(Controller.proportionalFeedforward("+coast+"));\n" +
                "                c.brake.set(Controller.proportionalFeedforward("+brake+"));\n" +
                "\n" +
                "                c.headingFeedback.set(Controller.proportional("+heading+"));\n" +
                "                c.headingBrakeCoefficients.set(Vector2D.cartesian("+headingLinear+", "+headingQuadratic+"));\n" +
                "\n" +
                "                c.linearBrakeCoefficients.set(Matrix.diag("+forwardLinear+", "+strafeLinear+"));\n" +
                "                c.quadraticBrakeCoefficients.set(Matrix.diag("+forwardQuadratic+", "+strafeQuadratic+"));\n" +
                "\n" +
                "                c.maxAchievableForwardVelocity.set("+forwardVelocity+");\n" +
                "                c.maxAchievableStrafeVelocity.set("+strafeVelocity+");\n" +
                "                c.naturalForwardDeceleration.set("+forwardDeceleration+");\n" +
                "                c.naturalStrafeDeceleration.set("+strafeDeceleration+");\n" +
                "            }\n" +
                "    );");
    }
}

