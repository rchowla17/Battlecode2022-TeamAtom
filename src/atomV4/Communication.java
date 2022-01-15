package atomV4;

import battlecode.common.*;
import java.util.*;

public class Communication {
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
                MapLocation mapLocation = convertIntToMapLocation(locations[i]);
                MapLocation thisLocation = convertIntToMapLocation(location);
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

    static void addEnemyLocation(RobotController rc, int location) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(10), rc.readSharedArray(11), rc.readSharedArray(12),
                rc.readSharedArray(13), rc.readSharedArray(14), rc.readSharedArray(15) };
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] != 0) {
                MapLocation mapLocation = convertIntToMapLocation(locations[i]);
                MapLocation thisLocation = convertIntToMapLocation(location);
                if (mapLocation.distanceSquaredTo(thisLocation) < 50) {
                    break;
                }
            } else if (locations[i] == 0) {
                rc.writeSharedArray(i + 10, location);
                break;
            }
        }
    }

    static int[] getEnemyLocations(RobotController rc) throws GameActionException {
        int[] locations = new int[] { rc.readSharedArray(10), rc.readSharedArray(11), rc.readSharedArray(12),
                rc.readSharedArray(13), rc.readSharedArray(14), rc.readSharedArray(15) };
        return locations;
    }

    static void clearEnemyLocations(RobotController rc) throws GameActionException {
        setCommArrayIndexToZero(rc, 10);
        setCommArrayIndexToZero(rc, 11);
        setCommArrayIndexToZero(rc, 12);
        setCommArrayIndexToZero(rc, 13);
        setCommArrayIndexToZero(rc, 14);
        setCommArrayIndexToZero(rc, 15);
    }

    static void addArchonId(RobotController rc, int id) throws GameActionException {
        int[] ids = new int[] { rc.readSharedArray(50), rc.readSharedArray(51), rc.readSharedArray(52),
                rc.readSharedArray(53) };
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == 0) {
                rc.writeSharedArray(i + 50, id);
                break;
            }
        }
    }

    static int getArchonSpawnIndex(RobotController rc) throws GameActionException {
        return rc.readSharedArray(54);
    }

    static void increaseArchonSpawnIndex(RobotController rc) throws GameActionException {
        if (rc.readSharedArray(54) >= rc.getArchonCount() - 1) {
            rc.writeSharedArray(54, 0);
        } else {
            rc.writeSharedArray(54, rc.readSharedArray(54) + 1);
        }

    }

    static int[] getArchonIds(RobotController rc) throws GameActionException {
        int[] ids = new int[] { rc.readSharedArray(50), rc.readSharedArray(51), rc.readSharedArray(52),
                rc.readSharedArray(53) };
        return ids;
    }

    static void setLastLeadAmnt(RobotController rc, int value) throws GameActionException {
        rc.writeSharedArray(63, value);
    }

    static int getLastLeadAmnt(RobotController rc) throws GameActionException {
        return rc.readSharedArray(63);
    }

    static MapLocation convertIntToMapLocation(int location) {
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

    static void sendDistressSignal(RobotController rc, int location) throws GameActionException {
        if (rc.readSharedArray(59) == 0) {
            rc.writeSharedArray(59, location);
        }
    }

    static void endDistressSignal(RobotController rc, int location) throws GameActionException {
        if (rc.readSharedArray(59) == location) {
            setCommArrayIndexToZero(rc, 59);
        }
    }

    static int checkDistressSignal(RobotController rc) throws GameActionException {
        return rc.readSharedArray(59);
    }

    static int convertMapLocationToInt(MapLocation location) {
        String x = String.format("%02d", location.x);
        String y = String.format("%02d", location.y);
        String locationS = x + y;
        return Integer.parseInt(locationS);
    }
}
