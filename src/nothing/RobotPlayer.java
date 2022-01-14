package nothing;

import battlecode.common.*;
import java.util.Random;

public strictfp class RobotPlayer {
    public static void run(RobotController rc) throws GameActionException {
        while (true) {
            try {
                switch (rc.getType()) {
                    case ARCHON:
                        runArchon(rc);
                        break;
                }
            } catch (GameActionException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                Clock.yield();
            }
        }
    }

    static void runArchon(RobotController rc) throws GameActionException {
    }
}
