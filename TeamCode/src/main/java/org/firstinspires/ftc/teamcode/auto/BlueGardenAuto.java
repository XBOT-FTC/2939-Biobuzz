package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.PoseFactory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Same routine as {@link RedGardenAuto}: start, intake our GARDEN POLLEN, PARK in our LOADING ZONE.
 *
 * All the paths and intake steps live in RedGardenAuto. The field is 180-degree rotationally symmetric,
 * so this only swaps in a PoseFactory that turns every red pose about field center (141.5 in. field).
 * That puts start, GARDEN and LOADING ZONE on blue's side.
 */
@Autonomous(name = "Blue Garden Auto", group = "Autonomous")
public class BlueGardenAuto extends RedGardenAuto {
    public BlueGardenAuto() {
        super(PoseFactory.degrees().rotateAround(70.75, 70.75, 180));
    }
}
