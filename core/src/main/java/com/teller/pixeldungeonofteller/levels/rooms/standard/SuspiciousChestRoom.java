package com.teller.pixeldungeonofteller.levels.rooms.standard;

import com.teller.pixeldungeonofteller.items.Gold;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Random;

public class SuspiciousChestRoom extends EmptyRoom {

    @Override
    public int minWidth() {
        return Math.max(5, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(5, super.minHeight());
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        Item i = level.findPrizeItem();

        if ( i == null ){
            i = new Gold().random();
        }

        int center = level.pointToCell(center());

        Painter.set(level, center, Terrain.PEDESTAL);

        if (Random.Int(3) == 0) {
            level.drop(i, center).type = Heap.Type.MIMIC;
        } else {
            level.drop(i, center).type = Heap.Type.CHEST;
        }
    }
}
