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

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.SkeletonSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Skeleton extends Mob {

    private static final String TXT_HERO_KILLED = "You were killed by the explosion of bones...";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Bleeding.class);
    }

    {
        spriteClass = SkeletonSprite.class;

        HP = HT = 0;
        ARMOR = 15;
        SHLD = 20;
        MAXSHLD = SHLD;
        SlashThreshold=4;
        defenseSkill = 9;

        EXP = 5;
        maxLvl = 10;

        loot = Generator.Category.WEAPON;
        lootChance = 0.2f;

        properties.add(Property.UNDEAD);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(1, 2));
        dmg.AddPuncture(Random.NormalIntRange(1, 1));
        dmg.AddImpact(Random.NormalIntRange(1, 7));
        return dmg;
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        boolean heroKilled = false;
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
            if (ch != null && ch.isAlive()) {
                PhysicalDamage dmg = new PhysicalDamage();
                dmg.AddSlash(2);
                dmg.AddImpact(Random.NormalIntRange(2, 6));
                //int damage = Math.max( 0,  damageRoll() - (ch.drRoll() / 2) );
                ch.damage(dmg, this);
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
        }

        if (Dungeon.visible[pos]) {
            Sample.INSTANCE.play(Assets.SND_BONES);
        }

        if (heroKilled) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(this, "explo_kill"));
        }
    }

    @Override
    protected Item createLoot() {
        Item loot;
        do {
            loot = Generator.random(Generator.Category.WEAPON);
            //50% chance of re-rolling tier 4 or 5 items
        } while (loot instanceof Weapon && ((Weapon) loot).tier >= 4 && Random.Int(2) == 0);
        loot.level(0);
        return loot;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    public boolean isAlive() {
        return SHLD > 0;
    }
}
