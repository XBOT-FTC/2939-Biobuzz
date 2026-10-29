package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;




@Autonomous(name = "Pedro Base Auto", group = "Autonomous")
public class BaseAuto extends LinearOpMode {

    protected Drivetrain drive;
    protected Follower follower;
    protected Localizer localizer;
    protected Foresight foresight;
    protected Timer pathTimer;
    protected ElapsedTime opModeTimer;
    protected enum PathState {
        START,
        PARK,
        END
    }

    protected PathState pathState;

    // Subclasses set this to where the robot is placed on the field.
    protected Pose startPose = new Pose();

    // Helper method to easily change states and reset the timer
    public void setPathState(PathState state) {
        pathState = state;
        pathTimer.reset();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        // Scheduler is static, so clear commands left over from the last OpMode.
        Scheduler.reset();
        pathTimer = new Timer();
        opModeTimer = new ElapsedTime();

        waitForStart();
        opModeTimer.reset();

        setPathState(PathState.START);

        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
            follower.update();

            autonomousStateMachine();
            Scheduler.execute();

            telemetry.addData("State", pathState);
            telemetry.addData("Pose", follower.getPose());
            telemetry.update();
        }
    }

    protected void autonomousStateMachine() {
        // Autos are to be placed here
    }
}
