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
import com.teller.pixeldungeonofteller.actors.blobs.Blob;
import com.teller.pixeldungeonofteller.actors.blobs.StenchGas;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Ooze;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Ghost;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.FetidRatSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FetidRat extends Rat {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(StenchGas.class);
    }

    {
        spriteClass = FetidRatSprite.class;

        HP = HT = 25;
        SHLD = 0;
        MAXSHLD = SHLD;

        defenseSkill = 5;

        EXP = 4;

        state = WANDERING;

        properties.add(Property.MINIBOSS);
        properties.add(Property.DEMONIC);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Ooze.class);
        }
        return damage;
    }

    @Override
    public Damage defenseProc(Char enemy, Damage damage) {
        GameScene.add(Blob.seed(pos, 20, StenchGas.class));
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        Ghost.Quest.process();
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}