package org.firstinspires.ftc.teamcode;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;

import com.pedropathing.localization.Localizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
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


    public static Follower createFollower(HardwareMap h, Drivetrain drivetrain, Localizer localizer, Foresight foresight) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return new Follower(localizer,drivetrain,foresight);
    }

    public static double inPerTick2939 = 0.02393;
    public static double lateralInPerTick2939 = 0.02519;

    public static double ks2939 = 1.0967513649697667;

    public static double kv2939 = 0.0042730455553834186;

    public static double ka2939 = .00067;

    public static double trackWidth2939 = 1226.5643209432749;

    public static double axialGain2939 = 3;

    public static double axialVelGain2939 = 1.9;

    public static double headingGain2939 = 4.5;

    public static double headingVelGain2939 = .5;

    public static double lateralGain2939 = 1.3;

    public static double lateralVelGain2939 = 0;

    public static class Params {
        // IMU orientation
        // TODO: fill in these values based on
        //   see https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html?highlight=imu#physical-hub-mounting
        public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        // drive model parameters
        public double inPerTick = Constants.inPerTick2939;
        public double lateralInPerTick = Constants.lateralInPerTick2939;
        public double trackWidthTicks = Constants.trackWidth2939;

        // feedforward parameters (in tick units)
        public double kS = Constants.ks2939;
        public double kV = Constants.kv2939;
        public double kA = ka2939;

        // path profile parameters (in inches)
        public double maxWheelVel = 50;
        public double minProfileAccel = -30;
        public double maxProfileAccel = 50;

        // turn profile parameters (in radians)
        public double maxAngVel = Math.PI; // shared with path
        public double maxAngAccel = Math.PI;

        // path controller gains
        public double axialGain = axialGain2939;
        public double lateralGain = lateralGain2939;
        public double headingGain = headingGain2939; // shared with turn

        public double axialVelGain = axialVelGain2939;
        public double lateralVelGain = lateralVelGain2939;
        public double headingVelGain = headingVelGain2939; // shared with turn
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



