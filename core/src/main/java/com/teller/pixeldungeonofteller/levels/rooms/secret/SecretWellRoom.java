package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.actors.blobs.WaterOfAwareness;
import com.teller.pixeldungeonofteller.actors.blobs.WaterOfHealth;
import com.teller.pixeldungeonofteller.actors.blobs.WaterOfTransmutation;
import com.teller.pixeldungeonofteller.actors.blobs.WellWater;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class SecretWellRoom extends SecretRoom {

    private static final Class<?>[] WATERS =
            {WaterOfAwareness.class, WaterOfHealth.class, WaterOfTransmutation.class};

    @Override
    public boolean canConnect(Point p) {
        //refuses connections next to corners
        return super.canConnect(p) && ((p.x > left+1 && p.x < right-1) || (p.y > top+1 && p.y < bottom-1));
    }

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Point door = entrance();
        Point well;
        if (door.x == left){
            well = new Point(right-2, door.y);
        } else if (door.x == right){
            well = new Point(left+2, door.y);
        } else if (door.y == top){
            well = new Point(door.x, bottom-2);
        } else {
            well = new Point(door.x, top+2);
        }

        Painter.fill(level, well.x-1, well.y-1, 3, 3, Terrain.CHASM);
        Painter.drawLine(level, door, well, Terrain.EMPTY);

        Painter.set( level, well, Terrain.WELL );

        @SuppressWarnings("unchecked")
        Class<? extends WellWater> waterClass = (Class<? extends WellWater>) Random.element( WATERS );

        WellWater.seed(well.x + level.width() * well.y, 1, waterClass, level);

        entrance().set( Door.Type.HIDDEN );
    }
}
