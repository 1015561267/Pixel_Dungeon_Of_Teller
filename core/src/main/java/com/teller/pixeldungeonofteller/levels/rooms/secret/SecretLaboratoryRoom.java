package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.blobs.Alchemy;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.potions.PotionOfExperience;
import com.teller.pixeldungeonofteller.items.potions.PotionOfFrost;
import com.teller.pixeldungeonofteller.items.potions.PotionOfHealing;
import com.teller.pixeldungeonofteller.items.potions.PotionOfInvisibility;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLevitation;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLiquidFlame;
import com.teller.pixeldungeonofteller.items.potions.PotionOfMindVision;
import com.teller.pixeldungeonofteller.items.potions.PotionOfParalyticGas;
import com.teller.pixeldungeonofteller.items.potions.PotionOfPurity;
import com.teller.pixeldungeonofteller.items.potions.PotionOfToxicGas;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.HashMap;

public class SecretLaboratoryRoom extends SecretRoom {

    private static HashMap<Class<? extends Potion>, Float> potionChances = new HashMap<>();
    static{
        potionChances.put(PotionOfHealing.class,        2f);
        potionChances.put(PotionOfExperience.class,     5f);
        potionChances.put(PotionOfToxicGas.class,       1f);
        potionChances.put(PotionOfParalyticGas.class,   3f);
        potionChances.put(PotionOfLiquidFlame.class,    1f);
        potionChances.put(PotionOfLevitation.class,     1f);
        potionChances.put(PotionOfMindVision.class,     3f);
        potionChances.put(PotionOfPurity.class,         2f);
        potionChances.put(PotionOfInvisibility.class,   1f);
        potionChances.put(PotionOfFrost.class,          1f);
    }

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        entrance().set( Door.Type.HIDDEN );

        Point pot = center();
        Painter.set( level, pot, Terrain.ALCHEMY );

        Alchemy alchemy = new Alchemy();
        alchemy.seed( level, pot.x + level.width() * pot.y, Random.IntRange(30, 60) );
        level.blobs.put( Alchemy.class, alchemy );

        int n = Random.IntRange( 2, 3 );
        HashMap<Class<? extends Potion>, Float> chances = new HashMap<>(potionChances);
        for (int i=0; i < n; i++) {
            int pos;
            do {
                pos = level.pointToCell(random());
            } while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get( pos ) != null);

            try{
                Class<?extends Potion> potionCls = Random.chances(chances);
                chances.put(potionCls, 0f);
                level.drop( potionCls.newInstance(), pos );
            } catch (Exception e){
                PixelDungeonOfTeller.reportException(e);
            }

        }

    }

}
