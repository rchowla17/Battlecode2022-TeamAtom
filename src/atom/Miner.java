package atom;

import battlecode.common.*;
import java.util.*;

public class Miner {
    static MapLocation currentLoc;
    static int randomMoves = 0;

    static void runMiner(RobotController rc) throws GameActionException {
        rc.setIndicatorString("");
        currentLoc = rc.getLocation();

        Team opponent = rc.getTeam().opponent();
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        int nearbyMinerCount = 0;

        UnitCounter.addMiner(rc);

        //checks nearby possible metals and removes them if they are no longer there
        /*int closestPossibleMetal = getClosestPossibleMetalLocation(rc);
        MapLocation closestPossibleMetalLocation = null;
        if (closestPossibleMetal != 0) {
            closestPossibleMetalLocation = Communication.convertIntToMapLocation(closestPossibleMetal);
            if (rc.canSenseLocation(closestPossibleMetalLocation)) {
                if (rc.senseLead(closestPossibleMetalLocation) < 30) {
                    Communication.removeMetalLocation(closestPossibleMetal, rc);
                }
            }
        }*/

        if (nearbyRobots.length > 0) {
            for (int i = 0; i < nearbyRobots.length; i++) {
                RobotInfo robot = nearbyRobots[i];
                //stops unit from blocking spawn zone
                if (robot.getTeam().equals(rc.getTeam()) && robot.getType().equals(RobotType.ARCHON)
                        && rc.getLocation().distanceSquaredTo(robot.getLocation()) <= 4) {
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    //MapLocation center = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
                    //Direction dir = rc.getLocation().directionTo(center);
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                } else if (robot.getTeam().equals(rc.getTeam()) && robot.getType().equals(RobotType.MINER)) {
                    nearbyMinerCount++; //nearby miner counter
                } else if (robot.getTeam().equals(opponent) && robot.getType().equals(RobotType.SOLDIER)
                        || robot.getType().equals(RobotType.SAGE)) {

                    String x = String.format("%02d", robot.getLocation().x);
                    String y = String.format("%02d", robot.getLocation().y);
                    String locationS = x + y;
                    Communication.addEnemyLocation(rc, Integer.parseInt(locationS));
                    //run from enemy attackers
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                } else if (robot.getTeam().equals(opponent)) {
                    String x = String.format("%02d", robot.getLocation().x);
                    String y = String.format("%02d", robot.getLocation().y);
                    String locationS = x + y;
                    Communication.addEnemyLocation(rc, Integer.parseInt(locationS));
                }
            }
        }

        //tries to stop miners from flocking
        if (nearbyMinerCount > 2) {
            Direction dir = null;
            dir = Pathfinding.wander(rc);
            if (rc.canMove(dir)) {
                rc.move(dir);
                rc.setIndicatorString("WANDERFROMFLOCK");
                Data.randCounter++;
            }
        }

        ArrayList<MetalLocation> metalLocations = senseNearbyMetals(rc);

        MetalLocation target = findNearestMetalLocation(metalLocations, rc);
        int distanceToTarget = -1;
        if (target != null) {
            distanceToTarget = currentLoc.distanceSquaredTo(target.location);
        }

        if (distanceToTarget != -1) {
            //if target is within mining distance
            if (distanceToTarget <= 2) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        MapLocation mineLocation = new MapLocation(target.location.x + dx, target.location.y + dy);
                        if (target.type.equals("LEAD")) {
                            while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 3) {
                                rc.mineLead(mineLocation);
                                //rc.setIndicatorString("MININGGOLD");
                            }
                        }
                        if (target.type.equals("GOLD")) {
                            while (rc.canMineGold(mineLocation)) {
                                rc.mineGold(mineLocation);
                                //rc.setIndicatorString("MININGGOLD");
                            }
                        }
                    }
                }

                //if there is cooldown left after mining, the unit will try to move to another target
                if (rc.isActionReady()) {
                    metalLocations = senseNearbyMetals(rc);
                    target = findNearestMetalLocation(metalLocations, rc);

                    Direction dir = null;
                    if (target != null) {
                        //move towards target
                        //dir = Pathfinding.basicBug(rc, target.location);
                        dir = Pathfinding.advancedPathfinding(rc, target.location);
                        if (rc.canMove(dir)) {
                            rc.move(dir);
                            rc.setIndicatorString("MOVINGTOTARGET");
                        }
                    } else {
                        //random movement since there is no found target
                        dir = Pathfinding.wander(rc);
                        if (rc.canMove(dir)) {
                            rc.move(dir);
                            rc.setIndicatorString("WANDER");
                            Data.randCounter++;
                        }
                    }
                }
            } else {
                Direction dir = null;
                //dir = Pathfinding.basicBug(rc, target.location);
                dir = Pathfinding.advancedPathfinding(rc, target.location);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    rc.setIndicatorString("MOVINGTOTARGET");
                }
            }
        } else {
            //moves towards a location in the metallocation array if it exists
            Direction dir = null;
            /*
            if (closestPossibleMetal != 0) {
                //dir = Pathfinding.basicBug(rc, closestMetalLocation);
                dir = Pathfinding.advancedPathfinding(rc, closestPossibleMetalLocation);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                }
            } else {
                dir = Pathfinding.wander(rc);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    //rc.setIndicatorString("MOVINGRAND");
                    Data.randCounter++;
                }
            }*/

            dir = Pathfinding.wander(rc);
            if (rc.canMove(dir)) {
                rc.move(dir);
                rc.setIndicatorString("WANDER");
                Data.randCounter++;
            }
        }
    }

    static ArrayList<MetalLocation> senseNearbyMetals(RobotController rc) throws GameActionException {
        int vision = rc.getType().visionRadiusSquared;
        ArrayList<MetalLocation> metalLocations = new ArrayList<MetalLocation>();
        MapLocation[] locations = rc.senseNearbyLocationsWithLead(vision, 5);
        for (int i = 0; i < locations.length; i++) {
            MapLocation senseLoc = locations[i];
            int amnt = rc.senseLead(senseLoc);
            metalLocations.add(new MetalLocation("LEAD", amnt, senseLoc));
        }
        locations = rc.senseNearbyLocationsWithGold(vision);
        for (int i = 0; i < locations.length; i++) {
            MapLocation senseLoc = locations[i];
            int amnt = rc.senseLead(senseLoc);
            metalLocations.add(new MetalLocation("GOLD", amnt, senseLoc));
        }
        return metalLocations;
    }

    static MetalLocation findNearestMetalLocation(ArrayList<MetalLocation> metalLocations, RobotController rc)
            throws GameActionException {
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

        /*if (target != null) {
            String x = String.format("%02d", target.location.x);
            String y = String.format("%02d", target.location.y);
            String locationS = x + y;
            Communication.addMetalLocation(rc, Integer.parseInt(locationS));
        }*/

        return target;
    }

    static int getClosestPossibleMetalLocation(RobotController rc) throws GameActionException {
        int[] metalLocations = Communication.getMetalLocations(rc);
        int closestMetalLocation = 0;
        int distanceSquaredToClosest = Integer.MAX_VALUE;

        for (int i = 0; i < metalLocations.length; i++) {
            if (!(metalLocations[i] == 0)) {
                MapLocation location = Communication.convertIntToMapLocation(metalLocations[i]);
                if (rc.getLocation().distanceSquaredTo(location) < distanceSquaredToClosest) {
                    closestMetalLocation = metalLocations[i];
                    distanceSquaredToClosest = rc.getLocation().distanceSquaredTo(location);
                }
            }
        }
        return closestMetalLocation;
    }

    static void init(RobotController rc) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots(3, rc.getTeam());
        for (int i = 0; i < robots.length; i++) {
            RobotInfo robot = robots[i];
            if (robot.getType() == RobotType.ARCHON) {
                Data.spawnBaseLocation = robot.getLocation();
            }
        }
    }
}
