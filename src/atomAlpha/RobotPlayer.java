package atomAlpha;

import battlecode.common.*;

public strictfp class RobotPlayer {

    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        rc.setIndicatorString("Hello world!");
        while (true) {
            Data.turnCount += 1;
            try {
                switch (rc.getType()) {
                    case ARCHON:
                        Archon.runArchon(rc);
                        break;
                    case MINER:
                        Miner.runMiner(rc);
                        break;
                    case SOLDIER:
                        Soldier.runSoldier(rc);
                        break;
                    case LABORATORY:
                        break;
                    case WATCHTOWER:
                        break;
                    case BUILDER:
                        break;
                    case SAGE:
                        break;
                }
            } catch (GameActionException e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            } finally {
                Clock.yield();
            }
        }
    }
}
