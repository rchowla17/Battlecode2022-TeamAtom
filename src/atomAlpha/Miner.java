package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Miner {
    static MapLocation currentLoc;
    static int randomMoves = 0;
    static boolean goRand = false;

    static void runMiner(RobotController rc) throws GameActionException {
        rc.setIndicatorString("");
        currentLoc = rc.getLocation();

        Team opponent = rc.getTeam().opponent();
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        int nearbyMinerCount = 0;

        //checks nearby possible metals and removes them if they are no longer there
        int closestMetal = getClosestPossibleMetalLocation(rc);
        MapLocation closestMetalLocation = null;
        if (closestMetal != 0) {
            closestMetalLocation = Communication.convertIntMapLocation(closestMetal);
            if (rc.canSenseLocation(closestMetalLocation)) {
                if (rc.senseLead(closestMetalLocation) < 15) {
                    Communication.removeMetalLocation(closestMetal, rc);
                }
            }
        }

        if (nearbyRobots.length > 0) {
            for (int i = 0; i < nearbyRobots.length; i++) {
                RobotInfo robot = nearbyRobots[i];
                //stops unit from blocking spawn zone
                if (robot.getTeam().equals(rc.getTeam()) && robot.getType().equals(RobotType.ARCHON)
                        && rc.getLocation().distanceSquaredTo(robot.getLocation()) <= 4) {
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                } else if (robot.getTeam().equals(rc.getTeam()) && robot.getType().equals(RobotType.MINER)) {
                    nearbyMinerCount++; //nearby miner counter
                } else if (robot.getTeam().equals(opponent) && robot.getType().equals(RobotType.SOLDIER)
                        || robot.getType().equals(RobotType.SAGE)) {
                    //run from enemy attackers
                    Direction dir = rc.getLocation().directionTo(robot.getLocation()).opposite();
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                    }
                }
            }
        }

        //tries to stop miners from flocking
        if (nearbyMinerCount > 8) {
            Direction dir = null;
            dir = Pathfinding.randomDir(rc);
            if (rc.canMove(dir)) {
                rc.move(dir);
                //rc.setIndicatorString("MOVINGRAND");
                Data.randCounter++;
            }
        }

        //logic for miners to spread out after finishing mining out a location
        if(goRand){
            Direction dir = Pathfinding.randomDir(rc);
            if (rc.canMove(dir)) {
                rc.move(dir);
                //rc.setIndicatorString("MOVINGRAND");
                Data.randCounter++;
                randomMoves++;
            }

            if(randomMoves >= 10){
                randomMoves = 0;
                goRand = false;
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
                if (target.type.equals("LEAD")) {
                    if(rc.senseLead(target.location)<=3){
                        goRand = true;
                    }

                    //continously mines possible areas
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            MapLocation mineLocation = new MapLocation(target.location.x + dx, target.location.y + dy);
                            while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation)>3) {
                                rc.mineLead(mineLocation);
                                //rc.setIndicatorString("MININGLEAD");
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
                            dir = Pathfinding.basicBug(rc, target.location);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                //rc.setIndicatorString("MOVINGTO");
                            }
                        } else {
                            //random movement since there is no found target
                            dir = Pathfinding.randomDir(rc);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                //rc.setIndicatorString("MOVINGRAND");
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
                                //rc.setIndicatorString("MININGGOLD");
                            }
                        }
                    }

                    if (rc.isActionReady()) {
                        metalLocations = senseNearbyMetals(rc);
                        target = findNearestMetalLocation(metalLocations, rc);

                        Direction dir = null;
                        if (target != null) {
                            dir = Pathfinding.basicBug(rc, target.location);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                //rc.setIndicatorString("MOVINGTO");
                            }
                        } else {
                            dir = Pathfinding.randomDir(rc);
                            if (rc.canMove(dir)) {
                                rc.move(dir);
                                //rc.setIndicatorString("MOVINGRAND");
                                Data.randCounter++;
                            }
                        }
                    }
                }
            } else {
                Direction dir = null;
                dir = Pathfinding.basicBug(rc, target.location);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    //rc.setIndicatorString("MOVINGTO");
                }
            }
        } else {
            Direction dir = null;
            if (closestMetal != 0) {
                dir = Pathfinding.basicBug(rc, closestMetalLocation);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                }
            }else{
                dir = Pathfinding.randomDir(rc);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    //rc.setIndicatorString("MOVINGRAND");
                    Data.randCounter++;
                }
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

    static MetalLocation findNearestMetalLocation(ArrayList<MetalLocation> metalLocations, RobotController rc) throws GameActionException{
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
        
        if(target != null){
            String x = String.format("%02d", target.location.x);
            String y = String.format("%02d", target.location.y);
            String locationS = x + y;
            Communication.addMetalLocation(rc, Integer.parseInt(locationS));
        }

        return target;
    }

    static int getClosestPossibleMetalLocation(RobotController rc) throws GameActionException {
        int[] metalLocations = Communication.getMetalLocations(rc);
        int closestMetalLocation = 0;
        int distanceSquaredToClosest = Integer.MAX_VALUE;

        for (int i = 0; i < metalLocations.length; i++) {
            if (!(metalLocations[i] == 0)) {
                MapLocation location = Communication.convertIntMapLocation(metalLocations[i]);
                if (rc.getLocation().distanceSquaredTo(location) < distanceSquaredToClosest) {
                    closestMetalLocation = metalLocations[i];
                    distanceSquaredToClosest = rc.getLocation().distanceSquaredTo(location);
                }
            }
        }
        return closestMetalLocation;
    }
}
