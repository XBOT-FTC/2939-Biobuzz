package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.TeamDriveTrain;

@Autonomous(name = "literally just forward", group = "Autonomous")
public class DriveForwardAuto extends LinearOpMode {
    private static final double forwardPower = -0.3;
    private static final long driveMS = 5000;

    @Override
    public void runOpMode() {
        TeamDriveTrain drive = new TeamDriveTrain(hardwareMap);

        waitForStart();
        if (isStopRequested()) return;

        drive.setPowers(forwardPower, forwardPower, forwardPower, forwardPower);
        sleep(driveMS);
        drive.stop();
    }
}
