package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Pathfinding {
    public static Direction basicBug(RobotController rc, MapLocation target) throws GameActionException {
        Direction dir = rc.getLocation().directionTo(target);
        if (dir.equals(null)) {
            return Direction.CENTER;
        } else if (rc.canMove(dir)) {
            return dir;
        } else {
            Direction attemptDir = null;
            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 1:
                        attemptDir = dir.rotateRight();
                        break;
                    case 2:
                        attemptDir = dir.rotateRight().rotateRight();
                        break;
                    case 3:
                        attemptDir = dir.rotateRight().rotateRight().rotateRight();
                        break;
                    case 4:
                        attemptDir = dir.rotateLeft();
                        break;
                    case 5:
                        attemptDir = dir.rotateLeft().rotateLeft();
                        break;
                    case 6:
                        attemptDir = dir.rotateLeft().rotateLeft().rotateLeft();
                        break;
                    case 7:
                        attemptDir = dir.opposite();
                        break;
                    default:
                        break;
                }
                if (rc.canMove(attemptDir)) {
                    return attemptDir;
                }
            }
            return Direction.CENTER;
        }
    }

    public static Direction basicBug(RobotController rc, Direction dir) throws GameActionException {
        if (dir.equals(null)) {
            return Direction.CENTER;
        } else if (rc.canMove(dir)) {
            return dir;
        } else {
            Direction attemptDir = null;
            for (int i = 1; i < 8; i++) {
                switch (i) {
                    case 1:
                        attemptDir = dir.rotateRight();
                        break;
                    case 2:
                        attemptDir = dir.rotateRight().rotateRight();
                        break;
                    case 3:
                        attemptDir = dir.rotateRight().rotateRight().rotateRight();
                        break;
                    case 4:
                        attemptDir = dir.rotateLeft();
                        break;
                    case 5:
                        attemptDir = dir.rotateLeft().rotateLeft();
                        break;
                    case 6:
                        attemptDir = dir.rotateLeft().rotateLeft().rotateLeft();
                        break;
                    case 7:
                        attemptDir = dir.opposite();
                        break;
                    default:
                        break;
                }
                if (rc.canMove(attemptDir)) {
                    return attemptDir;
                }
            }
            return Direction.CENTER;
        }
    }

    public static Direction scoutBug(RobotController rc, Direction dir) throws GameActionException {
        if (dir.equals(null)) {
            return Direction.CENTER;
        } else if (rc.canMove(dir)) {
            return dir;
        } else {
            if (!rc.canSenseLocation(rc.getLocation().add(dir))) {
                Builder.scoutDir = dir.rotateRight().rotateRight();
                return dir.rotateRight().rotateRight();
            } else {
                Direction attemptDir = null;
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
                        return attemptDir;
                    }
                }
                return Direction.CENTER;
            }
        }
    }

    public static Direction randomDir(RobotController rc) throws GameActionException {
        if (Data.randCounter == 0) {
            //int random = (int) (Math.random() * 8);
            int random = rc.readSharedArray(61);
            Direction dir = Data.directions[random];

            if (dir.equals(null)) {
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
                        Data.randDirection = returnDirection;
                        return returnDirection;
                    }
                }
                return Direction.CENTER;
            }
        } else {
            if (Data.randCounter < 8) {
                Data.randCounter++;
            } else {
                Data.randCounter = 0;
            }
            return Data.randDirection;
        }
    }
}
