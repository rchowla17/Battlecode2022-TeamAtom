package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Builder {
    static boolean isScout = false;
    static Direction scoutDir = null;

    static void runBuilder(RobotController rc) throws GameActionException {
        if (isScout) {
            scout(rc);
        } else {

        }
    }

    static void init(RobotController rc) throws GameActionException {
        if (rc.canSenseRadiusSquared(3)) {
            for (RobotInfo robot : rc.senseNearbyRobots(3, rc.getTeam())) {
                if (robot.getType() == RobotType.ARCHON) {
                    scoutDir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    checkIfScout(rc);
                }
            }
        }
    }

    static void scout(RobotController rc) throws GameActionException {
        if (rc.canMove(scoutDir)) {
            rc.move(scoutDir);
        }
    }

    static void checkIfScout(RobotController rc) throws GameActionException {
        if (scoutDir == Direction.NORTH || scoutDir == Direction.WEST || scoutDir == Direction.EAST
                || scoutDir == Direction.SOUTH) {
            isScout = true;
        }
    }
}
