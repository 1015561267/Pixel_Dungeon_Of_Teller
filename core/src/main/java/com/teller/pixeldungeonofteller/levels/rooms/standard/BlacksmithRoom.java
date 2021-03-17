package com.teller.pixeldungeonofteller.levels.rooms.standard;

import com.teller.pixeldungeonofteller.actors.mobs.npcs.Blacksmith;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.traps.FireTrap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class BlacksmithRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 6);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 6);
    }

    public void paint(Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.TRAP );
        Painter.fill( level, this, 2, Terrain.EMPTY_SP );

        for (int i=0; i < 2; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY_SP);
            level.drop(
                    Generator.random( Random.oneOf(
                            Generator.Category.ARMOR,
                            Generator.Category.WEAPON
                    ) ), pos );
        }

        for (Door door : connected.values()) {
            door.set( Door.Type.UNLOCKED );
            Painter.drawInside( level, this, door, 1, Terrain.EMPTY );
        }

        Blacksmith npc = new Blacksmith();
        do {
            npc.pos = level.pointToCell(random( 2 ));
        } while (level.heaps.get( npc.pos ) != null);
        level.mobs.add( npc );

        for(Point p : getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.TRAP){
                level.setTrap(new FireTrap().reveal(), cell);
            }
        }
    }
}
