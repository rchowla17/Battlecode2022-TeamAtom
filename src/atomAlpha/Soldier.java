package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Soldier {
    static void runSoldier(RobotController rc) throws GameActionException {
        // Try to attack someone
        int actionRadius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(actionRadius, opponent);
        RobotInfo target = null;
        int targetHealth = Integer.MAX_VALUE;
        int targetValue = Integer.MAX_VALUE; //sage = 1, soldier = 2, builder = 3, archon = 4, miner = 5

        if (enemies.length > 0) {
            for (int i = 0; i < enemies.length; i++) {
                RobotInfo enemy = enemies[i];
                if (enemy.getType().equals(RobotType.ARCHON)) {
                    String x = String.format("%02d", enemy.getLocation().x);
                    String y = String.format("%02d", enemy.getLocation().y);
                    String locationS = x + y;
                    rc.setIndicatorString(locationS);
                    Communication.addEnemyArconLocation(Integer.parseInt(locationS), rc);
                }

                int enemyValue = determineEnemyValue(enemies[i]);
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
                        rc.setIndicatorString(locationS);
                        Communication.addEnemyArconLocation(Integer.parseInt(locationS), rc);
                    }

                    int enemyValue = determineEnemyValue(enemies[i]);
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
                dir = Pathfinding.randomDir(rc);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    rc.setIndicatorString("MOVINGRAND");
                    Data.randCounter++;
                }
            }
        }
    }

    static int determineEnemyValue(RobotInfo robot) {
        switch (robot.getType()) {
            case SAGE:
                return 1;
            case SOLDIER:
                return 2;
            case WATCHTOWER:
                return 3;
            case BUILDER:
                return 4;
            case ARCHON:
                return 5;
            case MINER:
                return 6;
            case LABORATORY:
                return 7;
        }
        return 0;
    }
}
