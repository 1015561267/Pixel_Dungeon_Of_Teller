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
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.Statistics;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.blobs.VenomGas;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Frost;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.actors.buffs.Roots;
import com.teller.pixeldungeonofteller.items.food.MysteryMeat;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.sprites.PiranhaSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Piranha extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(Paralysis.class);
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(VenomGas.class);
        IMMUNITIES.add(Roots.class);
        IMMUNITIES.add(Frost.class);
    }

    {
        spriteClass = PiranhaSprite.class;

        baseSpeed = 2f;

        EXP = 0;
    }

    public Piranha() {
        super();

        HP = HT = 20 + Dungeon.depth * 4;
        ARMOR = (int) (1.5*Dungeon.depth);
        SlashThreshold=Dungeon.depth;
        SHLD = 0;
        MAXSHLD = SHLD;

        defenseSkill = 10 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {
        if (!Dungeon.level.water[pos]) {
            die(null);
            sprite.killAndErase();
            return true;
        }
            return super.act();
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddPuncture(Random.NormalIntRange((int) Math.ceil(Dungeon.depth / 2), (int) (2 + Dungeon.depth*1.5)));
        dmg.AddImpact(Random.NormalIntRange((int) Math.ceil(Dungeon.depth / 2), 2 + Dungeon.depth));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 20 + Dungeon.depth * 2;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth);
    }

    @Override
    public void die(Object cause) {
        Dungeon.level.drop(new MysteryMeat(), pos).sprite.drop();
        super.die(cause);

        Statistics.piranhasKilled++;
        Badges.validatePiranhasKilled();
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {

        if (rooted) {
            return false;
        }

        int step = Dungeon.findStep(this, pos, target,
                Dungeon.level.water,
                fieldOfView);
        if (step != -1) {
            move(step);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther(int target) {
        int step = Dungeon.flee(this, pos, target,
                Dungeon.level.water,
                fieldOfView);
        if (step != -1) {
            move(step);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
