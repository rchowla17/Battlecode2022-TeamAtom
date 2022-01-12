package atomAlpha;

import battlecode.common.*;

public strictfp class RobotPlayer {
    public static void run(RobotController rc) throws GameActionException {

        RobotType type = rc.getType();

        switch (type) {
            case ARCHON:
                Archon.init(rc);
                break;
            case MINER:
                Miner.init(rc);
                break;
            case SOLDIER:
                Soldier.init(rc);
                break;
            case LABORATORY:
                break;
            case WATCHTOWER:
                break;
            case BUILDER:
                Builder.init(rc);
                break;
            case SAGE:
                break;
        }

        while (true) {
            try {
                switch (type) {
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
                        Builder.runBuilder(rc);
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
