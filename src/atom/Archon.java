package atom;

import battlecode.common.*;
import java.util.*;

public class Archon {
    static int startSpawn = 0; //counter for initial spawn order

    static ArrayList<RobotType> spawnOrder = new ArrayList<RobotType>();
    static int spawnOrderCounter = 0;
    static ArrayList<Direction> spawnDirections = new ArrayList<Direction>();
    static int ogArchonNumber = 0;

    static boolean enemyArchonNear = false;

    //static boolean seenEnemy = false;

    static void runArchon(RobotController rc) throws GameActionException {
        //allows for differing random numbers across instances on the same turn
        int random = (int) (Math.random() * 8);
        rc.writeSharedArray(61, random);
        random = (int) (Math.random() * 6);
        rc.writeSharedArray(60, random);

        UnitCounter.reset(rc);

        if (rc.getRoundNum() % 3 == 0) {
            Communication.clearEnemyLocations(rc);
        }

        /*if (!seenEnemy) {
            int[] enemyLocations = Communication.getEnemyLocations(rc);
            for (int i = 0; i < enemyLocations.length; i++) {
                if (enemyLocations[i] != 0) {
                    seenEnemy = true;
                }
            }
        }*/
        if (Communication.getArchonIds(rc)[Communication.getArchonSpawnIndex(rc)] == rc.getID()
                || rc.getArchonCount() != ogArchonNumber) {
            enemyArchonNear = false;
            checkEnemyNear(rc);

            if (!enemyArchonNear) {
                if (startSpawn <= 3) {
                    gameStartSequence(rc);
                } else {
                    normalSpawnSequence(rc);
                }
            }
        }
    }

