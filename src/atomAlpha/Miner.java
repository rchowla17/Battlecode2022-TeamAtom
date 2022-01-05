package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Miner {
    static MapLocation currentLoc;

    static void runMiner(RobotController rc) throws GameActionException {
        rc.setIndicatorString("");
        currentLoc = rc.getLocation();

        Team opponent = rc.getTeam().opponent();
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();

        if (nearbyRobots.length > 0) {
            for (int i = 0; i < nearbyRobots.length; i++) {
                RobotInfo robot = nearbyRobots[i];
                if (robot.getTeam() == rc.getTeam() && robot.getType() == RobotType.ARCHON
                        && rc.getLocation().distanceSquaredTo(robot.getLocation()) <= 4) {
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                } else if (robot.getTeam() == opponent && robot.getType() == RobotType.SOLDIER
                        || robot.getType() == RobotType.SAGE) {
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                }
            }
        }

        ArrayList<MetalLocation> metalLocations = senseNearbyMetals(rc);

        MetalLocation target = findNearestMetalLocation(metalLocations);
        int distanceToTarget = -1;
        if (target != null) {
            distanceToTarget = currentLoc.distanceSquaredTo(target.location);
        }

        if (distanceToTarget != -1) {
            if (distanceToTarget <= 2) {
                if (target.type.equals("LEAD")) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            MapLocation mineLocation = new MapLocation(target.location.x + dx, target.location.y + dy);
                            while (rc.canMineLead(mineLocation)) {
                                rc.mineLead(mineLocation);
                                rc.setIndicatorString("MININGLEAD");
                            }
                        }
                    }

                    if (rc.isActionReady()) {
                        metalLocations = senseNearbyMetals(rc);
                        target = findNearestMetalLocation(metalLocations);

                        Direction dir = null;
                        if (target != null) {
                            dir = Pathfinding.getBasicBug(rc, target.location);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                rc.setIndicatorString("MOVINGTO");
                            }
                        } else {
                            dir = Pathfinding.getRandom(rc);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                rc.setIndicatorString("MOVINGRAND");
                                Data.randCounter++;
                            }
                        }
                    }
                } else if (target.type.equals("GOLD")) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            MapLocation mineLocation = new MapLocation(target.location.x + dx, target.location.y + dy);
                            while (rc.canMineGold(mineLocation)) {
                                rc.mineGold(mineLocation);
                                rc.setIndicatorString("MININGGOLD");
                            }
                        }
                    }

                    if (rc.isActionReady()) {
                        metalLocations = senseNearbyMetals(rc);
                        target = findNearestMetalLocation(metalLocations);

                        Direction dir = null;
                        if (target != null) {
                            dir = Pathfinding.getBasicBug(rc, target.location);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                rc.setIndicatorString("MOVINGTO");
                            }
                        } else {
                            dir = Pathfinding.getRandom(rc);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                rc.setIndicatorString("MOVINGRAND");
                                Data.randCounter++;
                            }
                        }
                    }
                }
            } else {
                Direction dir = null;
                dir = Pathfinding.getBasicBug(rc, target.location);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    rc.setIndicatorString("MOVINGTO");
                }
            }
        } else {
            Direction dir = null;
            dir = Pathfinding.getRandom(rc);
            if (rc.canMove(dir)) {
                rc.move(dir);
                rc.setIndicatorString("MOVINGRAND");
                Data.randCounter++;
            }
        }
    }

    static ArrayList<MetalLocation> senseNearbyMetals(RobotController rc) throws GameActionException {
        ArrayList<MetalLocation> metalLocations = new ArrayList<MetalLocation>();
        MapLocation currentLoc = rc.getLocation();
        MapLocation[] locations = rc.getAllLocationsWithinRadiusSquared(currentLoc,
                rc.getType().visionRadiusSquared);
        for (int i = 0; i < locations.length; i++) {
            MapLocation senseLoc = locations[i];
            if (rc.canSenseLocation(senseLoc)) {
                int goldAmnt = rc.senseGold(senseLoc);
                int leadAmnt = rc.senseLead(senseLoc);
                if (goldAmnt > 0) {
                    metalLocations.add(new MetalLocation("GOLD", goldAmnt, senseLoc));
                } else if (leadAmnt > 0) {
                    metalLocations.add(new MetalLocation("LEAD", leadAmnt, senseLoc));
                }
            }
        }
        return metalLocations;
    }

    static MetalLocation findNearestMetalLocation(ArrayList<MetalLocation> metalLocations) {
        MetalLocation target = null;
        int distanceToTarget = Integer.MAX_VALUE;
        boolean foundGold = false;

        for (MetalLocation loc : metalLocations) {
            String type = loc.type;
            int distanceToLoc = currentLoc.distanceSquaredTo(loc.location);

            if (type.equals("GOLD")) {
                if (!foundGold) {
                    target = loc;
                    distanceToTarget = distanceToLoc;
                    foundGold = true;
                } else {
                    if (distanceToLoc < distanceToTarget) {
                        target = loc;
                        distanceToTarget = distanceToLoc;
                    }
                }
            }
            if (distanceToLoc < distanceToTarget && !foundGold) {
                target = loc;
                distanceToTarget = distanceToLoc;
            }
        }

        return target;
    }
}
