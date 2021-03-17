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
package com.teller.pixeldungeonofteller.items.potions;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Cripple;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.actors.buffs.Weakness;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;

public class PotionOfHealing extends Potion {

    {
        initials = 2;

        bones = true;
    }

    public static void heal(Hero hero) {

        hero.HP = hero.HT;
        Buff.detach(hero, Poison.class);
        Buff.detach(hero, Cripple.class);
        Buff.detach(hero, Weakness.class);
        Buff.detach(hero, Bleeding.class);

        hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
    }

    public static void cure( Hero hero ) {
        Buff.detach( hero, Poison.class );
        Buff.detach( hero, Cripple.class );
        Buff.detach( hero, Weakness.class );
        Buff.detach( hero, Bleeding.class );

    }

    @Override
    public void apply(Hero hero) {
        setKnown();
        heal(Dungeon.hero);
        GLog.p(Messages.get(this, "heal"));
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
