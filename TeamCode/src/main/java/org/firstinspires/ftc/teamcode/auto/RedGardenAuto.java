package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.robot.Robot;

import java.util.List;

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
    private List<LynxModule> hubs;

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

    // follow() only ends at the path's end, so a robot stuck on POLLEN or a wall would never move on to PARK.
    // GARDEN limits start from Mona Shores' BIOBUZZ GardenGather (same Pedro 3 + Ivy stack: 4 s line-up, 4 s drive in,
    // 1.5 s intake dwell), with 2 s more per drive because our robot is slower. GARDEN POLLEN scores nothing in AUTO
    // but PARK does, so the rest of the 30 s (16.5 s) goes to PARK.
    private Command followFor(Path path, double timeoutMs) {
        return race(follow(follower, path), waitMs(timeoutMs));
    }

    private Command autoRoutine() {
        return sequential(
                followFor(toGarden(), 6000),
                intake.on(),
                followFor(intoGarden(), 6000),
                waitMs(1500),
                intake.off(),
                followFor(park(), 16500)
        );
    }

    @Override
    public void init() {
        // One bulk read per loop instead of one hub call per encoder/velocity read.
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

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
        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }

        follower.update();
        Scheduler.execute();

        telemetry.addData("Pose", follower.pose());
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }
}
