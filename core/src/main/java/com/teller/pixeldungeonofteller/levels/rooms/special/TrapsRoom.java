package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLevitation;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.traps.BlazingTrap;
import com.teller.pixeldungeonofteller.levels.traps.ConfusionTrap;
import com.teller.pixeldungeonofteller.levels.traps.DisintegrationTrap;
import com.teller.pixeldungeonofteller.levels.traps.ExplosiveTrap;
import com.teller.pixeldungeonofteller.levels.traps.FlockTrap;
import com.teller.pixeldungeonofteller.levels.traps.GrimTrap;
import com.teller.pixeldungeonofteller.levels.traps.ParalyticTrap;
import com.teller.pixeldungeonofteller.levels.traps.SpearTrap;
import com.teller.pixeldungeonofteller.levels.traps.SummoningTrap;
import com.teller.pixeldungeonofteller.levels.traps.TeleportationTrap;
import com.teller.pixeldungeonofteller.levels.traps.ToxicTrap;
import com.teller.pixeldungeonofteller.levels.traps.Trap;
import com.teller.pixeldungeonofteller.levels.traps.VenomTrap;
import com.teller.pixeldungeonofteller.levels.traps.WarpingTrap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class TrapsRoom extends SpecialRoom {

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );

        Class<? extends Trap> trapClass;
        switch (Random.Int(5)){
            case 0: default:
                trapClass = SpearTrap.class;
                break;
            case 1:
                trapClass = !Dungeon.bossLevel(Dungeon.depth + 1)? null : SummoningTrap.class;
                break;
            case 2: case 3: case 4:
                trapClass = Random.oneOf(levelTraps[Dungeon.depth/5]);
                break;
        }

        if (trapClass == null){
            Painter.fill(level, this, 1, Terrain.CHASM);
        } else {
            Painter.fill(level, this, 1, Terrain.TRAP);
        }

        Door door = entrance();
        door.set( Door.Type.REGULAR );

        int lastRow = level.map[left + 1 + (top + 1) * level.width()] == Terrain.CHASM ? Terrain.CHASM : Terrain.EMPTY;

        int x = -1;
        int y = -1;
        if (door.x == left) {
            x = right - 1;
            y = top + height() / 2;
            Painter.fill( level, x, top + 1, 1, height() - 2 , lastRow );
        } else if (door.x == right) {
            x = left + 1;
            y = top + height() / 2;
            Painter.fill( level, x, top + 1, 1, height() - 2 , lastRow );
        } else if (door.y == top) {
            x = left + width() / 2;
            y = bottom - 1;
            Painter.fill( level, left + 1, y, width() - 2, 1 , lastRow );
        } else if (door.y == bottom) {
            x = left + width() / 2;
            y = top + 1;
            Painter.fill( level, left + 1, y, width() - 2, 1 , lastRow );
        }

        for(Point p : getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.TRAP){
                try {
                    level.setTrap(((Trap) trapClass.newInstance()).reveal(), cell);
                } catch (Exception e) {
                    PixelDungeonOfTeller.reportException(e);
                }
            }
        }

        int pos = x + y * level.width();
        if (Random.Int( 3 ) == 0) {
            if (lastRow == Terrain.CHASM) {
                Painter.set( level, pos, Terrain.EMPTY );
            }
            level.drop( prize( level ), pos ).type = Heap.Type.CHEST;
        } else {
            Painter.set( level, pos, Terrain.PEDESTAL );
            level.drop( prize( level ), pos );
        }

        level.addItemToSpawn( new PotionOfLevitation() );
    }

    private static Item prize(Level level ) {

        Item prize;

        if (Random.Int(3) != 0){
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

    @SuppressWarnings("unchecked")
    private static Class<?extends Trap>[][] levelTraps = new Class[][]{
            //sewers
            {ToxicTrap.class, TeleportationTrap.class, FlockTrap.class},
            //prison
            {ConfusionTrap.class, ExplosiveTrap.class, ParalyticTrap.class},
            //caves
            {BlazingTrap.class, VenomTrap.class, ExplosiveTrap.class},
            //city
            {WarpingTrap.class, VenomTrap.class, DisintegrationTrap.class},
            //halls, muahahahaha
            // WTF?IS THIS REALLY EVAN?--Teller
            {GrimTrap.class}
    };
}