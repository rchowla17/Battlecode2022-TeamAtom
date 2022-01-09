package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Soldier {
    static boolean attacker = false;

    static void runSoldier(RobotController rc) throws GameActionException {
        int actionRadius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(actionRadius, opponent);
        RobotInfo target = null;
        int targetHealth = Integer.MAX_VALUE;
        int targetValue = Integer.MAX_VALUE; //sage = 1, soldier = 2, builder = 3, archon = 4, miner = 5

        UnitCounter.addSoldier(rc);

        int closestEnemyArcon = getClosestEnemyArcon(rc);
        MapLocation closestEnemyArconLocation = null;
        if (closestEnemyArcon != 0) {
            String locationS = Integer.toString(closestEnemyArcon);
            int x = 0, y = 0;
            if (locationS.length() == 3) {
                x = Integer.parseInt(locationS.substring(0, 1));
                y = Integer.parseInt(locationS.substring(1));
            } else if (locationS.length() == 4) {
                x = Integer.parseInt(locationS.substring(0, 2));
                y = Integer.parseInt(locationS.substring(2));
            }
            closestEnemyArconLocation = new MapLocation(x, y);
            if (rc.canSenseLocation(closestEnemyArconLocation)) {
                if (rc.senseRobotAtLocation(closestEnemyArconLocation) == null) {
                    Communication.removeEnemyArconLocation(closestEnemyArcon, rc);
                }
            }
        }

        // Try to attack someone
        if (enemies.length > 0) {
            for (int i = 0; i < enemies.length; i++) {
                RobotInfo enemy = enemies[i];
                if (enemy.getType().equals(RobotType.ARCHON)) {
                    String x = String.format("%02d", enemy.getLocation().x);
                    String y = String.format("%02d", enemy.getLocation().y);
                    String locationS = x + y;
                    //rc.setIndicatorString(locationS);
                    Communication.addEnemyArconLocation(Integer.parseInt(locationS), rc);
                    swarmArcon(rc, enemy.getLocation());
                }

                int enemyValue = Data.determineEnemyValue(enemies[i]);
                if (enemyValue <= targetValue && enemies[i].health < targetHealth) {
                    target = enemies[i];
                    targetHealth = enemies[i].health;
                    targetValue = enemyValue;
                }
            }
            MapLocation toAttack = target.location;
            if (rc.canAttack(toAttack)) {
                rc.attack(toAttack);
            }
        } else {
            int visionRadius = rc.getType().visionRadiusSquared;
            enemies = rc.senseNearbyRobots(visionRadius, opponent);
            int targetDistance = Integer.MAX_VALUE;

            if (enemies.length > 0) {
                for (int i = 0; i < enemies.length; i++) {
                    RobotInfo enemy = enemies[i];
                    if (enemy.getType().equals(RobotType.ARCHON)) {
                        String x = String.format("%02d", enemy.getLocation().x);
                        String y = String.format("%02d", enemy.getLocation().y);
                        String locationS = x + y;
                        //rc.setIndicatorString(locationS);
                        Communication.addEnemyArconLocation(Integer.parseInt(locationS), rc);
                    }

                    int enemyValue = Data.determineEnemyValue(enemies[i]);
                    int distanceToEnemy = rc.getLocation().distanceSquaredTo(enemies[i].getLocation());
                    if (enemyValue <= targetValue && distanceToEnemy < targetDistance) {
                        target = enemies[i];
                        targetDistance = distanceToEnemy;
                        targetValue = enemyValue;
                    }
                }
                MapLocation toAttack = target.location;
                Direction dir = Pathfinding.basicBug(rc, toAttack);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                }
            } else {
                Direction dir = null;
                if (attacker && closestEnemyArcon != 0) {
                    dir = Pathfinding.basicBug(rc, closestEnemyArconLocation);
                    if (rc.canMove(dir)) {
                        rc.move(dir);
                        //rc.setIndicatorString("MOVINGTOARCON");
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
    }

    static int getClosestEnemyArcon(RobotController rc) throws GameActionException {
        int[] enemyArconLocations = Communication.getEnemyArconLocations(rc);
        int closestArconLocation = 0;
        int distanceSquaredToClosest = Integer.MAX_VALUE;
        for (int i = 0; i < enemyArconLocations.length; i++) {
            if (!(enemyArconLocations[i] == 0)) {
                String locationS = Integer.toString(enemyArconLocations[i]);
                int x = 0, y = 0;
                if (locationS.length() == 3) {
                    x = Integer.parseInt(locationS.substring(0, 1));
                    y = Integer.parseInt(locationS.substring(1));
                } else if (locationS.length() == 4) {
                    x = Integer.parseInt(locationS.substring(0, 2));
                    y = Integer.parseInt(locationS.substring(2));
                }
                MapLocation location = new MapLocation(x, y);
                if (rc.getLocation().distanceSquaredTo(location) < distanceSquaredToClosest) {
                    closestArconLocation = enemyArconLocations[i];
                    distanceSquaredToClosest = rc.getLocation().distanceSquaredTo(location);
                }
            }
        }
        return closestArconLocation;
    }

    static void swarmArcon(RobotController rc, MapLocation location) throws GameActionException {
        if (!(rc.getLocation().distanceSquaredTo(location) <= 2)) {
            Direction dir = Pathfinding.basicBug(rc, location);
            if (rc.canMove(dir)) {
                rc.move(dir);
            }
        }
    }

    static void init(RobotController rc) throws GameActionException {
        int rand = rc.readSharedArray(62);
        if (rand == 0) {
            attacker = true;
        }
        RobotInfo[] robots = rc.senseNearbyRobots();
        for (int i = 0; i < robots.length; i++) {
            RobotInfo robot = robots[i];
            if (robot.getTeam().equals(rc.getTeam()) && rc.getType().equals(RobotType.ARCHON)) {
                Data.spawnBaseLocation = robot.getLocation();
            }
        }
    }
}
