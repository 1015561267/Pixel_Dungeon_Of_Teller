package com.teller.pixeldungeonofteller.levels.painters;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.rooms.Room;
import com.teller.pixeldungeonofteller.tiles.DungeonTileSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CityPainter extends RegularPainter {

    @Override
    protected void decorate(Level level, ArrayList<Room> rooms) {

        int[] map = level.map;
        int w = level.width();
        int l = level.length();

        for (int i=0; i < l - w; i++) {

            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
                map[i] = Terrain.EMPTY_DECO;

            } else if (map[i] == Terrain.WALL
                    && !DungeonTileSheet.wallStitcheable(map[i + w])
                    && Random.Int( 22 - Dungeon.depth ) == 0) {
                map[i] = Terrain.WALL_DECO;
            }
        }

    }
}

