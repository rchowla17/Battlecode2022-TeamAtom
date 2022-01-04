package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Pathfinding {
    public static Direction getBasicBug(RobotController rc, MapLocation target) throws GameActionException {
        Direction dir = rc.getLocation().directionTo(target);
        if (dir == null) {
            return Direction.CENTER;
        } else if (rc.canMove(dir)) {
            return dir;
        } else {
            Direction attemptDir = null;
            Direction returnDirection = null;
            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 1:
                        attemptDir = dir.rotateRight();
                        break;
                    case 2:
                        attemptDir = dir.rotateLeft();
                        break;
                    case 3:
                        attemptDir = dir.rotateRight().rotateRight();
                        break;
                    case 4:
                        attemptDir = dir.rotateLeft().rotateLeft();
                        break;
                    case 5:
                        attemptDir = dir.opposite().rotateRight();
                        break;
                    case 6:
                        attemptDir = dir.opposite().rotateLeft();
                        break;
                    case 7:
                        attemptDir = dir.opposite();
                        break;
                    default:
                        break;
                }
                if (rc.canMove(attemptDir)) {
                    returnDirection = attemptDir;
                }
            }
            return returnDirection;
        }
    }

    public static Direction getRandom(RobotController rc, MapLocation target) throws GameActionException {
        int random = (int) (Math.random() * 8);
        Direction dir = Data.directions[random];

        if (dir == null) {
            return Direction.CENTER;
        } else if (rc.canMove(dir)) {
            return dir;
        } else {
            Direction attemptDir = null;
            Direction returnDirection = null;
            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 1:
                        attemptDir = dir.rotateRight();
                        break;
                    case 2:
                        attemptDir = dir.rotateLeft();
                        break;
                    case 3:
                        attemptDir = dir.rotateRight().rotateRight();
                        break;
                    case 4:
                        attemptDir = dir.rotateLeft().rotateLeft();
                        break;
                    case 5:
                        attemptDir = dir.opposite().rotateRight();
                        break;
                    case 6:
                        attemptDir = dir.opposite().rotateLeft();
                        break;
                    case 7:
                        attemptDir = dir.opposite();
                        break;
                    default:
                        break;
                }
                if (rc.canMove(attemptDir)) {
                    returnDirection = attemptDir;
                }
            }
            return returnDirection;
        }
    }
}
