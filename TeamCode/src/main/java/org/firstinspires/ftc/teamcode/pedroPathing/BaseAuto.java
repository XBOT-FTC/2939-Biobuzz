package org.firstinspires.ftc.teamcode.pedroPathing;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Constants;

import dev.frozenmilk.util.collections.Cons;


@Autonomous(name = "Pedro Base Auto", group = "Autonomous")
public abstract class BaseAuto extends OpMode {

    protected Drivetrain drive;
    protected Follower follower;
    protected Localizer localizer;
    protected Foresight foresight;
    protected Timer pathTimer;
    protected ElapsedTime opModeTimer;

    protected final PoseFactory p = PoseFactory.degrees();

    protected final Pose startPose = p.of(24, 24, 0);
    protected final Pose park = p.of(48, 48, 90);


    // Helper method to easily change states and reset the timer
    public void setPathState(PathState state) {
        pathState = state;
        pathTimer.reset();
    }

    protected void autonomousStateMachine() {
        // Autos are to be placed here
    }

    protected enum PathState {
        START,
        PARK,
        END
    }

    protected PathState pathState;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);

    }

    @Override
    public void start() {
        follower = Constants.createFollower(hardwareMap,drive,localizer,foresight);
        pathTimer = new Timer();
        opModeTimer = new ElapsedTime();
        opModeTimer.reset();

        setPathState(PathState.START);
        autonomousStateMachine();
        }

    @Override
    public void loop() {
        follower.update();
    }


    private Path park() {
        return line(startPose, park).linear(startPose, park);
    }
    }