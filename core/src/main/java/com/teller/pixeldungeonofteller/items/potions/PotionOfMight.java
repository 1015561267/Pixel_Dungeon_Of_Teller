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

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.utils.GLog;

public class PotionOfMight extends Potion {

    {
        initials = 6;

        bones = true;
    }

    @Override
    public void apply(Hero hero) {
        setKnown();

        hero.STR++;
        hero.HT += 5;
        hero.HP += 5;
        hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg_1"));
        GLog.p(Messages.get(this, "msg_2"));

        Badges.validateStrengthAttained();
    }

    @Override
    public int price() {
        return isKnown() ? 100 * quantity : super.price();
    }
}
