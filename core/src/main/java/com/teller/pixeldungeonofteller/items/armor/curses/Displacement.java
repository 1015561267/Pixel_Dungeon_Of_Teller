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
package com.teller.pixeldungeonofteller.items.armor.curses;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfTeleportation;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Displacement extends Armor.Glyph {

    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing(0x000000);

    @Override
    public Damage proc(Armor armor, Char attacker, Char defender, Damage damage) {

        if (defender == Dungeon.hero && Random.Int(20) == 0) {
            ScrollOfTeleportation.teleportHero(Dungeon.hero);
            damage.cancel();
            return damage;
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public boolean curse() {
        return true;
    }
}
