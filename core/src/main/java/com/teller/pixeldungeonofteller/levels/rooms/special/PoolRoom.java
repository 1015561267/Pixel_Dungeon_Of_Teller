package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.mobs.Piranha;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.potions.PotionOfInvisibility;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Random;

public class PoolRoom extends SpecialRoom {

    private static final int NPIRANHAS	= 3;

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.WATER );

        Door door = entrance();
        door.set( Door.Type.REGULAR );

        int x = -1;
        int y = -1;
        if (door.x == left) {

            x = right - 1;
            y = top + height() / 2;

        } else if (door.x == right) {

            x = left + 1;
            y = top + height() / 2;

        } else if (door.y == top) {

            x = left + width() / 2;
            y = bottom - 1;

        } else if (door.y == bottom) {

            x = left + width() / 2;
            y = top + 1;

        }

        int pos = x + y * level.width();
        level.drop( prize( level ), pos ).type =
                Random.Int( 3 ) == 0 ? Heap.Type.CHEST : Heap.Type.HEAP;
        Painter.set( level, pos, Terrain.PEDESTAL );

        level.addItemToSpawn( new PotionOfInvisibility() );

        for (int i=0; i < NPIRANHAS; i++) {
            Piranha piranha = new Piranha();
            do {
                piranha.pos = level.pointToCell(random());
            } while (level.map[piranha.pos] != Terrain.WATER|| level.findMob( piranha.pos ) != null);
            level.mobs.add( piranha );
        }
    }

    private static Item prize(Level level ) {

        Item prize;

        if (Random.Int(3) == 0){
            prize = level.findPrizeItem();
            if (prize != null)
                return prize;
        }

        //1 floor set higher in probability, never cursed
        do {
            if (Random.Int(2) == 0) {
                prize = Generator.randomWeapon((Dungeon.depth / 5) + 1);
            } else {
                prize = Generator.randomArmor((Dungeon.depth / 5) + 1);
            }
        } while (prize.cursed);

        //33% chance for an extra update.
        if (!(prize instanceof MissileWeapon) && Random.Int(3) == 0){
            prize.upgrade();
        }

        return prize;
    }
}

