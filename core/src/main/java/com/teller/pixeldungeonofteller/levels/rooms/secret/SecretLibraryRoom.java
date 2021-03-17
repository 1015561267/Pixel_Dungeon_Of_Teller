package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfIdentify;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfLullaby;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicMapping;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMirrorImage;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfPsionicBlast;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRage;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRecharging;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRemoveCurse;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfTeleportation;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfTerror;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Random;

import java.util.HashMap;

public class SecretLibraryRoom extends SecretRoom {

    @Override
    public int minWidth() {
        return Math.max(7, super.minWidth());
    }

    @Override
    public int minHeight() {
        return Math.max(7, super.minHeight());
    }

    private static HashMap<Class<? extends Scroll>, Float> scrollChances = new HashMap<>();
    static{
        scrollChances.put( ScrollOfIdentify.class,      1f );
        scrollChances.put( ScrollOfTeleportation.class, 1f );
        scrollChances.put( ScrollOfRemoveCurse.class,   3f );
        scrollChances.put( ScrollOfRecharging.class,    1f );
        scrollChances.put( ScrollOfMagicMapping.class,  3f );
        scrollChances.put( ScrollOfRage.class,          1f );
        scrollChances.put( ScrollOfTerror.class,        2f );
        scrollChances.put( ScrollOfLullaby.class,       2f );
        scrollChances.put( ScrollOfPsionicBlast.class,  5f );
        scrollChances.put( ScrollOfMirrorImage.class,   1f );
    }

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.BOOKSHELF );

        Painter.fillEllipse(level, this, 2, Terrain.EMPTY_SP);

        Door entrance = entrance();
        if (entrance.x == left || entrance.x == right){
            Painter.drawInside(level, this, entrance, (width() - 3) / 2, Terrain.EMPTY_SP);
        } else {
            Painter.drawInside(level, this, entrance, (height() - 3) / 2, Terrain.EMPTY_SP);
        }
        entrance.set( Door.Type.HIDDEN );

        int n = Random.IntRange( 2, 3 );
        HashMap<Class<? extends Scroll>, Float> chances = new HashMap<>(scrollChances);
        for (int i=0; i < n; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get( pos ) != null);

            try{
                Class<?extends Scroll> scrollCls = Random.chances(chances);
                chances.put(scrollCls, 0f);
                level.drop( scrollCls.newInstance(), pos );
            } catch (Exception e){
                PixelDungeonOfTeller.reportException(e);
            }

        }
    }

}

