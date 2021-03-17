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
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Corruption;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.effects.Pushing;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.potions.PotionOfHealing;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.features.Door;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Swarm extends Mob {

    private static final float SPLIT_DELAY = 1f;
    private static final String GENERATION = "generation";
    int generation = 0;

    {
        spriteClass = SwarmSprite.class;

        HP = HT = 65;
        ARMOR = 0;
        SHLD = 0;
        SlashThreshold=0;
        MAXSHLD = SHLD;
        defenseSkill = 5;

        EXP = 3;
        maxLvl = 9;

        flying = true;

        loot = new PotionOfHealing();
        lootChance = 0.1667f; //by default, see die()
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(GENERATION, generation);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        generation = bundle.getInt(GENERATION);
        if (generation > 0) EXP = 0;
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(1, 4));
        dmg.AddImpact(Random.NormalIntRange(0, 3));
        return dmg;

    }

    @Override
    public Damage defenseProc(Char enemy, Damage damage) {
        if (HP >= damage.sum() + 2) {
            ArrayList<Integer> candidates = new ArrayList<>();
            boolean[] passable = Dungeon.level.passable;
            int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
            for (int n : neighbours) {
                if (passable[n] && Actor.findChar(n) == null) {
                    candidates.add(n);
                }
            }
            if (candidates.size() > 0) {
                Swarm clone = split();
                clone.HP = (HP - (int) damage.sum()) / 2;
                clone.pos = Random.element(candidates);
                clone.state = clone.HUNTING;
                if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
                    Door.enter(clone.pos);
                }
                GameScene.add(clone, SPLIT_DELAY);
                Actor.addDelayed(new Pushing(clone, pos, clone.pos), -1);
                HP -= clone.HP;
            }
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public int attackSkill(Char target) {
        return 10;
    }

    private Swarm split() {
        Swarm clone = new Swarm();
        clone.generation = generation + 1;
        clone.EXP = 0;
        if (buff(Burning.class) != null) {
            Buff.affect(clone, Burning.class).reignite(clone);
        }
        if (buff(Poison.class) != null) {
            Buff.affect(clone, Poison.class).set(2);
        }
        if (buff(Corruption.class) != null) {
            Buff.affect(clone, Corruption.class);
        }
        return clone;
    }

    @Override
    public void die(Object cause) {
        //sets drop chance
        lootChance = 1f / ((6 + 2 * Dungeon.limitedDrops.swarmHP.count) * (generation + 1));
        super.die(cause);
    }

    @Override
    protected Item createLoot() {
        Dungeon.limitedDrops.swarmHP.count++;
        return super.createLoot();
    }
}
