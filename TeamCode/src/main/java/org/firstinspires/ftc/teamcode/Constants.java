package org.firstinspires.ftc.teamcode;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig driveConfig = new MecanumConfig(
            c -> {
                c.frontLeftName.set("left_front");
                c.backLeftName.set("left_back");
                c.frontRightName.set("right_front");
                c.backRightName.set("right_back");

                c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.manualBrakeMode.set(true);
            }
    );

    public static PinpointConfig localizerConfig = new PinpointConfig(
            c -> {
                c.name.set("pinpoint");

                // These were set at random
                c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
                c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
                c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);

                c.xPodOffset.set(2.0);
                c.yPodOffset.set(2.0);
            }
    );

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                c.forwardTranslational.set(Controller.proportional(0.0));
                c.strafeTranslational.set(Controller.proportional(0.0));

                c.maxAchievableForwardVelocity.set(5.0);
                c.maxAchievableStrafeVelocity.set(5.0);
                c.naturalForwardDeceleration.set(1.0);
                c.naturalStrafeDeceleration.set(1.0);

    // TODO: weigh the robot. Mass is in kg and Pedro uses it for centripetal correction.
    public static FollowerConstants followerConstants = new FollowerConstants()
            //placeholder mass, update when robot is weighed
            .mass(10);
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    // TODO: flip directions until "Tuning > Localization Test" drives forward on +y stick.
    // xVelocity/yVelocity come from the Forward/Lateral Velocity Tuners.
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .leftFrontMotorName("leftFrontMotor")
            .leftRearMotorName("leftRearMotor")
            .rightFrontMotorName("rightFrontMotor")
            .rightRearMotorName("rightRearMotor")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);


    public static Follower createFollower(HardwareMap h, Drivetrain drivetrain, Localizer localizer, Foresight foresight) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return new Follower(localizer,drivetrain,foresight);
    }

    public static double inPerTick2939 = 0.02393;
    public static double lateralInPerTick2939 = 0.02519;

    public static double ks2939 = 1.0967513649697667;

            // TODO: measure pod offsets from robot center (inches), then confirm with Tuning > Offsets Tuner
            .forwardPodY(0.0) //
            .strafePodX(0.0)

    public static double ka2939 = .00067;

    public static double trackWidth2939 = 1226.5643209432749;

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }

}
//    // This is for pure driver encoder odometry
//    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants() {}
//            .leftFrontMotorName("leftFront")
//            .leftRearMotorName("leftRear")
//            .rightFrontMotorName("rightFront")
//            .rightRearMotorName("rightRear")
//
//            .leftFrontEncoderDirection(Encoder.FORWARD)
//            .leftRearEncoderDirection(Encoder.FORWARD)
//            .rightFrontEncoderDirection(Encoder.FORWARD)
//            .rightRearEncoderDirection(Encoder.FORWARD);



