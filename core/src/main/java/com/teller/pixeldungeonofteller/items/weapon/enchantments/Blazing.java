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
package com.teller.pixeldungeonofteller.items.weapon.enchantments;

import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Blazing extends Weapon.Enchantment {

    private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing(0xFF4400);

    @Override
    public Damage proc(Weapon weapon, Char attacker, Char defender, Damage damage) {
        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        int level = Math.max(0, weapon.level());
        if (Random.Int(level + 3) >= 2) {
            if (Random.Int(2) == 0) {
                Buff.affect(defender, Burning.class).reignite(defender);
            }
            defender.damage(new AbsoluteDamage(Random.Int(1, level + 2), this, defender), this);
            defender.sprite.emitter().burst(FlameParticle.FACTORY, level + 1);
        }
        return damage;
    }

    @Override
    public Glowing glowing() {
        return ORANGE;
    }
}
