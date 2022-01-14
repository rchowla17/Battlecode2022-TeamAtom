package atomV2d1;

import battlecode.common.*;
import java.util.*;

public class Builder {
    static boolean isScout = false;
    static Direction scoutDir = null;

    //only being used to find enemy archons rn?
    static void runBuilder(RobotController rc) throws GameActionException {
        if (isScout) {
            scout(rc);
        } else {

        }
    }

    static void init(RobotController rc) throws GameActionException {
        if (rc.canSenseRadiusSquared(3)) {
            for (RobotInfo robot : rc.senseNearbyRobots(3, rc.getTeam())) {
                if (robot.getType().equals(RobotType.ARCHON)) {
                    scoutDir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    checkIfScout(rc);
                }
            }
        }
    }

    static void scout(RobotController rc) throws GameActionException {
        Direction dir = Pathfinding.scoutBug(rc, scoutDir);
        if (rc.canMove(dir)) {
            rc.move(dir);
        }

        int actionRadius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(actionRadius, opponent);
        for (int i = 0; i < enemies.length; i++) {
            RobotInfo enemy = enemies[i];
            if (enemy.getType().equals(RobotType.ARCHON)) {
                String x = String.format("%02d", enemy.getLocation().x);
                String y = String.format("%02d", enemy.getLocation().y);
                String locationS = x + y;
                rc.setIndicatorString(locationS);
                Communication.addEnemyArconLocation(Integer.parseInt(locationS), rc);
            }
        }
    }

    static void checkIfScout(RobotController rc) throws GameActionException {
        if (scoutDir.equals(Direction.NORTH) || scoutDir.equals(Direction.WEST) || scoutDir.equals(Direction.EAST)
                || scoutDir.equals(Direction.SOUTH)) {
            isScout = true;
        }
    }
}
