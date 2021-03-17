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
package com.teller.pixeldungeonofteller.actors.mobs;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.sprites.ShieldedSprite;
import com.watabou.utils.Random;

public class Shielded extends Brute {

    {
        spriteClass = ShieldedSprite.class;

        HP = HT = 25;
        ARMOR = 55;
        defenseSkill = 20;


    }

    @Override
    public PhysicalDamage damageRoll() {

        PhysicalDamage dmg = new PhysicalDamage();
        if (enraged) {
            dmg.AddSlash(Random.NormalIntRange(2, 16));
            dmg.AddPuncture(Random.NormalIntRange(6, 36));
            dmg.AddImpact(Random.NormalIntRange(6, 12));
        } else {
            dmg.AddSlash(Random.NormalIntRange(1, 8));
            dmg.AddPuncture(Random.NormalIntRange(3, 18));
            dmg.AddImpact(Random.NormalIntRange(3, 6));
        }
        return dmg;
    }


    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Badges.validateRare(this);
    }
}
