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

import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.CursedFlame;
import com.teller.pixeldungeonofteller.actors.buffs.Weakness;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Flare;
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.bags.Bag;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndBag;

public class ScrollOfRemoveCurse extends InventoryScroll {

    {
        initials = 8;
        mode = WndBag.Mode.UNIDED_OR_CURSED;
    }

    public static boolean uncurse(Hero hero, Item... items) {

        boolean procced = false;
        for (Item item : items) {
            if (item != null && item.cursed) {
                item.cursed = false;
                procced = true;
            }
            if (item instanceof Weapon) {
                Weapon w = (Weapon) item;
                if (w.hasCurseEnchant()) {
                    w.enchant(null);
                    w.cursed = false;
                    procced = true;
                }
            }
            if (item instanceof Armor) {
                Armor a = (Armor) item;
                if (a.hasCurseGlyph()) {
                    a.inscribe(null);
                    a.cursed = false;
                    procced = true;
                }
            }
            if (item instanceof Ring && item.level() <= 0) {
                item.upgrade(1 - item.level());
            }
            if (item instanceof Bag) {
                for (Item bagItem : ((Bag) item).items) {
                    if (bagItem != null && bagItem.cursed) {
                        bagItem.cursed = false;
                        procced = true;
                    }
                }
            }
        }

        if (procced) {
            hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
        }

        Buff.detach(hero, CursedFlame.class);

        return procced;
    }

    @Override
    protected void onItemSelected(Item item) {
        new Flare(6, 32).show(curUser.sprite, 2f);

        boolean procced = uncurse(curUser, item);

        Weakness.detach(curUser, Weakness.class);

        if (procced) {
            GLog.p(Messages.get(this, "cleansed"));
        } else {
            GLog.i(Messages.get(this, "not_cleansed"));
        }
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
