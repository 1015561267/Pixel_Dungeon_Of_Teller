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

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.wands.Wand;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndBag;

public class ScrollOfUpgrade extends InventoryScroll {

    {
        initials = 11;
        mode = WndBag.Mode.UPGRADEABLE;

        bones = true;
    }

    public static void upgrade(Hero hero) {
        hero.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
    }

    public static void weakenCurse(Hero hero) {
        GLog.p(Messages.get(ScrollOfUpgrade.class, "weaken_curse"));
        hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 5);
    }

    public static void removeCurse(Hero hero) {
        GLog.p(Messages.get(ScrollOfUpgrade.class, "remove_curse"));
        hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
    }

    @Override
    protected void onItemSelected(Item item) {

        upgrade(curUser);

        //logic for telling the user when item properties change from upgrades
        //...yes this is rather messy
        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            boolean wasCursed = w.cursed;
            boolean hadCursedEnchant = w.hasCurseEnchant();
            boolean hadGoodEnchant = w.hasGoodEnchant();

            w.upgrade();

            if (hadCursedEnchant && !w.hasCurseEnchant()) {
                removeCurse(Dungeon.hero);
            } else if (wasCursed && !w.cursed) {
                weakenCurse(Dungeon.hero);
            }
            if (hadGoodEnchant && !w.hasGoodEnchant()) {
                GLog.w(Messages.get(Weapon.class, "incompatible"));
            }

        } else if (item instanceof Armor) {
            Armor a = (Armor) item;
            boolean wasCursed = a.cursed;
            boolean hadCursedGlyph = a.hasCurseGlyph();
            boolean hadGoodGlyph = a.hasGoodGlyph();

            a.upgrade();

            if (hadCursedGlyph && !a.hasCurseGlyph()) {
                removeCurse(Dungeon.hero);
            } else if (wasCursed && !a.cursed) {
                weakenCurse(Dungeon.hero);
            }
            if (hadGoodGlyph && !a.hasGoodGlyph()) {
                GLog.w(Messages.get(Armor.class, "incompatible"));
            }

        } else if (item instanceof Wand) {
            boolean wasCursed = item.cursed;

            item.upgrade();

            if (wasCursed && !item.cursed) {
                removeCurse(Dungeon.hero);
            }

        } else if (item instanceof Ring) {
            boolean wasCursed = item.cursed;

            item.upgrade();

            if (wasCursed && !item.cursed) {
                if (item.level() < 1) {
                    weakenCurse(Dungeon.hero);
                } else {
                    removeCurse(Dungeon.hero);
                }
            }

        } else {
            item.upgrade();
        }

        Badges.validateItemLevelAquired(item);
    }

    @Override
    public int price() {
        return isKnown() ? 50 * quantity : super.price();
    }
}
