package atom;

import battlecode.common.*;
import java.util.*;

public class Miner2 {
    static MapLocation currentLoc;

    static void runMiner(RobotController rc) throws GameActionException {
        currentLoc = rc.getLocation();
        Team opponent = rc.getTeam().opponent();

        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        int nearbyMinerCount = 0;

        UnitCounter.addMiner(rc);

        if (nearbyRobots.length > 0) {
            for (int i = 0; i < nearbyRobots.length; i++) {
                RobotInfo robot = nearbyRobots[i];
                //stops unit from blocking spawn zone
                if (robot.getTeam().equals(rc.getTeam()) && robot.getType().equals(RobotType.ARCHON)
                        && rc.getLocation().distanceSquaredTo(robot.getLocation()) <= 4) {
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    dir = Pathfinding.greedyPathfinding(rc, dir);
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                } else if (robot.getTeam().equals(rc.getTeam()) && robot.getType().equals(RobotType.MINER)) {
                    nearbyMinerCount++;
                } else if (robot.getTeam().equals(opponent) && robot.getType().equals(RobotType.SOLDIER)
                        || robot.getType().equals(RobotType.SAGE)) {

                    Communication.addEnemyLocation(rc, Communication.convertMapLocationToInt(robot.getLocation()));
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    dir = Pathfinding.greedyPathfinding(rc, dir);
                    //Direction dir = Pathfinding.escapeEnemies(rc);
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                } else if (robot.getTeam().equals(opponent)) {
                    Communication.addEnemyLocation(rc, Communication.convertMapLocationToInt(robot.getLocation()));
                }
            }
        }

        ArrayList<MetalLocation> metalLocations = senseNearbyMetals(rc);
        MetalLocation target = findNearestMetalLocation(metalLocations, rc);
        int distanceToTarget = -1;

        if (target != null) {
            distanceToTarget = currentLoc.distanceSquaredTo(target.location);
            //if target is within mining distance
            if (distanceToTarget <= 2) {
                MapLocation[] surroundings = rc.getAllLocationsWithinRadiusSquared(target.location,
                        RobotType.MINER.actionRadiusSquared);

                MapLocation leastRubbleLocation = null;
                int rubbleAtleastRubbleLocation = Integer.MAX_VALUE;

                for (int i = 0; i < surroundings.length; i++) {
                    if (rc.canSenseLocation(surroundings[i]) && !rc.canSenseRobotAtLocation(surroundings[i])
                            && rc.senseRubble(surroundings[i]) < rubbleAtleastRubbleLocation) {
                        leastRubbleLocation = surroundings[i];
                        rubbleAtleastRubbleLocation = rc.senseRubble(surroundings[i]);
                    }
                }

                if (leastRubbleLocation != null) {
                    Direction moveToOptimalLocation = Pathfinding.greedyPathfinding(rc, leastRubbleLocation);
                    if (rc.canMove(moveToOptimalLocation)) {
                        rc.move(moveToOptimalLocation);
                    }
                }

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        MapLocation mineLocation = new MapLocation(target.location.x + dx, target.location.y + dy);
                        if (target.type.equals("LEAD")) {
                            while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 2) {
                                rc.mineLead(mineLocation);
                            }
                        }
                        if (target.type.equals("GOLD")) {
                            while (rc.canMineGold(mineLocation)) {
                                rc.mineGold(mineLocation);
                            }
                        }
                    }
                }
            } else {
                MapLocation[] surroundings = rc.getAllLocationsWithinRadiusSquared(target.location,
                        RobotType.MINER.actionRadiusSquared);

                MapLocation leastRubbleLocation = null;
                int rubbleAtleastRubbleLocation = Integer.MAX_VALUE;

                for (int i = 0; i < surroundings.length; i++) {
                    if (rc.canSenseLocation(surroundings[i]) && !rc.canSenseRobotAtLocation(surroundings[i])
                            && rc.senseRubble(surroundings[i]) < rubbleAtleastRubbleLocation) {
                        leastRubbleLocation = surroundings[i];
                        rubbleAtleastRubbleLocation = rc.senseRubble(surroundings[i]);
                    }
                }

                if (leastRubbleLocation != null) {
                    Direction moveToOptimalLocation = Pathfinding.greedyPathfinding(rc, leastRubbleLocation);
                    if (rc.canMove(moveToOptimalLocation)) {
                        rc.move(moveToOptimalLocation);
                    }
                }
            }
        } else {
            Direction dir = Pathfinding.explore(rc);
            if (rc.canMove(dir)) {
                rc.move(dir);
                //rc.setIndicatorString("WANDER");
            }
        }
    }

    static ArrayList<MetalLocation> senseNearbyMetals(RobotController rc) throws GameActionException {
        int vision = rc.getType().visionRadiusSquared;
        ArrayList<MetalLocation> metalLocations = new ArrayList<MetalLocation>();
        MapLocation[] locations = rc.senseNearbyLocationsWithLead(vision, 3);
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
