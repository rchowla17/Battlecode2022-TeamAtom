package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Archon {
    static Queue<RobotType> startSpawnOrder = new LinkedList<RobotType>();
    static int startSpawn = 0;

    static ArrayList<RobotType> spawnOrder = new ArrayList<RobotType>();
    static int spawnOrderCounter = 0;

    static void runArchon(RobotController rc) throws GameActionException {
        int[] locations = Communication.getEnemyArconLocations(rc);
        String indicatorString = "";

        int random = (int) (Math.random() * 2);
        rc.writeSharedArray(62, random);

        for (int i = 0; i < locations.length; i++) {
            indicatorString += locations[i] + ",";
        }
        rc.setIndicatorString(indicatorString);

        while (startSpawn <= 9) {
            gameStartSequence(rc);
        }

        Communication.setCommArrayIndexToZero(rc, 63);
        normalSpawnSequence(rc);
    }

    public static void normalSpawnSequence(RobotController rc) throws GameActionException {
        RobotType spawn = spawnOrder.get(spawnOrderCounter % spawnOrder.size());
        Direction spawnDir = openSpawnLocation(rc, spawn);

        switch (spawn) {
            case SOLDIER:
                if (rc.canBuildRobot(RobotType.SOLDIER, spawnDir)) {
                    rc.buildRobot(RobotType.SOLDIER, spawnDir);
                    spawnOrderCounter++;
                }
                break;
            case MINER:
                if (rc.canBuildRobot(RobotType.MINER, spawnDir)) {
                    rc.buildRobot(RobotType.MINER, spawnDir);
                    spawnOrderCounter++;
                }
                break;
        }
    }

    //initial starting logic
    public static void gameStartSequence(RobotController rc) throws GameActionException {
        switch (startSpawn) {
            case 0:
                if (rc.canBuildRobot(RobotType.MINER, Direction.NORTHEAST)) {
                    rc.buildRobot(RobotType.MINER, Direction.NORTHEAST);
                    startSpawn++;
                }
                break;
            case 1:
                if (rc.canBuildRobot(RobotType.MINER, Direction.NORTHWEST)) {
                    rc.buildRobot(RobotType.MINER, Direction.NORTHWEST);
                    startSpawn++;
                }
                break;
            case 2:
                if (rc.canBuildRobot(RobotType.MINER, Direction.SOUTHEAST)) {
                    rc.buildRobot(RobotType.MINER, Direction.SOUTHEAST);
                    startSpawn++;
                }
                break;
            case 3:
                if (rc.canBuildRobot(RobotType.MINER, Direction.SOUTHWEST)) {
                    rc.buildRobot(RobotType.MINER, Direction.SOUTHWEST);
                    startSpawn++;
                }
                break;
            case 4:
                if (rc.canBuildRobot(RobotType.BUILDER, Direction.NORTH)) {
                    rc.buildRobot(RobotType.BUILDER, Direction.NORTH);
                    startSpawn++;
                }
                break;
            case 5:
                if (rc.canBuildRobot(RobotType.MINER, Direction.WEST)) {
                    rc.buildRobot(RobotType.MINER, Direction.WEST);
                    startSpawn++;
                }
                break;
            case 6:
                if (rc.canBuildRobot(RobotType.BUILDER, Direction.SOUTH)) {
                    rc.buildRobot(RobotType.BUILDER, Direction.SOUTH);
                    startSpawn++;
                }
                break;
            case 7:
                if (rc.canBuildRobot(RobotType.MINER, Direction.EAST)) {
                    rc.buildRobot(RobotType.MINER, Direction.EAST);
                    startSpawn++;
                }
                break;
            case 8:
                if (rc.canBuildRobot(RobotType.BUILDER, Direction.WEST)) {
                    rc.buildRobot(RobotType.BUILDER, Direction.WEST);
                    startSpawn++;
                }
                break;
            case 9:
                if (rc.canBuildRobot(RobotType.BUILDER, Direction.EAST)) {
                    rc.buildRobot(RobotType.BUILDER, Direction.EAST);
                    startSpawn++;
                }
                break;
        }
    }

    //unit initilization 
    public static void init() {
        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.SOLDIER);
        spawnOrder.add(RobotType.MINER);
    }

    //returns open spawn direction
    public static Direction openSpawnLocation(RobotController rc, RobotType type) throws GameActionException {
        for (Direction dir : Data.directions) {
            if (rc.canBuildRobot(type, dir)) {
                return dir;
            }
        }
        return Direction.CENTER;
    }
}
