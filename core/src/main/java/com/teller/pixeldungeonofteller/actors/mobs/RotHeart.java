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
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.blobs.Blob;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.plants.Rotberry;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.RotHeartSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class RotHeart extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Terror.class);
    }

    {
        spriteClass = RotHeartSprite.class;

        HP = HT = 100;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 0;
        MAXSHLD = 0;

        defenseSkill = 0;

        EXP = 4;

        state = PASSIVE;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    @Override
    public void damage(Damage dmg, Object src) {
        //TODO: when effect properties are done, change this to FIRE
        if (src instanceof Burning) {
            destroy();
            sprite.die();
        } else {
            super.damage(dmg, src);
        }
    }

    @Override
    public Damage defenseProc(Char enemy, Damage damage) {
        GameScene.add(Blob.seed(pos, 20, ToxicGas.class));
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])) {
            if (mob instanceof RotLasher) {
                mob.die(null);
            }
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.drop(new Rotberry.Seed(), pos).sprite.drop();
    }

    @Override
    public Damage damageRoll() {
        return new AbsoluteDamage(0, this, enemy);
    }

    @Override
    public int attackSkill(Char target) {
        return 0;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

}
