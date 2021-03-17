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
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.Blob;
import com.teller.pixeldungeonofteller.actors.blobs.Web;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.actors.buffs.Roots;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.items.food.MysteryMeat;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.SpinnerSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Spinner extends Mob {

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(Poison.class);
    }

    static {
        IMMUNITIES.add(Roots.class);
    }

    {
        spriteClass = SpinnerSprite.class;

        HP = HT = 80;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 0;
        MAXSHLD = 0;

        defenseSkill = 14;

        EXP = 9;
        maxLvl = 16;

        loot = new MysteryMeat();
        lootChance = 0.125f;

        FLEEING = new Fleeing();
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 14));
        dmg.AddPuncture(Random.NormalIntRange(1, 8));
        dmg.AddImpact(Random.NormalIntRange(1, 10));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 6);
    }

    @Override
    protected boolean act() {
        boolean result = super.act();

        if (state == FLEEING && buff(Terror.class) == null &&
                enemy != null && enemySeen && enemy.buff(Poison.class) == null) {
            state = HUNTING;
        }
        return result;
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(enemy, Poison.class).set(Random.Int(7, 9) * Poison.durationFactor(enemy));
            state = FLEEING;
        }
        return damage;
    }

    @Override
    public void move(int step) {
        if (state == FLEEING) {
            GameScene.add(Blob.seed(pos, Random.Int(5, 7), Web.class));
        }
        super.move(step);
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    private class Fleeing extends Mob.Fleeing {
        @Override
        protected void nowhereToRun() {
            if (buff(Terror.class) == null) {
                state = HUNTING;
            } else {
                super.nowhereToRun();
            }
        }
    }
}
