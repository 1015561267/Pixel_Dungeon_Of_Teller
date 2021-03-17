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
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.potions.PotionOfHealing;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Vampiric;
import com.teller.pixeldungeonofteller.sprites.BatSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Bat extends Mob {

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(Vampiric.class);
    }

    {
        spriteClass = BatSprite.class;

        HP = HT = 35;
        ARMOR = 8;
        SlashThreshold=6;
        SHLD = 0;
        MAXSHLD = 0;

        defenseSkill = 15;
        baseSpeed = 2f;

        EXP = 7;
        maxLvl = 15;

        flying = true;

        loot = new PotionOfHealing();
        lootChance = 0.1667f; //by default, see die()
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 12));
        dmg.AddPuncture(Random.NormalIntRange(1, 2));
        dmg.AddImpact(Random.NormalIntRange(2, 6));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 16;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        int reg = Math.min(damage.effictivehpdamage + damage.effictiveslddamage, HT - HP);

        if (reg > 0) {
            HP += reg;
            sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
        }

        return damage;
    }

    @Override
    public void die(Object cause) {
        //sets drop chance
        lootChance = 1f / ((6 + Dungeon.limitedDrops.batHP.count));
        super.die(cause);
    }

    @Override
    protected Item createLoot() {
        Dungeon.limitedDrops.batHP.count++;
        return super.createLoot();
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }
}
