package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.items.Weightstone;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLiquidFlame;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;

public class SecretRunestoneRoom extends SecretRoom {

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Door entrance = entrance();
        Point center = center();

        if (entrance.x == left || entrance.x == right){
            Painter.drawLine(level,
                    new Point(center.x, top+1),
                    new Point(center.x, bottom-1),
                    Terrain.BOOKSHELF);
            if (entrance.x == left) {
                Painter.fill(level, center.x+1, top+1, right-center.x-1, height()-2, Terrain.EMPTY_SP);
            } else {
                Painter.fill(level, left+1, top+1, center.x-left-1, height()-2, Terrain.EMPTY_SP);
            }
        } else {
            Painter.drawLine(level,
                    new Point(left+1, center.y),
                    new Point(right-1, center.y),
                    Terrain.BOOKSHELF);
            if (entrance.y == top) {
                Painter.fill(level, left+1, center.y+1, width()-2, bottom-center.y-1, Terrain.EMPTY_SP);
            } else {
                Painter.fill(level, left+1, top+1, width()-2, center.y-top-1, Terrain.EMPTY_SP);
            }
        }

        level.addItemToSpawn(new PotionOfLiquidFlame());

        int dropPos;
        do{
            dropPos = level.pointToCell(random());
        } while (level.map[dropPos] != Terrain.EMPTY_SP);
        level.drop( new Weightstone(), dropPos);

        entrance.set(Door.Type.HIDDEN);
    }

    @Override
    public boolean canPlaceWater(Point p) {
        return false;
    }

    @Override
    public boolean canPlaceGrass(Point p) {
        return false;
    }
}

