package com.teller.pixeldungeonofteller.levels.rooms.connection;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class ConnectionRoom extends Room {

    @Override
    public int minWidth() { return 3; }
    public int maxWidth() { return 10; }

    @Override
    public int minHeight() { return 3; }
    public int maxHeight() { return 10; }

    @Override
    public int minConnections(int direction) {
        if (direction == ALL)   return 2;
        else                    return 0;
    }

    @Override
    public int maxConnections(int direction) {
        if (direction == ALL)   return 16;
        else                    return 4;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        //traps cannot appear in connection rooms on floor 1
        return super.canPlaceTrap(p) && Dungeon.depth > 1;
    }

    //FIXME this is a very messy way of handing variable connection rooms
    private static ArrayList<Class<?extends ConnectionRoom>> rooms = new ArrayList<>();
    static {
        rooms.add(TunnelRoom.class);
        rooms.add(BridgeRoom.class);

        rooms.add(PerimeterRoom.class);
        rooms.add(WalkwayRoom.class);

        rooms.add(RingBridgeRoom.class);
        rooms.add(MazeConnectionRoom.class);
    }

    private static float[][] chances = new float[27][];
    static {
        chances[1] =  new float[]{20, 1,    0, 2,       2, 1};
        chances[4] =  chances[3] = chances[2] = chances[1];
        chances[5] =  new float[]{18, 0,    0, 0,       7, 0};

        chances[6] =  new float[]{0, 0,     22, 3,      0, 0};
        chances[10] = chances[9] = chances[8] = chances[7] = chances[6];

        chances[11] = new float[]{12, 0,    0, 5,       5, 3};
        chances[15] = chances[14] = chances[13] = chances[12] = chances[11];

        chances[16] = new float[]{0, 0,     18, 3,      3, 1};
        chances[20] = chances[19] = chances[18] = chances[17] = chances[16];

        chances[21] = chances[5];

        chances[22] = new float[]{15, 4,    0, 2,       3, 2};
        chances[26] = chances[25] = chances[24] = chances[23] = chances[22];

    }

    public static ConnectionRoom createRoom(){
        try {
            return rooms.get(Random.chances(chances[Dungeon.depth])).newInstance();
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }
}

