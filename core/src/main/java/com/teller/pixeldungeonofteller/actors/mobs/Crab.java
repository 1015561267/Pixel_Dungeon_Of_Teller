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

import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.items.food.MysteryMeat;
import com.teller.pixeldungeonofteller.sprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab extends Mob {

    {
        spriteClass = CrabSprite.class;

        HP = HT = 15;
        ARMOR = 7;
        SlashThreshold=3;
        SHLD = 0;
        MAXSHLD = 0;

        defenseSkill = 5;
        baseSpeed = 2f;

        EXP = 4;
        maxLvl = 9;

        loot = new MysteryMeat();
        lootChance = 0.167f;
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(1, 4));
        dmg.AddImpact(Random.NormalIntRange(1, 4));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }
}
