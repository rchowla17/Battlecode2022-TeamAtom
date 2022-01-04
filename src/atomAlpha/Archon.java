package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Archon {
    static Queue<RobotType> startSpawnOrder = new LinkedList<RobotType>();
    static int startSpawn = 0;

    static void runArchon(RobotController rc) throws GameActionException {
        while (startSpawn <= 9) {
            gameStartSequence(rc);
        }
    }

    //initial starting logic
    public static void gameStartSequence(RobotController rc) throws GameActionException {
        switch (startSpawn) {
            case 0:
                if (rc.canBuildRobot(RobotType.MINER, Direction.NORTH)) {
                    rc.buildRobot(RobotType.MINER, Direction.NORTH);
                    startSpawn++;
                }
                break;
            case 1:
                if (rc.canBuildRobot(RobotType.MINER, Direction.EAST)) {
                    rc.buildRobot(RobotType.MINER, Direction.EAST);
                    startSpawn++;
                }
                break;
            case 2:
                if (rc.canBuildRobot(RobotType.MINER, Direction.SOUTH)) {
                    rc.buildRobot(RobotType.MINER, Direction.SOUTH);
                    startSpawn++;
                }
                break;
            case 3:
                if (rc.canBuildRobot(RobotType.MINER, Direction.WEST)) {
                    rc.buildRobot(RobotType.MINER, Direction.WEST);
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
