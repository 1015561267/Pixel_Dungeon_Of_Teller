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
package com.teller.pixeldungeonofteller.items.armor.glyphs;

import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.effects.Pushing;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.wands.WandOfBlastWave;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Repulsion extends Armor.Glyph {

    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFFF);

    @Override
    public Damage proc(Armor armor, Char attacker, Char defender, Damage damage) {

        int level = Math.max(0, armor.level());

        if (Random.Int(level + 5) >= 4) {
            int oppositeHero = attacker.pos + (attacker.pos - defender.pos);
            //Ballistica trajectory = new Ballistica(attacker.pos, oppositeHero, Ballistica.MAGIC_BOLT);
            //WandOfBlastWave.throwChar(attacker, trajectory, 2);
            Pushing.knockback(attacker,attacker.pos,2);
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }
}
