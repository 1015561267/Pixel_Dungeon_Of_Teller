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

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Statistics;
import com.teller.pixeldungeonofteller.actors.buffs.Appetizing;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Hunger;
import com.teller.pixeldungeonofteller.actors.buffs.Recharging;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.effects.SpellSprite;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRecharging;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

    public static final String AC_EAT = "EAT";
    private static final float TIME_TO_EAT = 3f;
    public float energy = Hunger.HUNGRY;
    public String message = Messages.get(this, "eat_msg");

    public int hornValue = 3;

    {
        stackable = true;
        image = ItemSpriteSheet.RATION;

        bones = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_EAT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_EAT)) {

            detach(hero.belongings.backpack);

            (hero.buff(Hunger.class)).satisfy(energy);
            GLog.i(message);

            if (hero.buff(Appetizing.class) != null) {
                hero.buff(Appetizing.class).exqute(hero, energy);
            }

            switch (hero.heroClass) {
                case WARRIOR:
                    if (hero.HP < hero.HT) {
                        hero.HP = Math.min(hero.HP + 5, hero.HT);
                        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    }
                    break;
                case MAGE:
                    //1 charge
                    Buff.affect(hero, Recharging.class, 4f);
                    ScrollOfRecharging.charge(hero);
                    break;
                case ROGUE:
                case HUNTRESS:
                    break;
            }

            hero.sprite.operate(hero.pos);
            hero.busy();
            SpellSprite.show(hero, SpellSprite.FOOD);
            Sample.INSTANCE.play(Assets.SND_EAT);

            hero.spend(TIME_TO_EAT);

            Statistics.foodEaten++;
            Badges.validateFoodEaten();

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int price() {
        return 10 * quantity;
    }
}
