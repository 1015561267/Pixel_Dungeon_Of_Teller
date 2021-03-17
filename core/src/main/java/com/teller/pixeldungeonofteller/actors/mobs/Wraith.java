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
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.WraithSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Wraith extends Mob {

    private static final float SPAWN_DELAY = 2f;
    private static final String LEVEL = "level";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Grim.class);
        IMMUNITIES.add(Terror.class);
    }

    private int level;

    {
        spriteClass = WraithSprite.class;

        HP = HT = 1;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 0;
        MAXSHLD = 0;


        EXP = 0;

        flying = true;

        properties.add(Property.UNDEAD);
    }

    public static void spawnAround(int pos) {
        for (int n : PathFinder.NEIGHBOURS4) {
            int cell = pos + n;
            if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
                spawnAt(cell);
            }
        }
    }

    public static Wraith spawnAt(int pos) {
        if (Dungeon.level.passable[pos] && Actor.findChar(pos) == null) {

            Wraith w = new Wraith();
            w.adjustStats(Dungeon.depth);
            w.pos = pos;
            w.state = w.HUNTING;
            GameScene.add(w, SPAWN_DELAY);

            w.sprite.alpha(0);
            w.sprite.parent.add(new AlphaTweener(w.sprite, 1, 0.5f));

            w.sprite.emitter().burst(ShadowParticle.CURSE, 5);

            return w;
        } else {
            return null;
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        adjustStats(level);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddImpact(Random.NormalIntRange(2 + level / 2, 3 + level * 2));
        return dmg;

    }

    @Override
    public int attackSkill(Char target) {
        return 10 + level;
    }

    public void adjustStats(int level) {
        this.level = level;
        defenseSkill = attackSkill(null) * 5;
        enemySeen = true;
    }

    @Override
    public boolean reset() {
        state = WANDERING;
        return true;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
