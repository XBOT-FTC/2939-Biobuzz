package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static FollowerConstants followerConstants = new FollowerConstants();
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static String leftFront(){
        return "leftFrontMotor";
    }
    public static String leftRear(){
        return "leftRearMotor";
    }
    public static String rightFront() {
        return "rightFrontMotor";
    }
    public static String rightRear() {
        return "rightRearMotor";
    }


    public static PinpointConstants localizerConstants = new PinpointConstants()
            .hardwareMapName("pinpoint")
            .distanceUnit(DistanceUnit.INCH)

            .forwardPodY(0.0) //
            .strafePodX(0.0)

            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)

            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }

    public static double inPerTick3231 = 0.02393;
    public static double lateralInPerTick3231 = 0.02519;

    public static double ks3231 = 1.0967513649697667;

    public static double kv3231 = 0.0042730455553834186;

    public static double ka3231 = .00067;

    public static double trackWidth3231 = 1226.5643209432749;

    public static double axialGain3231 = 3;

    public static double axialVelGain3231 = 1.9;

    public static double headingGain3231 = 4.5;

    public static double headingVelGain3231 = .5;

    public static double lateralGain3231 = 1.3;

    public static double lateralVelGain3231 = 0;

    public static class Params {
        // IMU orientation
        // TODO: fill in these values based on
        //   see https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html?highlight=imu#physical-hub-mounting
        public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        // drive model parameters
        public double inPerTick = Constants.inPerTick3231;
        public double lateralInPerTick = Constants.lateralInPerTick3231;
        public double trackWidthTicks = Constants.trackWidth3231;

        // feedforward parameters (in tick units)
        public double kS = Constants.ks3231;
        public double kV = Constants.kv3231;
        public double kA = ka3231;

        // path profile parameters (in inches)
        public double maxWheelVel = 50;
        public double minProfileAccel = -30;
        public double maxProfileAccel = 50;

        // turn profile parameters (in radians)
        public double maxAngVel = Math.PI; // shared with path
        public double maxAngAccel = Math.PI;

        // path controller gains
        public double axialGain = axialGain3231;
        public double lateralGain = lateralGain3231;
        public double headingGain = headingGain3231; // shared with turn

        public double axialVelGain = axialVelGain3231;
        public double lateralVelGain = lateralVelGain3231;
        public double headingVelGain = headingVelGain3231; // shared with turn
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



