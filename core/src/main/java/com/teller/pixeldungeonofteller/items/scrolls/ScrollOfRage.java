/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.teller.pixeldungeonofteller.items.scrolls;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Amok;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.mobs.Mimic;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRage extends Scroll {

    {
        initials = 6;
    }

    @Override
    protected void doRead() {

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            mob.beckon(curUser.pos);
            if (Dungeon.hero.fieldOfView[mob.pos]) {
                Buff.prolong(mob, Amok.class, 5f);
            }
        }

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.MIMIC) {
                Mimic m = Mimic.spawnAt(heap.pos, heap.items);
                if (m != null) {
                    m.beckon(curUser.pos);
                    heap.destroy();
                }
            }
        }

        GLog.w(Messages.get(this, "roar"));
        setKnown();

        curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);
        Invisibility.dispel();

        readAnimation();

        Dungeon.hero.buff(Noise.class).readRage();
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
