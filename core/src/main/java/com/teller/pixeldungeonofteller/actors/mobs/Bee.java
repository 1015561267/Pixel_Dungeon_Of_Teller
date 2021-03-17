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
package com.teller.pixeldungeonofteller.actors.mobs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Amok;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.sprites.BeeSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Bee extends Mob {
    private static final String LEVEL = "level";
    private static final String POTPOS = "potpos";
    private static final String POTHOLDER = "potholder";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Poison.class);
        IMMUNITIES.add(Amok.class);
    }

    private int level;
    //-1 refers to a pot that has gone missing.
    private int potPos;
    //-1 for no owner
    private int potHolder;

    {
        spriteClass = BeeSprite.class;

        viewDistance = 4;

        EXP = 0;
        SHLD = 0;
        MAXSHLD = SHLD;

        flying = true;
        state = WANDERING;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(POTPOS, potPos);
        bundle.put(POTHOLDER, potHolder);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spawn(bundle.getInt(LEVEL));
        potPos = bundle.getInt(POTPOS);
        potHolder = bundle.getInt(POTHOLDER);
    }

    public void spawn(int level) {
        this.level = level;
        HT = 10 + level;
        ARMOR = level;
        SlashThreshold=2+level/2;
        SHLD = 0;
        MAXSHLD = SHLD;
        defenseSkill = 9 + level;
    }

    public void setPotInfo(int potPos, Char potHolder) {
        this.potPos = potPos;
        if (potHolder == null)
            this.potHolder = -1;
        else
            this.potHolder = potHolder.id();
    }

    @Override
    public int attackSkill(Char target) {
        return defenseSkill;
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddPuncture(Random.NormalIntRange(2, 2 + level));
        dmg.AddImpact(Random.NormalIntRange(3 + (int) Math.floor(level / 4), 2+level));
        return dmg;

    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (enemy instanceof Mob) {
            ((Mob) enemy).aggro(this);
        }
        return damage;
    }

    @Override
    protected Char chooseEnemy() {
        //if the pot is no longer present, target the hero
        if (potHolder == -1 && potPos == -1)
            return Dungeon.hero;

            //if something is holding the pot, target that
        else if (Actor.findById(potHolder) != null)
            return (Char) Actor.findById(potHolder);

            //if the pot is on the ground
        else {

            //if already targeting something, and that thing is still alive and near the pot, keeping targeting it.
            if (enemy != null && enemy.isAlive() && Dungeon.level.mobs.contains(enemy)
                    && fieldOfView[enemy.pos] && enemy.invisible == 0
                    && Dungeon.level.distance(enemy.pos, potPos) <= 3)
                return enemy;

            //find all mobs near the pot
            HashSet<Char> enemies = new HashSet<>();
            for (Mob mob : Dungeon.level.mobs)
                if (!(mob instanceof Bee) && Dungeon.level.distance(mob.pos, potPos) <= 3 && (mob.hostile || mob.ally))
                    enemies.add(mob);

            //pick one, if there are none, check if the hero is near the pot, go for them, otherwise go for nothing.
            if (enemies.size() > 0) return Random.element(enemies);
            else
                return (Dungeon.level.distance(Dungeon.hero.pos, potPos) <= 3) ? Dungeon.hero : null;
        }
    }

    @Override
    protected boolean getCloser(int target) {
        if (enemy != null && Actor.findById(potHolder) == enemy) {
            target = enemy.pos;
        } else if (potPos != -1 && (state == WANDERING || Dungeon.level.distance(target, potPos) > 3))
            this.target = target = potPos;
        return super.getCloser(target);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}