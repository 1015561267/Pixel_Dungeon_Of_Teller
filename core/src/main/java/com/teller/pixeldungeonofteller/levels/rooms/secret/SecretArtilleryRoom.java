package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.rooms.Room;

public class SecretArtilleryRoom extends SecretRoom {

    @Override
    public void paint(Level level) {
        super.paint(level);

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        Painter.set(level, center(), Terrain.STATUE_SP);

        for (int i = 0; i < 4; i++){
            int itemPos;
            do{
                itemPos = level.pointToCell(random());
            } while ( level.map[itemPos] != Terrain.EMPTY_SP
                    || level.heaps.get(itemPos) != null);

            Item item;
            do{
                item = Generator.randomWeapon();
            } while (!(item instanceof MissileWeapon));

            level.drop(item, itemPos);
        }

        entrance().set(Room.Door.Type.HIDDEN);
    }
}
