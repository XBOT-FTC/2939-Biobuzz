package org.firstinspires.ftc.teamcode;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Constants {
    public static String intakeMotorName = "intake";

    // TODO: flip directions until "Tuning > Tests > Driving Test" drives forward on +y stick.
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

                // TODO: measure pod offsets from robot center (inches), then confirm with Tuning > Pinpoint Tuner
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
            }
    );
}
