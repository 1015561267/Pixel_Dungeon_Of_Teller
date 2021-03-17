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
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.items.Gold;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.BruteSprite;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Brute extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Terror.class);
    }

    protected boolean enraged = false;

    {
        spriteClass = BruteSprite.class;

        HP = HT = 20;
        ARMOR = 40;
        SlashThreshold=10;
        SHLD = 0;
        MAXSHLD = 0;

        defenseSkill = 15;

        EXP = 8;
        maxLvl = 15;

        loot = Gold.class;
        lootChance = 0.5f;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        enraged = (ARMOR == 0||HP<HT/4 );///HP < HT / 4;
    }

    @Override
    public PhysicalDamage damageRoll() {

        PhysicalDamage dmg = new PhysicalDamage();
        if (enraged) {
            dmg.AddSlash(Random.NormalIntRange(2, 16));
            dmg.AddPuncture(Random.NormalIntRange(6, 28));
            dmg.AddImpact(Random.NormalIntRange(4, 8));
        } else {
            dmg.AddSlash(Random.NormalIntRange(1, 8));
            dmg.AddPuncture(Random.NormalIntRange(3, 14));
            dmg.AddImpact(Random.NormalIntRange(2, 4));
        }
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 8);
    }

    @Override
    public void damage(Damage dmg, Object src) {
        super.damage(dmg, src);

        //if (isAlive() && !enraged && HP < HT / 4) {
        if (isAlive() && !enraged && ARMOR == 0 && SHLD == 0) {
            enraged = true;
            spend(TICK);
            if (Dungeon.visible[pos]) {
                GLog.w(Messages.get(this, "enraged_text"));
                sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enraged"));
            }
        }
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
