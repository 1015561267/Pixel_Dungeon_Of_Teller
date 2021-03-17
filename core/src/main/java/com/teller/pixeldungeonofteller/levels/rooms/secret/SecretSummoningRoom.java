package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.traps.SummoningTrap;
import com.watabou.utils.Point;

public class SecretSummoningRoom extends SecretRoom {

    //minimum of 3x3 traps, max of 6x6 traps

    @Override
    public int maxWidth() {
        return 8;
    }

    @Override
    public int maxHeight() {
        return 8;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.SECRET_TRAP);

        Point center = center();
        level.drop(Generator.random(), level.pointToCell(center)).type = Heap.Type.SKELETON;

        for (Point p : getPoints()){
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.SECRET_TRAP){
                level.setTrap(new SummoningTrap().hide(), cell);
            }
        }

        entrance().set(Door.Type.HIDDEN);
    }

}
