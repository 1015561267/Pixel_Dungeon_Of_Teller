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
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class Knuckles extends Weapon {

    @Override
    public int stealth() {return 3;}

    public int Impactdamage(){return 0;}
    public int Slashdamage() {return Random.Int(1,6)+level()*Random.Int(0,2);}
    public int Puncturedamage(){return 0;}

    @Override
    public Type WeaponType() {
        return Type.DualWield;
    }

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

        boolean same1 = false;
        boolean same2 = false;
        boolean dual1 = false;
        boolean dual2 = false;

        if (hero.belongings.mainhandweapon!=null)
        {
            if(hero.belongings.mainhandweapon instanceof Knuckles){
                same1 = true;
            }
            else if(hero.belongings.mainhandweapon.WeaponType() == Type.DualWield)
            {
                dual1 = true;
            }
        }

        if (hero.belongings.offhandweapon!=null)
        {
            if(hero.belongings.offhandweapon instanceof Knuckles){
                same2 = true;
            }
            else if(hero.belongings.offhandweapon.WeaponType() == Type.DualWield)
            {
                dual2 = true;
            }
        }

        if(same1&&same2) { return 10f; }
        else if(dual1&&dual2) { return 20f; }
        else return 30f;
    }
}
