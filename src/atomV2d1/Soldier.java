package atomV2d1;

import battlecode.common.*;
import java.util.*;

public class Soldier {
    static void runSoldier(RobotController rc) throws GameActionException {
        int actionRadius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();

        RobotInfo[] enemiesInActionRadius = rc.senseNearbyRobots(actionRadius, opponent);
        RobotInfo target = null;
        int targetHealth = Integer.MAX_VALUE;
        int targetValue = Integer.MAX_VALUE; //sage = 1, soldier = 2, builder = 3, archon = 4, miner = 5

        UnitCounter.addSoldier(rc);

        int closestEnemyArcon = getClosestEnemyArcon(rc);
        MapLocation closestEnemyArconLocation = null;
        if (closestEnemyArcon != 0) {
            closestEnemyArconLocation = Communication.convertIntToMapLocation(closestEnemyArcon);
            if (rc.canSenseLocation(closestEnemyArconLocation)) {
                if (rc.senseRobotAtLocation(closestEnemyArconLocation) == null) {
                    Communication.removeEnemyArconLocation(closestEnemyArcon, rc);
                }
            }
        }

        // Try to attack someone
        if (enemiesInActionRadius.length > 0) {
            for (int i = 0; i < enemiesInActionRadius.length; i++) {
                RobotInfo enemy = enemiesInActionRadius[i];
                if (enemy.getType().equals(RobotType.ARCHON)) {
                    Communication.addEnemyArconLocation(Communication.convertMapLocationToInt(enemy.getLocation()), rc);
                    swarmArcon(rc, enemy.getLocation());
                }

                int enemyValue = Data.determineEnemyValue(enemiesInActionRadius[i]);
                if (enemyValue <= targetValue && enemiesInActionRadius[i].health < targetHealth) {
                    target = enemiesInActionRadius[i];
                    targetHealth = enemiesInActionRadius[i].health;
                    targetValue = enemyValue;
                }
            }

            MapLocation toAttack = target.location;
            Communication.addEnemyLocation(rc, Communication.convertMapLocationToInt(target.location));

            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }

            if ((target.getType() == RobotType.SOLDIER || target.getType() == RobotType.SAGE)
                    && rc.getLocation().distanceSquaredTo(target.location) < actionRadius) {
                Direction away = rc.getLocation().directionTo(toAttack).opposite();
                away = Pathfinding.greedyPathfinding(rc, away);
                if (rc.canMove(away)) {
                    rc.move(away);
                }
            }

            /*Direction toTarget = Pathfinding.greedyPathfinding(rc, toAttack);
            if (rc.canMove(toTarget)) {
                rc.move(toTarget);
            }*/
        } else {
            int visionRadius = rc.getType().visionRadiusSquared;
            RobotInfo[] enemiesInVisionRange = rc.senseNearbyRobots(visionRadius, opponent);
            int targetDistance = Integer.MAX_VALUE;

            if (enemiesInVisionRange.length > 0) {
                for (int i = 0; i < enemiesInVisionRange.length; i++) {
                    RobotInfo enemy = enemiesInVisionRange[i];
                    if (enemy.getType().equals(RobotType.ARCHON)) {
                        Communication.addEnemyArconLocation(Communication.convertMapLocationToInt(enemy.getLocation()),
                                rc);
                    }

                    int enemyValue = Data.determineEnemyValue(enemiesInVisionRange[i]);
                    int distanceToEnemy = rc.getLocation().distanceSquaredTo(enemiesInVisionRange[i].getLocation());
                    if (enemyValue <= targetValue && distanceToEnemy < targetDistance) {
                        target = enemiesInVisionRange[i];
                        targetDistance = distanceToEnemy;
                        targetValue = enemyValue;
                    }
                }

                MapLocation toAttack = target.location;
                Communication.addEnemyArconLocation(Communication.convertMapLocationToInt(target.getLocation()),
                        rc);

                MapLocation[] surroundings = rc.getAllLocationsWithinRadiusSquared(target.location,
                        actionRadius);
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
                if (rc.canAttack(toAttack)) {
                    rc.attack(toAttack);
                }
            } else {
                if (closestEnemyArcon != 0) {
                    //dir = Pathfinding.basicBug(rc, closestEnemyArconLocation);
                    Direction dir = Pathfinding.greedyPathfinding(rc, closestEnemyArconLocation);
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                        //rc.setIndicatorString("MOVINGTOARCON");
                    }
                } else {
                    int closestEnemy = getClosestEnemy(rc);
                    if (closestEnemy != 0) {
                        MapLocation closestEnemyLocation = Communication.convertIntToMapLocation(closestEnemy);
                        Direction dir = Pathfinding.greedyPathfinding(rc, closestEnemyLocation);
                        if (rc.canMove(dir)) {
                            rc.move(dir);
                        }
                    } else {
                        Direction dir = Pathfinding.wander(rc);
                        if (rc.canMove(dir)) {
                            rc.move(dir);
                            //rc.setIndicatorString("MOVINGRAND");
                        }
                    }
                }
            }
        }
    }

    static int getClosestEnemyArcon(RobotController rc) throws GameActionException {
        int[] enemyArconLocations = Communication.getEnemyArconLocations(rc);
        int closestArconLocation = 0;
        int distanceSquaredToClosest = Integer.MAX_VALUE;
        for (int i = 0; i < enemyArconLocations.length; i++) {
            if (enemyArconLocations[i] != 0) {
                MapLocation location = Communication.convertIntToMapLocation(enemyArconLocations[i]);
                if (rc.getLocation().distanceSquaredTo(location) < distanceSquaredToClosest) {
                    closestArconLocation = enemyArconLocations[i];
                    distanceSquaredToClosest = rc.getLocation().distanceSquaredTo(location);
                }
            }
        }
        return closestArconLocation;
    }

    static int getClosestEnemy(RobotController rc) throws GameActionException {
        int[] enemyLocations = Communication.getEnemyLocations(rc);
        int closestEnemyLocation = 0;
        int distanceSquaredToClosest = Integer.MAX_VALUE;
        for (int i = 0; i < enemyLocations.length; i++) {
            if (enemyLocations[i] != 0) {
                MapLocation location = Communication.convertIntToMapLocation(enemyLocations[i]);
                if (rc.getLocation().distanceSquaredTo(location) < distanceSquaredToClosest) {
                    closestEnemyLocation = enemyLocations[i];
                    distanceSquaredToClosest = rc.getLocation().distanceSquaredTo(location);
                }
            }
        }
        return closestEnemyLocation;
    }

    static void swarmArcon(RobotController rc, MapLocation location) throws GameActionException {
        if (!(rc.getLocation().distanceSquaredTo(location) <= 2)) {
            Direction dir = Pathfinding.greedyPathfinding(rc, location);
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }
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
