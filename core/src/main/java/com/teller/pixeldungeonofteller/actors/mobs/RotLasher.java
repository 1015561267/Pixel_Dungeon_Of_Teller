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

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Cripple;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.sprites.RotLasherSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class RotLasher extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(ToxicGas.class);
    }

    {
        spriteClass = RotLasherSprite.class;

        HP = HT = 50;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 0;
        MAXSHLD = 0;
        defenseSkill = 0;

        EXP = 1;

        loot = Generator.Category.SEED;
        lootChance = 1f;

        state = WANDERING = new Waiting();

        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        if (enemy == null || !Dungeon.level.adjacent(pos, enemy.pos)) {
            HP = Math.min(HT, HP + 3);
        }
        return super.act();
    }

    @Override
    public void damage(Damage dmg, Object src) {
        if (src instanceof Burning) {
            destroy();
            sprite.die();
        } else {
            super.damage(dmg, src);
        }
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        Buff.affect(enemy, Cripple.class, 2f);
        return super.attackProc(enemy, damage);
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 5));
        dmg.AddPuncture(Random.NormalIntRange(2, 7));
        dmg.AddImpact(Random.NormalIntRange(1, 2));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 8);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    private class Waiting extends Mob.Wandering {
    }
}
