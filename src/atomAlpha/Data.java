package atomAlpha;

import battlecode.common.*;
import java.util.*;

public class Data {
    static final Direction[] directions = {
            Direction.NORTH,
            Direction.WEST,
            Direction.EAST,
            Direction.SOUTH,
            Direction.SOUTHEAST,
            Direction.SOUTHWEST,
            Direction.NORTHWEST,
            Direction.NORTHEAST,
    };
    static final Random rng = new Random(6147);

    static int randCounter = 0;
    static Direction randDirection = Direction.CENTER;
}
