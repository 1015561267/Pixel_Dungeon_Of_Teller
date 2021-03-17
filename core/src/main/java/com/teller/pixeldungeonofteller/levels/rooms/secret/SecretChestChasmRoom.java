package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.keys.GoldenKey;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLevitation;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;

public class SecretChestChasmRoom extends SecretRoom {

    //width and height are controlled here so that this room always requires 2 levitation potions

    @Override
    public int minWidth() {
        return 8;
    }

    @Override
    public int maxWidth() {
        return 9;
    }

    @Override
    public int minHeight() {
        return 8;
    }

    @Override
    public int maxHeight() {
        return 9;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.CHASM);

        Point p = new Point(left+1, top+1);
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));

        p.x = right-1;
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));

        p.y = bottom-1;
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));

        p.x = left+1;
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));


        p = new Point(left+3, top+3);
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;

        p.x = right-3;
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;

        p.y = bottom-3;
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;

        p.x = left+3;
        Painter.set(level, p, Terrain.EMPTY_SP);
        level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;

        level.addItemToSpawn(new PotionOfLevitation());

        entrance().set(Door.Type.HIDDEN);
    }
}
