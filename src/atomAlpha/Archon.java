package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Archon {
    static int startSpawn = 0; //counter for initial spawn order

    static ArrayList<RobotType> spawnOrder = new ArrayList<RobotType>();
    static int spawnOrderCounter = 0;

    /* Archon Logic:
        First 9 things build will be miners in each of the different directions
        After that, every time we run Archon, we will alternate building Miners and Soldiers
        in the first available direction
    */
    static void runArchon(RobotController rc) throws GameActionException {
        //allows for differing random numbers across instances on the same turn
        int random = (int) (Math.random() * 2);
        rc.writeSharedArray(62, random);
        // random = (int) (Math.random() * 8);
        // rc.writeSharedArray(61, random);
        random = (int) (Math.random() * 3);
        rc.writeSharedArray(61, random);

        //System.out.println("Miners" + UnitCounter.getMiners(rc) + "Soldiers" + UnitCounter.getSoldiers(rc));
        UnitCounter.reset(rc);

        //initial spawn logic
        while (startSpawn <= 7) {
            gameStartSequence(rc);
        }

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
                if (rc.canBuildRobot(RobotType.MINER, Direction.NORTH)) {
                    rc.buildRobot(RobotType.MINER, Direction.NORTH);
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
                if (rc.canBuildRobot(RobotType.MINER, Direction.SOUTH)) {
                    rc.buildRobot(RobotType.MINER, Direction.SOUTH);
                    startSpawn++;
                }
                break;
            case 7:
                if (rc.canBuildRobot(RobotType.MINER, Direction.EAST)) {
                    rc.buildRobot(RobotType.MINER, Direction.EAST);
                    startSpawn++;
                }
                break;
        }
    }

    //unit initilization 
    public static void init() {
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
