package atomV1d1;

import battlecode.common.*;
import java.util.*;

public class Laboratory {

    //if we can produce gold, then produce gold
    public void runLaboratory(RobotController rc) throws GameActionException {
        if (rc.canTransmute())
            ;
        rc.transmute();
    }

    public void init() {
        //dunno what to instantiate
    }
}