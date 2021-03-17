package com.teller.pixeldungeonofteller.levels.rooms.standard;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.items.quest.CeremonialCandle;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.tiles.CustomTiledVisual;
import com.watabou.utils.Point;

public class RitualSiteRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 5);
    }

    public void paint( Level level ) {

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        RitualMarker vis = new RitualMarker();
        Point c = center();
        vis.pos(c.x - 1, c.y - 1);

        level.customTiles.add(vis);

        Painter.fill(level, c.x-1, c.y-1, 3, 3, Terrain.EMPTY_DECO);

        level.addItemToSpawn(new CeremonialCandle());
        level.addItemToSpawn(new CeremonialCandle());
        level.addItemToSpawn(new CeremonialCandle());
        level.addItemToSpawn(new CeremonialCandle());

        CeremonialCandle.ritualPos = c.x + (level.width() * c.y);
    }

    public static class RitualMarker extends CustomTiledVisual {

        public RitualMarker(){
            super( Assets.PRISON_QUEST );
        }

        @Override
        public CustomTiledVisual create() {
            tileH = tileW = 3;
            mapSimpleImage(0, 0);
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

