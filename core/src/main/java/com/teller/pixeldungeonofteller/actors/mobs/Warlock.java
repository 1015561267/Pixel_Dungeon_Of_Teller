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
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Weakness;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.potions.PotionOfHealing;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.WarlockSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Warlock extends Mob implements Callback {

    private static final float TIME_TO_ZAP = 1f;
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(Grim.class);
    }

    {
        spriteClass = WarlockSprite.class;

        HP = HT = 40;
        ARMOR = 15;
        SlashThreshold=8;
        SHLD = 45;
        MAXSHLD = 45;

        defenseSkill = 18;

        EXP = 11;
        maxLvl = 21;

        loot = Generator.Category.POTION;
        lootChance = 0.83f;

        properties.add(Property.UNDEAD);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(1, 12));
        dmg.AddPuncture(Random.NormalIntRange(1, 4));
        dmg.AddImpact(Random.NormalIntRange(1, 8));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 8);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack(Char enemy) {

        if (Dungeon.level.adjacent(pos, enemy.pos)) {

            return super.doAttack(enemy);

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap(enemy.pos);
            } else {
                zap();
            }

            return !visible;
        }
    }

    private void zap() {
        spend(TIME_TO_ZAP);

        if (hit(this, enemy, true)) {
            if (enemy == Dungeon.hero && Random.Int(2) == 0) {
                Buff.prolong(enemy, Weakness.class, Weakness.duration(enemy));
            }

            MagicalDamage dmg = new MagicalDamage();
            dmg.AddShadow(Random.Int(12, 20));
            enemy.damage(dmg, this);

            if (!enemy.isAlive() && enemy == Dungeon.hero) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "bolt_kill"));
            }
        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public Item createLoot() {
        Item loot = super.createLoot();

        if (loot instanceof PotionOfHealing) {

            //count/10 chance of not dropping potion
            if (Random.Int(10) - Dungeon.limitedDrops.warlockHP.count < 0) {
                return null;
            } else
                Dungeon.limitedDrops.warlockHP.count++;

        }

        return loot;
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }
}
