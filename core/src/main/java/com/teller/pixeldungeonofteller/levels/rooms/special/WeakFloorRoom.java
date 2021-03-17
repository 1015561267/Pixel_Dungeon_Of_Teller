package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.tiles.CustomTiledVisual;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class WeakFloorRoom extends SpecialRoom {

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.CHASM );

        Door door = entrance();
        door.set( Door.Type.REGULAR );

        if (door.x == left) {
            for (int i=top + 1; i < bottom; i++) {
                Painter.drawInside( level, this, new Point( left, i ), Random.IntRange( 1, width() - 3 ), Terrain.EMPTY_SP );
            }
        } else if (door.x == right) {
            for (int i=top + 1; i < bottom; i++) {
                Painter.drawInside( level, this, new Point( right, i ), Random.IntRange( 1, width() - 3 ), Terrain.EMPTY_SP );
            }
        } else if (door.y == top) {
            for (int i=left + 1; i < right; i++) {
                Painter.drawInside( level, this, new Point( i, top ), Random.IntRange( 1, height() - 3 ), Terrain.EMPTY_SP );
            }
        } else if (door.y == bottom) {
            for (int i=left + 1; i < right; i++) {
                Painter.drawInside( level, this, new Point( i, bottom ), Random.IntRange( 1, height() - 3 ), Terrain.EMPTY_SP );
            }
        }

        Point well = null;
        if (door.x == left) {
            well = new Point( right-1, Random.Int( 2 ) == 0 ? top + 2 : bottom - 1 );
        } else if (door.x == right) {
            well = new Point( left+1, Random.Int( 2 ) == 0 ? top + 2 : bottom - 1 );
        } else if (door.y == top) {
            well = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, bottom-1 );
        } else if (door.y == bottom) {
            well = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, top+2 );
        }
        Painter.set(level, well, Terrain.CHASM);
        CustomTiledVisual vis = new HiddenWell();
        vis.pos(well.x, well.y);
        level.customTiles.add(vis);
    }

    public static class HiddenWell extends CustomTiledVisual {

        public HiddenWell(){
            super(Assets.WEAK_FLOOR);
        }

        @Override
        public CustomTiledVisual create() {
            tileW = tileH = 1;
            map( new int[]{Dungeon.depth/5}, 1);
            return super.create();
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(this, "name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(this, "desc");
        }

    }
}