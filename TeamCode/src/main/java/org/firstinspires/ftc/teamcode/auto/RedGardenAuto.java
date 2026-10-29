package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.robot.Robot;

/**
 * Starts against the audience wall, intakes the 4 POLLEN in our GARDEN, then PARKs in our LOADING ZONE.
 *
 * Poses are for red, in inches, with the origin at the audience/red-wall corner,
 * +x toward the blue wall and +y away from the audience. They assume an 18 in. robot with the intake on the front.
 */
@Autonomous(name = "Red Garden Auto", group = "Autonomous")
public class RedGardenAuto extends OpMode {
    private Follower follower;
    private Intake intake;

    // Touching the audience wall on red's side, intake facing the wall.
    private final Pose startPose;
    // Above the GARDEN POLLEN, which sit in a line from the corner along the audience wall.
    private final Pose gardenApproachPose;
    // Intake against the wall, covering all 4 GARDEN POLLEN.
    private final Pose gardenPose;
    // Bends around the red-wall FLOWER (y ~48) without reaching the HIVE (x > 47).
    private final Pose parkControl1;
    private final Pose parkControl2;
    // Partly in the LOADING ZONE (x 0-11, y ~96-119) and off the wall, so we score both PARK and LEAVE.
    private final Pose parkPose;

    public RedGardenAuto() {
        this(PoseFactory.degrees());
    }

    protected RedGardenAuto(PoseFactory poseFactory) {
        startPose = poseFactory.of(36, 9, -90);
        gardenApproachPose = poseFactory.of(10, 24, -90);
        gardenPose = poseFactory.of(10, 10, -90);
        parkControl1 = poseFactory.of(32, 24, 0);
        parkControl2 = poseFactory.of(32, 100, 0);
        parkPose = poseFactory.of(16, 108, 0);
    }

    private Path toGarden() {
        return line(startPose, gardenApproachPose).constant(startPose);
    }

    private Path intoGarden() {
        return line(gardenApproachPose, gardenPose)
                .constant(gardenPose)
                .with(Constants.foresightConfig.maxVelocityConstraint.at(15.0)); // inches/second
    }

    private Path park() {
        return curve(gardenPose, parkControl1, parkControl2, parkPose).linear(gardenPose, parkPose);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, toGarden()),
                intake.on(),
                follow(follower, intoGarden()),
                waitMs(1000),
                intake.off(),
                follow(follower, park())
        );
    }

    @Override
    public void init() {
        // Scheduler is static, so clear commands left over from the last OpMode.
        Scheduler.reset();

        follower = Robot.createFollower(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        intake = new Intake(hardwareMap, telemetry);
        schedule(intake.periodic());
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("Pose", follower.pose());
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }
}
