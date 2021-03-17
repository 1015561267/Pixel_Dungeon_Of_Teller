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
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Knuckles extends DualWieldWeapon {

    @Override
    public int stealth() {return 3;}

    public int Impactdamage(){return 0;}
    public int Slashdamage() {return Random.Int(1,6)+level()*Random.Int(0,2);}
    public int Puncturedamage(){return 0;}

    {
        image = ItemSpriteSheet.KNUCKLEDUSTER;
        tier = 1;
        DLY = 0.5f; //2x speed
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");    }

    @Override
    public int min(int lvl) {
        return 1;
    }

    @Override
    public int max(int lvl) {
        return 6 + 2 * lvl;
    }

    @Override
    public int STRReq(int lvl) {
        return 1;
    }

    @Override
    public int DEXReq(int lvl) {
        return 1;
    }

    public int DEXMINSCALE() { return 1; }
    public int STRMAXSCALE() { return 1; }
    public int DEXMAXSCALE() { return 2; }

    @Override
    public boolean attackable() {
        return true;
    }

    @Override
    public float cooldown() {
        if (Dungeon.hero.belongings.mainhandweapon instanceof Knuckles) {
            return 10f;
        } else if (Dungeon.hero.belongings.mainhandweapon instanceof DualWieldWeapon) {
            return 20f;
        } else return 20f;
    }
}
