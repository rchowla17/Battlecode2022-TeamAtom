package atomV4d4;

import battlecode.common.*;
import java.util.*;

public class Laboratory {
    public void runLaboratory(RobotController rc) throws GameActionException {
        if (rc.canTransmute())
            rc.transmute();
    }
}