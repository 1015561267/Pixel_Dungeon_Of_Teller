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
package com.teller.pixeldungeonofteller.items.weapon.missiles;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.PinCushion;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.hero.HeroClass;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.rings.RingOfSharpshooting;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Projecting;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.Flintlock;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;

abstract public class MissileWeapon extends Weapon {

    PhysicalPercentage percentage = new PhysicalPercentage(0f, 0f, 0f);

    {
        stackable = true;
        levelKnown = true;

        defaultAction = AC_THROW;
        usesTargeting = true;
    }

    @Override
    public Type WeaponType() {
        return Type.Missile;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_EQUIP);
        return actions;
    }

    @Override
    public int throwPos(Hero user, int dst) {
        if (hasEnchant(Projecting.class)
                && !Dungeon.level.solid[dst] && Dungeon.level.distance(user.pos, dst) <= 4) {
            return dst;
        } else {
            return super.throwPos(user, dst);
        }
    }

    @Override
    protected void onThrow(int cell) {
        Char enemy = Actor.findChar(cell);
        if (enemy == null || enemy == curUser) {
            if (this instanceof Boomerang)
                super.onThrow(cell);
            else
                miss(cell);
        } else {
            if (!curUser.shoot(enemy, this)) {
                miss(cell);
            } else if (!(this instanceof Boomerang)) {

                int bonus = RingOfSharpshooting.getBonus(curUser, RingOfSharpshooting.Aim.class);

                if (curUser.heroClass == HeroClass.HUNTRESS && enemy.buff(PinCushion.class) == null)
                    bonus += 3;

                    if (Random.Float() > Math.pow(0.7, bonus)) {
                        if (enemy.isAlive())
                            Buff.affect(enemy, PinCushion.class).stick(this);
                        else
                            Dungeon.level.drop(this, enemy.pos).sprite.drop();
                    }
            }
        }
    }

    protected void miss(int cell) {
        int bonus = RingOfSharpshooting.getBonus(curUser, RingOfSharpshooting.Aim.class);
        //degraded ring of sharpshooting will even make missed shots break.
        if (Random.Float() < Math.pow(0.6, -bonus))
            super.onThrow(cell);
    }

    @Override
    public Damage proc(Char attacker, Char defender, Damage damage) {
        Hero hero = (Hero) attacker;
        if (hero.rangedWeapon == null && stackable) {
            if (quantity == 1) {
                doUnequip(hero, false, false);
            } else {
                detach(null);
            }
        }
        return damage;
    }

    @Override
    public Item random() {
        return this;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String info() {

        String info = desc();

        info += "\n\n" + Messages.get(MissileWeapon.class, "stats", imbue.damageFactor(min()), imbue.damageFactor(max()), STRReq());

        if (STRReq() > Dungeon.hero.STR()) {
            info += " " + Messages.get(Weapon.class, "too_heavy");
        } else if (Dungeon.hero.heroClass == HeroClass.HUNTRESS && Dungeon.hero.STR() > STRReq()) {
            info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())) {
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped(Dungeon.hero)) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        }

        info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

        return info;
    }

    @Override
    public PhysicalDamage damageRoll(Char owner) {
        return new PhysicalDamage(imbue.damageFactor(Random.NormalIntRange(min(), max())), percentage);
    }
}
