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
package com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.rings.RingOfSharpshooting;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Projecting;
import com.teller.pixeldungeonofteller.items.weapon.missiles.Boomerang;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class Tamahawk extends DualWieldWeapon {

    @Override
    public int stealth() {return 1;}

    public int Impactdamage(){return Random.Int(4,14);}
    public int Slashdamage() {return Random.Int(2,10);}
    public int Puncturedamage(){return Random.Int(2,10);}

    private boolean throwEquiped;
    {
        image = ItemSpriteSheet.TOMAHAWK;
        stackable = false;
        defaultAction = AC_THROW;
        usesTargeting = true;
        tier = 5;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") ;  }

    @Override
    public int min(int lvl) {
        return 8 ;
    }

    @Override
    public int max(int lvl) {
        return 34;
    }

    @Override
    public int STRReq(int lvl) {
        return 5;
    }

    @Override
    public int DEXReq(int lvl) {
        return 4;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
    }

    protected static CellSelector.Listener tamahawk_thrower = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                curItem.cast(curUser, target);
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Item.class, "prompt");
        }
    };

    @Override
    public int throwPos(Hero user, int dst) {
        if (hasEnchant(Projecting.class)
                && !Dungeon.level.solid[dst] && Dungeon.level.distance(user.pos, dst) <= 4) {
            return dst;
        } else {
            return super.throwPos(user, dst);
        }
    }

    public boolean isUpgradable() {
        return false;
    }

    @Override
    public void cast(Hero user, int dst) {
        throwEquiped = isEquipped(user) && !cursed;

        if (throwEquiped) Dungeon.quickslot.convertToPlaceholder(this);
        super.cast(user, dst);
    }

    @Override
    protected void onThrow(int cell) {

        if(!throwEquiped)
        {
            super.onThrow(cell);
        }
        else {
            Char enemy = Actor.findChar(cell);
            if (enemy != null && enemy != curUser) {
                curUser.shoot(enemy, this);
            }
            Heap heap = Dungeon.level.drop(this, cell);
            if (!heap.isEmpty()) {
                heap.sprite.drop(cell);
            }
        }
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
        damage.tamahaawk=true;
        return damage;
    }

    @Override
    public PhysicalDamage damageRoll(Char owner) {
        PhysicalDamage dmg=super.damageRoll(owner);
        dmg.tamahaawk=true;
        return dmg;
    }

    @Override
    public int price() {
        return 15;
    }

    @Override
    public boolean attackable() {
        return true;
    }

    @Override
    public float cooldown() {
        if (hero.belongings.mainhandweapon instanceof Tamahawk) {
            return 40f;
        } else if (hero.belongings.mainhandweapon instanceof DualWieldWeapon) {
            return 40f;
        } else return 40f;
    }
}
