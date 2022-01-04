package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Miner {
    static boolean canMineGold = false;
    static MapLocation currentLoc;

    static void runMiner(RobotController rc) throws GameActionException {
        rc.setIndicatorString("");
        currentLoc = rc.getLocation();

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
                                rc.setIndicatorString("MINING");
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
        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -4; dy <= 4; dy++) {
                MapLocation senseLoc = new MapLocation(currentLoc.x + dx, currentLoc.y + dy);
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
        }
        return metalLocations;
    }

    static MetalLocation findNearestMetalLocation(ArrayList<MetalLocation> metalLocations) {
        MetalLocation target = null;
        int distanceToTarget = Integer.MAX_VALUE;

        for (MetalLocation loc : metalLocations) {
            String type = loc.type;
            if (canMineGold || (!canMineGold && type.equals("LEAD"))) {
                int distanceToLoc = currentLoc.distanceSquaredTo(loc.location);
                if (distanceToLoc < distanceToTarget) {
                    target = loc;
                    distanceToTarget = distanceToLoc;
                }
            }
        }

        return target;
    }
}
