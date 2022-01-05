package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Communication {
    static void communicateScoutMode(RobotController rc) throws GameActionException {
        rc.writeSharedArray(63, 1);
    }

    static void setCommArrayIndexToZero(RobotController rc, int index) throws GameActionException {
        rc.writeSharedArray(index, 0);
    }
}
