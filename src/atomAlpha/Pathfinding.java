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

    public static Direction advancedPathfinding(RobotController rc, MapLocation target) throws GameActionException {
        /*
        find the direction to move like normal
        go left or right from there and see if there's less rubble
        
        
        */
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
                Direction leftDir = attemptDir.rotateLeft();
                Direction rightDir = attemptDir.rotateRight();

                MapLocation frontLoc = rc.getLocation().add(attemptDir);
                MapLocation leftLocation = rc.getLocation().add(leftDir);
                MapLocation rightLocation = rc.getLocation().add(rightDir);

                ArrayList<MapLocation> options = new ArrayList<MapLocation>();
                if (rc.canSenseLocation(frontLoc)) {
                    options.add(frontLoc);
                }
                if (rc.canSenseLocation(leftLocation)) {
                    options.add(leftLocation);
                }
                if (rc.canSenseLocation(rightLocation)) {
                    options.add(rightLocation);
                }

                MapLocation best = leastRubble(rc, options);
                attemptDir = rc.getLocation().directionTo(best);

                if (rc.canMove(attemptDir)) {
                    return attemptDir;
                }
            }
            return Direction.CENTER;
        }
    }

    public static Direction advancedPathfinding(RobotController rc, Direction dir) throws GameActionException {
        /*
        find the direction to move like normal
        go left or right from there and see if there's less rubble
        
        
        */
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
                Direction leftDir = attemptDir.rotateLeft();
                Direction rightDir = attemptDir.rotateRight();

                MapLocation frontLoc = rc.getLocation().add(attemptDir);
                MapLocation leftLocation = rc.getLocation().add(leftDir);
                MapLocation rightLocation = rc.getLocation().add(rightDir);

                ArrayList<MapLocation> options = new ArrayList<MapLocation>();
                if (rc.canSenseLocation(frontLoc)) {
                    options.add(frontLoc);
                }
                if (rc.canSenseLocation(leftLocation)) {
                    options.add(leftLocation);
                }
                if (rc.canSenseLocation(rightLocation)) {
                    options.add(rightLocation);
                }

                MapLocation best = leastRubble(rc, options);
                attemptDir = rc.getLocation().directionTo(best);

                if (rc.canMove(attemptDir)) {
                    return attemptDir;
                }
            }
            return Direction.CENTER;
        }
    }

    public static MapLocation leastRubble(RobotController rc, ArrayList<MapLocation> options)
            throws GameActionException {
        int minRubble = Integer.MAX_VALUE;
        MapLocation out = rc.getLocation();
        for (MapLocation ml : options) {
            if ((rc.senseRubble(ml) < minRubble)) {
                minRubble = rc.senseRubble(ml);
                out = ml;
            }
        }
        return out;
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
