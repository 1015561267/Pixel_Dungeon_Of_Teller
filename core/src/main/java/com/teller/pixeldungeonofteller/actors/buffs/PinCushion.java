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
package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tamahawk;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;

public class PinCushion extends Buff {

    private static final String ITEMS = "items";
    private ArrayList<MissileWeapon> items = new ArrayList<MissileWeapon>();
    private ArrayList<Tamahawk> tamahawks = new ArrayList<Tamahawk>();

    public void stick(MissileWeapon projectile) {
        for (Item item : items) {
            if (item.isSimilar(projectile)) {
                item.quantity(item.quantity() + projectile.quantity());
                return;
            }
        }
        items.add(projectile);
    }

    public void stick(Tamahawk tamahawk) {
        tamahawks.add(tamahawk);
        return;
    }

    @Override
    public void detach() {
        for (Item item : items)
            Dungeon.level.drop(item, target.pos).sprite.drop();
        for (Tamahawk tamahawk : tamahawks)
            Dungeon.level.drop(tamahawk, target.pos).sprite.drop();
        super.detach();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(ITEMS, items);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        items = new ArrayList<MissileWeapon>((Collection<MissileWeapon>) ((Collection<?>) bundle.getCollection(ITEMS)));
        super.restoreFromBundle(bundle);
    }
}
