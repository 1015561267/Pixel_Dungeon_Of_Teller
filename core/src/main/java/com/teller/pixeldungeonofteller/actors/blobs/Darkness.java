package com.teller.pixeldungeonofteller.actors.blobs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Blindness;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.effects.BlobEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.watabou.utils.Random;

public class Darkness extends Blob {

    @Override
    protected void evolve() {
        super.evolve();
        Char ch;
        int cell;

        for (int i = area.left; i < area.right; i++) {
            for (int j = area.top; j < area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0 && (ch = Actor.findChar(cell)) != null) {
                    if (!ch.immunities().contains(this.getClass()))
                        Buff.prolong(ch, Blindness.class, 2);
                }
            }
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);

        emitter.pour(Speck.factory(Speck.DARKNESS), .25f);
    }

    @Override
    public String tileDesc()
    {
        return Messages.get(this, "desc");
    }
}
