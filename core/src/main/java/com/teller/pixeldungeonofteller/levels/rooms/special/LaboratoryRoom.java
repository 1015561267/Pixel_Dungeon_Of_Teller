package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.blobs.Alchemy;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.keys.IronKey;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LaboratoryRoom extends SpecialRoom {

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        Door entrance = entrance();

        Point pot = null;
        if (entrance.x == left) {
            pot = new Point( right-1, Random.Int( 2 ) == 0 ? top + 1 : bottom - 1 );
        } else if (entrance.x == right) {
            pot = new Point( left+1, Random.Int( 2 ) == 0 ? top + 1 : bottom - 1 );
        } else if (entrance.y == top) {
            pot = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, bottom-1 );
        } else if (entrance.y == bottom) {
            pot = new Point( Random.Int( 2 ) == 0 ? left + 1 : right - 1, top+1 );
        }
        Painter.set( level, pot, Terrain.ALCHEMY );

        Alchemy alchemy = new Alchemy();
        alchemy.seed( level, pot.x + level.width() * pot.y, 1 );
        level.blobs.put( Alchemy.class, alchemy );

        int n = Random.IntRange( 2, 3 );
        for (int i=0; i < n; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (
                    level.map[pos] != Terrain.EMPTY_SP ||
                            level.heaps.get( pos ) != null);
            level.drop( prize( level ), pos );
        }

        entrance.set( Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey( Dungeon.depth ) );
    }

    private static Item prize(Level level ) {

        Item prize = level.findPrizeItem( Potion.class );
        if (prize == null)
            prize = Generator.random( Generator.Category.POTION );

        return prize;
    }
}

