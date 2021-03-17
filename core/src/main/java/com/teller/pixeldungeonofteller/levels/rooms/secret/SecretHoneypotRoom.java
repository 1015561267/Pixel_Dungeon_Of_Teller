package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.mobs.Bee;
import com.teller.pixeldungeonofteller.items.Bomb;
import com.teller.pixeldungeonofteller.items.Honeypot;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class SecretHoneypotRoom extends SecretRoom {

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill(level, this, 1, Terrain.EMPTY );

        Point brokenPotPos = center();

        brokenPotPos.x = (brokenPotPos.x + entrance().x) / 2;
        brokenPotPos.y = (brokenPotPos.y + entrance().y) / 2;

        Honeypot.ShatteredPot pot = new Honeypot.ShatteredPot();
        level.drop(pot, level.pointToCell(brokenPotPos));

        Bee bee = new Bee();
        bee.spawn( Dungeon.depth );
        bee.HP = bee.HT;
        bee.pos = level.pointToCell(brokenPotPos);
        level.mobs.add( bee );

        pot.setBee(bee);
        bee.setPotInfo(level.pointToCell(brokenPotPos), null);

        placeItem(new Honeypot(), level);

        placeItem( Random.Int(3) == 0 ? new Bomb.DoubleBomb() : new Bomb(), level);

        if (Random.Int(2) == 0){
            placeItem( new Bomb(), level);
        }

        entrance().set(Door.Type.HIDDEN);
    }

    private void placeItem(Item item, Level level){
        int itemPos;
        do {
            itemPos = level.pointToCell(random());
        } while (level.heaps.get(itemPos) != null);

        level.drop(item, itemPos);
    }
}
