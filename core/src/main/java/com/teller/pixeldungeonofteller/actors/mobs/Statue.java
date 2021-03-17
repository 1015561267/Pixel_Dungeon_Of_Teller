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
import com.teller.pixeldungeonofteller.journal.Notes;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.Weapon.Enchantment;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Vampiric;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Statue extends Mob {

    private static final String WEAPON = "weapon";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(ToxicGas.class);
        RESISTANCES.add(Poison.class);
        RESISTANCES.add(Grim.class);
        IMMUNITIES.add(Vampiric.class);
        IMMUNITIES.add(Bleeding.class);
    }

    protected Weapon weapon;

    {
        spriteClass = StatueSprite.class;

        EXP = 0;
        state = PASSIVE;
    }

    public Statue() {
        super();

        do {
            weapon = (Weapon) Generator.random(Generator.Category.WEAPON);
        } while (!(weapon.attackable()) || (weapon instanceof MissileWeapon) || weapon.cursed);

        weapon.identify();
        weapon.enchant(Enchantment.random());

        HP = HT = 10 + Dungeon.depth;
        ARMOR = Dungeon.depth * 3;
        SlashThreshold=3+Dungeon.depth/2;
        SHLD = 0;
        MAXSHLD = SHLD;
        defenseSkill = 4 + Dungeon.depth;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(WEAPON, weapon);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        weapon = (Weapon) bundle.get(WEAPON);
    }

    @Override
    protected boolean act() {
        if (Dungeon.visible[pos]) {
            Notes.add( Notes.Landmark.STATUE );
        }
        return super.act();
    }

    @Override
    public PhysicalDamage damageRoll() {
        return weapon.damageRoll(this);
        //return Random.NormalIntRange( weapon.min(), weapon.max() );
    }

    @Override
    public int attackSkill(Char target) {
        return (int) ((9 + Dungeon.depth) * weapon.ACC);
    }

    @Override
    protected float attackDelay() {
        return weapon.DLY;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return Dungeon.level.distance(pos, enemy.pos) <= weapon.RCH;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, Dungeon.depth + weapon.defenseFactor(null));
    }

    @Override
    public void damage(Damage dmg, Object src) {

        if (state == PASSIVE) {
            state = HUNTING;
        }

        super.damage(dmg, src);
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        return weapon.proc(this, enemy, damage);
    }

    @Override
    public void beckon(int cell) {
        // Do nothing
    }

    @Override
    public void die(Object cause) {
        Dungeon.level.drop(weapon, pos).sprite.drop();
        super.die(cause);
    }

    @Override
    public void destroy() {
        Notes.remove( Notes.Landmark.STATUE );
        super.destroy();
    }

    @Override
    public boolean reset() {
        state = PASSIVE;
        return true;
    }

    @Override
    public String description() {
        return Messages.get(this, "desc", weapon.name()) + "\nhp:" + HP + "/" + HT + "\nArmor:" + ARMOR + "\nShield:" + SHLD + "/" + MAXSHLD;
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
