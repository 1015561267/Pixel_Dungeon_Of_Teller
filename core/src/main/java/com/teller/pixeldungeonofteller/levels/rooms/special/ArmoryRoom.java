package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.Bomb;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.keys.IronKey;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ArmoryRoom extends SpecialRoom {

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        Door entrance = entrance();
        Point statue = null;
        if (entrance.x == left) {
            statue = new Point( right-1, Random.Int( 2 ) == 0 ? top+1 : bottom-1 );
        } else if (entrance.x == right) {
            statue = new Point( left+1, Random.Int( 2 ) == 0 ? top+1 : bottom-1 );
        } else if (entrance.y == top) {
            statue = new Point( Random.Int( 2 ) == 0 ? left+1 : right-1, bottom-1 );
        } else if (entrance.y == bottom) {
            statue = new Point( Random.Int( 2 ) == 0 ? left+1 : right-1, top+1 );
        }
        if (statue != null) {
            Painter.set( level, statue, Terrain.STATUE );
        }

        int n = Random.IntRange( 1, 2 );
        for (int i=0; i < n; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY || level.heaps.get( pos ) != null);
            level.drop( prize( level ), pos );
        }

        entrance.set( Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey( Dungeon.depth ) );
    }

    private static Item prize(Level level ) {
        return Random.Int( 6 ) == 0 ?
                new Bomb().random() :
                Generator.random( Random.oneOf(
                        Generator.Category.ARMOR,
                        Generator.Category.WEAPON
                ) );
    }
}