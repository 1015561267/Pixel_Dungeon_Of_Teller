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
package com.teller.pixeldungeonofteller.items.food;

import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Hunger;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.actors.buffs.Roots;
import com.teller.pixeldungeonofteller.actors.buffs.Slow;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Random;

public class MysteryMeat extends Food {

    {
        image = ItemSpriteSheet.MEAT;
        energy = Hunger.STARVING - Hunger.HUNGRY;
        hornValue = 1;
    }

    public static void effect(Hero hero) {
        switch (Random.Int(5)) {
            case 0:
                GLog.w(Messages.get(MysteryMeat.class, "hot"));
                Buff.affect(hero, Burning.class).reignite(hero);
                break;
            case 1:
                GLog.w(Messages.get(MysteryMeat.class, "legs"));
                Buff.prolong(hero, Roots.class, Paralysis.duration(hero));
                break;
            case 2:
                GLog.w(Messages.get(MysteryMeat.class, "not_well"));
                Buff.affect(hero, Poison.class).set(Poison.durationFactor(hero) * hero.HT / 5);
                break;
            case 3:
                GLog.w(Messages.get(MysteryMeat.class, "stuffed"));
                Buff.prolong(hero, Slow.class, Slow.duration(hero));
                break;
        }
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_EAT)) {
            effect(hero);
        }
    }

    public int price() {
        return 5 * quantity;
    }
}
