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
package com.teller.pixeldungeonofteller.items.bags;

import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.wands.Wand;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class WandHolster extends Bag {

    public static float HOLSTER_SCALE_FACTOR = 0.85f;

    {
        image = ItemSpriteSheet.HOLSTER;

        size = 12;
    }

    @Override
    public boolean grab(Item item) {
        return item instanceof Wand || item instanceof MagicPage || item instanceof MagicBook;
    }

    @Override
    public boolean collect(Bag container) {
        if (super.collect(container)) {
            if (owner != null ) {
                for (Item item : items) {
                    if(item instanceof Wand) {
                        ((Wand) item).charge(owner, HOLSTER_SCALE_FACTOR);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (Item item : items) {
            ((Wand) item).stopCharging();
        }
    }

    @Override
    public int price() {
        return 50;
    }

}
