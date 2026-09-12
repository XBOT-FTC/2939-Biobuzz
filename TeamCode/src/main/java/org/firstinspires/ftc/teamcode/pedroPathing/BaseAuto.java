package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.localization.Localizer;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.Constants;


@Autonomous(name = "Pedro Base Auto", group = "Autonomous")
public class BaseAuto extends LinearOpMode {

    protected Follower follower;

    protected Timer pathTimer;
    protected ElapsedTime opModeTimer;

    protected enum PathState {
        START,
        PARK,
        END
    }
    public BaseAuto() {
        follower = Constants.createFollower(hardwareMap);
    }

    protected PathState pathState;

    // Helper method to easily change states and reset the timer
    public void setPathState(PathState state) {
        pathState = state;
        pathTimer.resetTimer();
    }



    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.createFollower(hardwareMap);
        pathTimer = new Timer();
        opModeTimer = new ElapsedTime();

        waitForStart();

        Scheduler.reset();

        if (isStopRequested()) return;

        opModeTimer.reset();

        Scheduler.schedule(autonomousStateMachine());

        while (opModeIsActive() && !isStopRequested()) {
            follower.update();
        }

    }

    protected Command autonomousStateMachine() {
        return sequential(
                // Autos are to be placed here
        );
    }
}
