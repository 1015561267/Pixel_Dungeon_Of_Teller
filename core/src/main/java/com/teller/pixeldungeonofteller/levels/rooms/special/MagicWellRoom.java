package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.blobs.WaterOfAwareness;
import com.teller.pixeldungeonofteller.actors.blobs.WaterOfHealth;
import com.teller.pixeldungeonofteller.actors.blobs.WaterOfTransmutation;
import com.teller.pixeldungeonofteller.actors.blobs.WellWater;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class MagicWellRoom extends SpecialRoom {

    private static final Class<?>[] WATERS =
            {WaterOfAwareness.class, WaterOfHealth.class, WaterOfTransmutation.class};

    public Class<?extends WellWater> overrideWater = null;

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        Point c = center();
        Painter.set( level, c.x, c.y, Terrain.WELL );

        @SuppressWarnings("unchecked")
        Class<? extends WellWater> waterClass =
                overrideWater != null ?
                        overrideWater :
                        (Class<? extends WellWater>) Random.element( WATERS );

        if (waterClass == WaterOfTransmutation.class) {
            SpecialRoom.disableGuaranteedWell();
        }

        WellWater.seed(c.x + level.width() * c.y, 1, waterClass, level);

        entrance().set( Door.Type.REGULAR );
    }
}

