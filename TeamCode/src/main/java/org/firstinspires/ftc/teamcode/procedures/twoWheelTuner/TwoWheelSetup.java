package org.firstinspires.ftc.teamcode.procedures.twoWheelTuner;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

class TwoWheelSetup {

    String forwardPodName;
    String strafePodName;
    String imuName;
    RevHubOrientationOnRobot.LogoFacingDirection logoDirection;
    RevHubOrientationOnRobot.UsbFacingDirection usbDirection;

    TwoWheelSetup(
            String forwardPodName,
            String strafePodName,
            String imuName,
            RevHubOrientationOnRobot.LogoFacingDirection logoDirection,
            RevHubOrientationOnRobot.UsbFacingDirection usbDirection
    ) {
        this.forwardPodName = forwardPodName;
        this.strafePodName = strafePodName;
        this.imuName = imuName;
        this.logoDirection = logoDirection;
        this.usbDirection = usbDirection;
    }
}

