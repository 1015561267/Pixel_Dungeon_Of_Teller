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

import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Greataxe extends TwoHandedWeapon {

    @Override
    public int stealth() {return 0;}

    public int Impactdamage(){return Random.Int(3,20)+level()*Random.Int(1,3);}
    public int Slashdamage() {return Random.Int(3,25)+level()*Random.Int(0,2);}
    public int Puncturedamage(){return Random.Int(3,10)+level()*Random.Int(1,4);}

    {
        image = ItemSpriteSheet.GREATAXE;

        DLY = 1.5f;
        tier = 5;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");}

    @Override
    public int min(int lvl) {
        return 9 + 2 * lvl;
    }

    @Override
    public int max(int lvl) { return 55 + 9 * lvl; }

    @Override
    public int STRReq(int lvl) {
        return 6;
    }

    public int STRMINSCALE() { return 2; }
    public int DEXMINSCALE() { return 1; }
    public int STRMAXSCALE() { return 4; }
    public int DEXMAXSCALE() { return 2; }
    public int INTMAXSCALE() { return 1; }
}
