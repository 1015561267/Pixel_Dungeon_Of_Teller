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
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Chill;
import com.teller.pixeldungeonofteller.actors.buffs.Frost;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLiquidFlame;
import com.teller.pixeldungeonofteller.items.wands.WandOfFireblast;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Blazing;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.sprites.ElementalSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Elemental extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(Blazing.class);
        IMMUNITIES.add(WandOfFireblast.class);
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Bleeding.class);
    }

    {
        spriteClass = ElementalSprite.class;

        HP = HT = 0;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 85;
        MAXSHLD = 85;


        defenseSkill = 20;

        EXP = 10;
        maxLvl = 20;

        flying = true;

        loot = new PotionOfLiquidFlame();
        lootChance = 0.1f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(3, 4));
        dmg.AddPuncture(Random.NormalIntRange(3, 4));
        dmg.AddImpact(Random.NormalIntRange(7, 10));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (Random.Int(2) == 0) {
            MagicalDamage dmg = new MagicalDamage();
            dmg.AddFire(Random.NormalIntRange(3, 8));
            enemy.damage(damage, enemy);
            Buff.affect(enemy, Burning.class).reignite(enemy);
        }
        return damage;
    }

    @Override
    public void add(Buff buff) {
        if (buff instanceof Burning) {
            if (SHLD < MAXSHLD) {
                SHLD++;
                sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            }
        } else if (buff instanceof Frost || buff instanceof Chill) {
            MagicalDamage magicalDamage = new MagicalDamage();
            if (Dungeon.level.water[this.pos]) {
                magicalDamage.AddIce(Random.NormalIntRange(MAXSHLD / 2, MAXSHLD));
                //damage(new AbsoluteDamage(Random.NormalIntRange(HT / 2, HT), this, this), buff);
            } else {
                magicalDamage.AddIce(Random.NormalIntRange(1, MAXSHLD * 2 / 3));
            }
            damage(magicalDamage, this);
            //damage( new AbsoluteDamage(Random.NormalIntRange( 1, HT * 2 / 3 ),this,this), buff );
        } else {
            super.add(buff);
        }
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    public boolean isAlive() {
        return SHLD > 0;
    }
}
