package sprint1;

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

    static void addMetalLocation(RobotController rc, int location) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(4), rc.readSharedArray(5), rc.readSharedArray(6),
                rc.readSharedArray(7), rc.readSharedArray(8), rc.readSharedArray(9) };
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] != 0) {
                MapLocation mapLocation = convertIntMapLocation(locations[i]);
                MapLocation thisLocation = convertIntMapLocation(location);
                if (mapLocation.distanceSquaredTo(thisLocation) < 20) {
                    break;
                }
            } else if (locations[i] == 0) {
                rc.writeSharedArray(i + 4, location);
                break;
            }
        }
    }

    static void removeMetalLocation(int location, RobotController rc) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(4), rc.readSharedArray(5), rc.readSharedArray(6),
                rc.readSharedArray(7), rc.readSharedArray(8), rc.readSharedArray(9) };
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] == location) {
                setCommArrayIndexToZero(rc, i + 4);
            }
        }
    }

    static int[] getMetalLocations(RobotController rc) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(4), rc.readSharedArray(5), rc.readSharedArray(6),
                rc.readSharedArray(7), rc.readSharedArray(8), rc.readSharedArray(9) };
        return locations;
    }

    static MapLocation convertIntMapLocation(int location) {
        String locationS = Integer.toString(location);
        int x = 0, y = 0;
        if (locationS.length() == 3) {
            x = Integer.parseInt(locationS.substring(0, 1));
            y = Integer.parseInt(locationS.substring(1));
        } else if (locationS.length() == 4) {
            x = Integer.parseInt(locationS.substring(0, 2));
            y = Integer.parseInt(locationS.substring(2));
        }
        return new MapLocation(x, y);
    }
}
