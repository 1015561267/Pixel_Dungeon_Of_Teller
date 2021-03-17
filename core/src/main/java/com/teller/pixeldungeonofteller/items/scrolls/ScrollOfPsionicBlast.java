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
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Blindness;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfPsionicBlast extends Scroll {

    {
        initials = 5;

        bones = true;
    }

    @Override
    protected void doRead() {

        GameScene.flash(0xFFFFFF);

        Sample.INSTANCE.play(Assets.SND_BLAST);
        Invisibility.dispel();

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.hero.fieldOfView[mob.pos]) {
                mob.damage(new AbsoluteDamage(mob.HT, this, mob), this);
            }
        }

        curUser.damage(new AbsoluteDamage(Math.max(curUser.HT / 5, curUser.HP / 2), this, curUser), this);
        Buff.prolong(curUser, Paralysis.class, Random.Int(4, 6));
        Buff.prolong(curUser, Blindness.class, Random.Int(6, 9));
        Dungeon.observe();

        setKnown();

        curUser.spendAndNext(TIME_TO_READ); //no animation here, the flash interrupts it anyway.

        if (!curUser.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(this, "ondeath"));
        }
    }

    @Override
    public int price() {
        return isKnown() ? 50 * quantity : super.price();
    }
}
