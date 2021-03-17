package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.items.Gold;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.traps.DisintegrationTrap;
import com.teller.pixeldungeonofteller.levels.traps.RockfallTrap;
import com.teller.pixeldungeonofteller.levels.traps.SpearTrap;
import com.teller.pixeldungeonofteller.levels.traps.Trap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class SecretHoardRoom extends SecretRoom {

    @Override
    public void paint(Level level) {
        super.paint(level);

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        Class<? extends Trap> trapClass;
        if (Random.Int(2) == 0){
            trapClass = RockfallTrap.class;
        } else if (Dungeon.depth >= 10){
            trapClass = DisintegrationTrap.class;
        } else {
            trapClass = SpearTrap.class;
        }

        int goldPos;
        //half of the internal space of the room
        int totalGold = ((width()-2)*(height()-2))/2;

        //no matter how much gold it drops, roughly equals 8 gold stacks.
        float goldRatio = 8 / (float)totalGold;
        for (int i = 0; i < totalGold; i++) {
            do {
                goldPos = level.pointToCell(random());
            } while (level.heaps.get(goldPos) != null);
            Item gold = new Gold().random();
            gold.quantity(Math.round(gold.quantity() * goldRatio));
            level.drop(gold, goldPos);
        }

        for (Point p : getPoints()){
            if (Random.Int(2) == 0 && level.map[level.pointToCell(p)] == Terrain.EMPTY){
                try {
                    level.setTrap(trapClass.newInstance().reveal(), level.pointToCell(p));
                    Painter.set(level, p, Terrain.TRAP);
                } catch (Exception e) {
                    PixelDungeonOfTeller.reportException(e);
                }
            }
        }

        entrance().set(Door.Type.HIDDEN);
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }
}