    public static void gameStartSequence(RobotController rc) throws GameActionException {
        if (!rc.isActionReady() && rc.getTeamLeadAmount(rc.getTeam()) >= RobotType.MINER.buildCostLead) {
            Communication.increaseArchonSpawnIndex(rc);
        } else {
            Direction dir = openSpawnLocation(rc, RobotType.MINER);
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
                Communication.increaseArchonSpawnIndex(rc);
                startSpawn++;
            }
        }
    }

    public static void normalSpawnSequence(RobotController rc) throws GameActionException {
        int leadAmnt = rc.getTeamLeadAmount(rc.getTeam());
        int lastLeadAmnt = Communication.getLastLeadAmnt(rc);
        int income = leadAmnt - lastLeadAmnt;
        Communication.setLastLeadAmnt(rc, leadAmnt);
        //System.out.println(income);

        /*if (income > 0) {
            if (income >= 25 && spawnOrder.size() == 3) {
                spawnOrder.add(RobotType.SOLDIER);
            } else if (income < 25 && spawnOrder.size() > 3) {
                spawnOrder = new ArrayList<RobotType>();
                spawnOrder.add(RobotType.SOLDIER);
                spawnOrder.add(RobotType.SOLDIER);
                spawnOrder.add(RobotType.MINER);
                if (spawnOrderCounter > spawnOrder.size() - 1) {
                    spawnOrderCounter = spawnOrder.size() - 1;
                }
            }
        }*/

        /*if (UnitCounter.getMiners(rc) > 100 && spawnOrder.size() == 3) {
            spawnOrder.add(RobotType.SOLDIER);
        } else if (UnitCounter.getMiners(rc) < 80 && spawnOrder.size() == 4) {
            spawnOrder.remove(RobotType.SOLDIER);
        }*/

        /*if (rc.getRoundNum() > 150 && spawnOrder.size() == 2) {
            spawnOrder.add(RobotType.SOLDIER);
        }*/

        RobotType spawn = spawnOrder.get(spawnOrderCounter);
        Direction spawnDir = openSpawnLocation(rc, spawn);
        if (!rc.isActionReady() && rc.getTeamLeadAmount(rc.getTeam()) >= spawn.buildCostLead) {
            Communication.increaseArchonSpawnIndex(rc);
        } else {
            switch (spawn) {
                case SOLDIER:
                    if (rc.getTeamGoldAmount(rc.getTeam()) >= RobotType.SAGE.buildCostGold) {
                        if (rc.canBuildRobot(RobotType.SAGE, spawnDir)) {
                            rc.buildRobot(RobotType.SAGE, spawnDir);
                            Communication.increaseArchonSpawnIndex(rc);
                            increaseSpawnOrderCounter();
                        }
                    } else if (rc.canBuildRobot(RobotType.SOLDIER, spawnDir)) {
                        rc.buildRobot(RobotType.SOLDIER, spawnDir);
                        Communication.increaseArchonSpawnIndex(rc);
                        increaseSpawnOrderCounter();
                    }
                    break;
                case MINER:
                    if (rc.canBuildRobot(RobotType.MINER, spawnDir)) {
                        rc.buildRobot(RobotType.MINER, spawnDir);
                        Communication.increaseArchonSpawnIndex(rc);
                        increaseSpawnOrderCounter();
                    }
                    break;
            }
        }
    }

    //returns open spawn direction
    public static Direction openSpawnLocation(RobotController rc, RobotType type) throws GameActionException {
        int rand = (int) (Math.random() * 3);
        if (rc.canBuildRobot(type, spawnDirections.get(rand))) {
            return spawnDirections.get(rand);
        } else {
            for (Direction dir : spawnDirections) {
                if (rc.canBuildRobot(type, dir)) {
                    return dir;
                }
            }
        }
        return Direction.CENTER;
    }

    public static void increaseSpawnOrderCounter() {
        if (spawnOrderCounter >= spawnOrder.size() - 1) {
            spawnOrderCounter = 0;
        } else {
            spawnOrderCounter++;
        }
    }

    public static void checkEnemyNear(RobotController rc) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots(-1, rc.getTeam().opponent());
        for (int i = 0; i < robots.length; i++) {
            RobotInfo robot = robots[i];
            if (robot.getType() == RobotType.ARCHON || robot.getType() == RobotType.SOLDIER
                    || robot.getType() == RobotType.SAGE) {
                //if (robot.getType() == RobotType.ARCHON) {
                enemyArchonNear = true;
                if (!rc.isActionReady() && rc.getTeamLeadAmount(rc.getTeam()) >= RobotType.SOLDIER.buildCostLead) {
                    Communication.increaseArchonSpawnIndex(rc);
                } else {
                    Direction spawnDirection = openSpawnLocation(rc, RobotType.SOLDIER);
                    if (rc.canBuildRobot(RobotType.SOLDIER, spawnDirection)) {
                        rc.buildRobot(RobotType.SOLDIER, spawnDirection);
                        Communication.increaseArchonSpawnIndex(rc);
                    }
                }
            }
        }
    }

    public static void init(RobotController rc) throws GameActionException {
        Communication.addArchonId(rc, rc.getID());
        ogArchonNumber = rc.getArchonCount();

        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.MINER);

        MapLocation center = new MapLocation(rc.getMapWidth() / 2, rc.getMapHeight() / 2);
        Direction dirToCenter = rc.getLocation().directionTo(center);
        spawnDirections.add(dirToCenter);
        spawnDirections.add(dirToCenter.rotateLeft());
        spawnDirections.add(dirToCenter.rotateRight());
        spawnDirections.add(dirToCenter.rotateLeft().rotateLeft());
        spawnDirections.add(dirToCenter.rotateRight().rotateRight());
        spawnDirections.add(dirToCenter.rotateLeft().rotateLeft().rotateLeft());
        spawnDirections.add(dirToCenter.rotateRight().rotateRight().rotateRight());
        spawnDirections.add(dirToCenter.opposite());

        /*RobotInfo[] robots = rc.senseNearbyRobots(rc.getType().visionRadiusSquared, rc.getTeam().opponent());
        for (int i = 0; i < robots.length; i++) {
            RobotInfo robot = robots[i];
            if (robot.getType() == RobotType.ARCHON) {
                Communication.addEnemyArconLocation(Communication.convertMapLocationToInt(robot.getLocation()), rc);
            }
        }*/
    }
}
