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

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.buffs.Berserk;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.hero.HeroSubClass;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Lucky extends Weapon.Enchantment {

    private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x00FF00);

    @Override
    public Damage proc(Weapon weapon, Char attacker, Char defender, Damage dmg) {

        int damage;
        int level = Math.max(0, weapon.level());
        if (Random.Int(100) < (55 + level)) {
            //int exStr = 0;
            //if (attacker == Dungeon.hero) exStr = Math.max(0, Dungeon.hero.STR() - weapon.STRReq());
            //damage = weapon.imbue.damageFactor(weapon.max()) + exStr - defender.drRoll();
            damage=2;
        } else {
            damage=0;
            //damage = weapon.imbue.damageFactor(weapon.min()) - defender.drRoll();
        }
        if (attacker == Dungeon.hero && Dungeon.hero.subClass == HeroSubClass.BERSERKER) {
            //damage = Buff.affect(Dungeon.hero, Berserk.class).damageFactor(damage);
        }
        dmg.multiplie(Math.max(0, damage));

        return dmg;
    }

    @Override
    public Glowing glowing() {
        return GREEN;
    }
}