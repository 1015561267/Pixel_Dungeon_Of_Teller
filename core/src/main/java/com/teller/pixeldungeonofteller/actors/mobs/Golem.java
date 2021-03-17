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
import com.teller.pixeldungeonofteller.actors.buffs.Amok;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Sleep;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Imp;
import com.teller.pixeldungeonofteller.sprites.GolemSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Golem extends Mob {

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
    }

    static {
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Sleep.class);
        IMMUNITIES.add(Bleeding.class);
    }

    {
        spriteClass = GolemSprite.class;

        HP = HT = 0;
        ARMOR = 65;
        SlashThreshold=20;
        SHLD = 45;
        MAXSHLD = 45;

        defenseSkill = 18;

        EXP = 12;
        maxLvl = 22;
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(1, 18));
        dmg.AddPuncture(Random.NormalIntRange(4, 10));
        dmg.AddImpact(Random.NormalIntRange(4, 12));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    protected float attackDelay() {
        return 1.5f;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 12);
    }

    @Override
    public void die(Object cause) {
        Imp.Quest.process(this);

        super.die(cause);
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    public boolean isAlive() {
        return SHLD > 0;
    }
}
