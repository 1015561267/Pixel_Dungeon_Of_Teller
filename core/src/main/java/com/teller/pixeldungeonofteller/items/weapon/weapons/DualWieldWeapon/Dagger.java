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
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Dagger extends DualWieldWeapon {

    @Override
    public int stealth() {return 3;}

    public int Impactdamage(){return 0;}
    public int Slashdamage() {return Random.Int(1,5)+level();}
    public int Puncturedamage(){return Random.Int(1,3)+level()*Random.Int(0,1);}

    public int backStabPuncturedamage(){return 3+level()*1;}//when surprise attack,Dirk and Dagger deal max puncture dmg

    private PhysicalPercentage percentage() { return new PhysicalPercentage(0,0.7f,0.3f); }

    {
        image = ItemSpriteSheet.DAGGER;
        tier = 1;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") ; }

    @Override
    public int min(int lvl) {
        return 2 + 1 * lvl;
    }

    @Override
    public int max(int lvl) {
        return 8 + 2 * lvl;
    }

    @Override
    public int STRReq(int lvl) {
        return 1;
    }
    @Override
    public int DEXReq(int lvl) {
        return 1;
    }

    public int STRMAXSCALE() { return 1; }
    public int DEXMAXSCALE() { return 3; }

    @Override
    public PhysicalDamage damageRoll(Char owner) {
        if (owner instanceof Hero) {
            PhysicalDamage dmg=new PhysicalDamage();
            Char enemy= ((Hero) owner).enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(owner)) {
                dmg.AddPuncture(imbue.damageFactor(backStabPuncturedamage()));
            }
            else { dmg.AddPuncture(imbue.damageFactor(Puncturedamage())); }
            dmg.AddImpact(imbue.damageFactor(Impactdamage()));
            dmg.AddSlash(imbue.damageFactor(Slashdamage()));
            return dmg;
        }
        else return super.damageRoll(owner);
    }
    @Override
    public boolean attackable() {
        return true;
    }
    @Override
    public float cooldown() {
        if (Dungeon.hero.belongings.mainhandweapon instanceof Dagger) {
            return 30f;
        } else if (Dungeon.hero.belongings.mainhandweapon instanceof DualWieldWeapon) {
            return 30f;
        } else return 30f;
    }
}
