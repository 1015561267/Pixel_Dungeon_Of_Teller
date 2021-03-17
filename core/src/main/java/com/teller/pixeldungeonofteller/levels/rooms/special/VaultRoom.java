package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.keys.CrystalKey;
import com.teller.pixeldungeonofteller.items.keys.GoldenKey;
import com.teller.pixeldungeonofteller.items.keys.IronKey;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class VaultRoom extends SpecialRoom {

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );
        Painter.fill( level, this, 2, Terrain.EMPTY );

        int cx = (left + right) / 2;
        int cy = (top + bottom) / 2;
        int c = cx + cy * level.width();
        Random.shuffle(prizeClasses);

        Item i1, i2;
        do {
            i1 = prize( level );
            i2 = prize( level );
        } while (i1.getClass() == i2.getClass());
        level.drop( i1, c ).type = Heap.Type.CRYSTAL_CHEST;
        level.drop( i2, c + PathFinder.NEIGHBOURS8[Random.Int( 8 )]).type = Heap.Type.CRYSTAL_CHEST;
        level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );

        entrance().set( Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey( Dungeon.depth ) );
    }

    private Item prize( Level level ) {
        return Generator.random( prizeClasses.remove(0) );
    }

    private ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
            Arrays.asList(Generator.Category.WAND,
                    Generator.Category.RING,
                    Generator.Category.ARTIFACT));
    }