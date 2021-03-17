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
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.sprites.SeniorSprite;
import com.watabou.utils.Random;

public class Senior extends Monk {

    {
        spriteClass = SeniorSprite.class;
        HP = HT = 80;
        SHLD = 40;
        MAXSHLD = SHLD;
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 8));
        dmg.AddPuncture(Random.NormalIntRange(4, 12));
        dmg.AddImpact(Random.NormalIntRange(4, 16));
        return dmg;

    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (Random.Int(10) == 0) {
            Buff.prolong(enemy, Paralysis.class, 1.1f);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Badges.validateRare(this);
    }
}
