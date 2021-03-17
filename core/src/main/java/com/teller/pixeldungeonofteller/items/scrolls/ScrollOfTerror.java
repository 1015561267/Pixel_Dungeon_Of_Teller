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
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.effects.Flare;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfTerror extends Scroll {

    {
        initials = 10;
    }

    @Override
    protected void doRead() {

        new Flare(5, 32).color(0xFF0000, true).show(curUser.sprite, 2f);
        Sample.INSTANCE.play(Assets.SND_READ);
        Invisibility.dispel();

        int count = 0;
        Mob affected = null;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Dungeon.hero.fieldOfView[mob.pos]) {
                Buff.affect(mob, Terror.class, Terror.DURATION).object = curUser.id();

                if (mob.buff(Terror.class) != null) {
                    count++;
                    affected = mob;
                }
            }
        }

        switch (count) {
            case 0:
                GLog.i(Messages.get(this, "none"));
                break;
            case 1:
                GLog.i(Messages.get(this, "one", affected.name));
                break;
            default:
                GLog.i(Messages.get(this, "many"));
        }
        setKnown();

        readAnimation();
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
