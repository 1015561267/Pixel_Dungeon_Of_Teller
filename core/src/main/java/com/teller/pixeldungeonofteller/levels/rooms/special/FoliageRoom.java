package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Challenges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.blobs.Foliage;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.plants.BlandfruitBush;
import com.teller.pixeldungeonofteller.plants.Sungrass;
import com.watabou.utils.Random;

public class FoliageRoom extends SpecialRoom {

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.HIGH_GRASS );
        Painter.fill( level, this, 2, Terrain.GRASS );

        entrance().set( Door.Type.REGULAR );

        if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
            if (Random.Int(2) == 0){
                level.plant(new Sungrass.Seed(), level.pointToCell(random()));
            }
        } else {
            int bushes = Random.Int(3);
            if (bushes == 0) {
                level.plant(new Sungrass.Seed(), level.pointToCell(random()));
            } else if (bushes == 1) {
                level.plant(new BlandfruitBush.Seed(), level.pointToCell(random()));
            } else if (Random.Int(5) == 0) {
                int plant1, plant2;
                plant1 = level.pointToCell(random());
                level.plant(new Sungrass.Seed(), plant1);
                do {
                    plant2 = level.pointToCell(random());
                } while (plant2 == plant1);
                level.plant(new BlandfruitBush.Seed(), plant2);
            }
        }

        Foliage light = (Foliage)level.blobs.get( Foliage.class );
        if (light == null) {
            light = new Foliage();
        }
        for (int i=top + 1; i < bottom; i++) {
            for (int j=left + 1; j < right; j++) {
                light.seed( level, j + level.width() * i, 1 );
            }
        }
        level.blobs.put( Foliage.class, light );
    }
}
