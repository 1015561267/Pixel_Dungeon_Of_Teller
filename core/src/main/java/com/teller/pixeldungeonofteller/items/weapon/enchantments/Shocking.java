/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.effects.Lightning;
import com.teller.pixeldungeonofteller.effects.particles.SparkParticle;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.traps.LightningTrap;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Shocking extends Weapon.Enchantment {

    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing(0xFFFFFF, 0.6f);
    private ArrayList<Char> affected = new ArrayList<>();
    private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

    @Override
    public Damage proc(Weapon weapon, Char attacker, Char defender, Damage damage) {
        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        int level = Math.max(0, weapon.level());

        if (Random.Int(level + 3) >= 2) {
            affected.clear();
            affected.add(attacker);
            arcs.clear();
            arcs.add(new Lightning.Arc(attacker.pos, defender.pos));
            MagicalDamage dmg = new MagicalDamage();


            hit(defender, Random.Int(1, (int) damage.sum() / 3));
            attacker.sprite.parent.add(new Lightning(arcs, null));
        }
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    private void hit(Char ch, int damage) {
        if (damage < 1) {
            return;
        }

        affected.add(ch);
        ch.damage(new AbsoluteDamage(Dungeon.level.water[ch.pos] && !ch.flying ? (damage * 2) : damage, this, ch), LightningTrap.LIGHTNING);

        ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
        ch.sprite.flash();

        HashSet<Char> ns = new HashSet<Char>();
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char n = Actor.findChar(ch.pos + PathFinder.NEIGHBOURS8[i]);
            if (n != null && !affected.contains(n)) {
                arcs.add(new Lightning.Arc(ch.pos, n.pos));
                hit(n, Random.Int(damage / 2, damage));
            }
        }
    }
}
