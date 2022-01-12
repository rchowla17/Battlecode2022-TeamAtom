package sprint1;

import battlecode.common.*;
import java.util.*;

public class Sage {
    static void runSoldier(RobotController rc) throws GameActionException {
        // Try to attack someone
        int actionRadius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(actionRadius, opponent);
        RobotInfo target = null;
        int targetValue = Integer.MAX_VALUE; //sage = 1, soldier = 2, builder = 3, archon = 4, miner = 5
        boolean unitWithinRange = false;

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

                int enemyValue = Data.determineEnemyValue(enemies[i]);
                if (enemyValue == 1 || enemyValue == 2 || enemyValue == 3 || enemyValue == 5)
                    unitWithinRange = true;
            }
            if (unitWithinRange && rc.canEnvision(AnomalyType.CHARGE)) {
                rc.envision(AnomalyType.CHARGE);
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
                dir = Pathfinding.randomDir(rc);
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    rc.setIndicatorString("MOVINGRAND");
                    Data.randCounter++;
                }
            }
        }
    }
}
