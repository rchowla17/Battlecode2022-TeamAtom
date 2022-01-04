package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Archon {
    static Queue<RobotType> startSpawnOrder = new LinkedList<RobotType>();

    static void runArchon(RobotController rc) throws GameActionException {
        while (!startSpawnOrder.isEmpty()) {
            gameStartSequence(rc);
        }
    }

    //initial starting logic
    public static void gameStartSequence(RobotController rc) throws GameActionException {
        RobotType type = startSpawnOrder.peek();
        Direction dir = openSpawnLocation(rc, type);
        if (rc.canBuildRobot(type, dir)) {
            rc.buildRobot(startSpawnOrder.poll(), dir);
        }
    }

    //unit initilization 
    public static void init() {
        startSpawnOrder.add(RobotType.MINER);
        startSpawnOrder.add(RobotType.MINER);
        startSpawnOrder.add(RobotType.MINER);
        startSpawnOrder.add(RobotType.MINER);
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
