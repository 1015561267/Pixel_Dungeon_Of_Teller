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
package com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon;

import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Spear extends Weapon {

    @Override
    public int stealth() {return 1;}

    public int Impactdamage(){return Random.Int(2,8)+level()*Random.Int(0,3);}
    public int Slashdamage() {return Random.Int(1,4);}
    public int Puncturedamage(){return Random.Int(2,8)+level()*Random.Int(0,3);}

    @Override
    public Type WeaponType() {
        return Type.TwoHanded;
    }

    {
        image = ItemSpriteSheet.SPEAR;

        tier = 2;
        DLY = 1.5f; //0.67x speed
        RCH = 2;    //extra reach
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") ;   }

    @Override
    public int min(int lvl) {
        return 4 ;
    }

    @Override
    public int max(int lvl) {
        return 20 + 6 * lvl;
    }

    @Override
    public int STRReq(int lvl) {
        return 2;
    }

    @Override
    public int DEXReq(int lvl) {
        return 0;
    }

    public int STRMINSCALE() { return 1; }
    public int DEXMINSCALE() { return 1; }
    public int INTMINSCALE() { return 1; }
    public int STRMAXSCALE() { return 1; }
    public int DEXMAXSCALE() { return 2; }
    public int INTMAXSCALE() { return 1; }
}
