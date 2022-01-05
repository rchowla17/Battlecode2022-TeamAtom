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

    static void addEnemyArconLocation(int location, RobotController rc) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(0), rc.readSharedArray(1), rc.readSharedArray(2),
                rc.readSharedArray(3) };
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] == location) {
                break;
            }
            if (locations[i] == 0) {
                rc.writeSharedArray(i, location);
                break;
            }
        }
    }

    static void removeEnemyArconLocation(int location, RobotController rc) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(0), rc.readSharedArray(1), rc.readSharedArray(2),
                rc.readSharedArray(3) };
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] == location) {
                setCommArrayIndexToZero(rc, i);
            }
        }
    }

    static int[] getEnemyArconLocations(RobotController rc) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(0), rc.readSharedArray(1), rc.readSharedArray(2),
                rc.readSharedArray(3) };
        return locations;
    }
}
