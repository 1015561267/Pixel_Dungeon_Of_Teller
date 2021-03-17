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
package com.teller.pixeldungeonofteller.items.armor;

import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class ClothArmor extends Armor {
    {
        image = ItemSpriteSheet.ARMOR_CLOTH;
        bones = false; //Finding them in bones would be semi-frequent and disappointing.
    }

    public ClothArmor() {
        super(1);
    }

    @Override
    public int DRMin(int lvl) {
        return 0;
    }

    @Override
    public int DRMax(int lvl) {
        return 1;
    }

    @Override
    public int GETMAXARMOR(int lvl) {
        return 4 + lvl;
    }

    @Override
    public int stealth() { return 3; }

    @Override
    public int GETMAXSHLD(int lvl) {
        return 7 + lvl * 1;
    }
}
