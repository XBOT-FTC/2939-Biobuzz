package org.firstinspires.ftc.teamcode;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;

import com.pedropathing.localization.Localizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
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



