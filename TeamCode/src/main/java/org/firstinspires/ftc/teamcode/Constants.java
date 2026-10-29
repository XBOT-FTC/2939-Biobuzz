package org.firstinspires.ftc.teamcode;

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


    public static PinpointConstants localizerConstants = new PinpointConstants()
            .hardwareMapName("pinpoint")
            .distanceUnit(DistanceUnit.INCH)

            // TODO: measure pod offsets from robot center (inches), then confirm with Tuning > Offsets Tuner
            .forwardPodY(0.0) //
            .strafePodX(0.0)

            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)

            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
//    // This is for pure driver encoder odometrry
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



