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

import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Dart extends MissileWeapon {

    static int IMPACTFACTOR = 0;
    static int SLASHFACTOR = 0;
    static int PUNCTUREFACTOR = 100;
    private PhysicalPercentage percentage() { return new PhysicalPercentage(0,0,1f); }
    {
        image = ItemSpriteSheet.DART;
        bones = false; //Finding them in bones would be semi-frequent and disappointing.
    }

    public Dart() {
        this(1);
    }

    public Dart(int number) {
        super();
        quantity = number;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") + "\n\n此武器实际造成的冲击:切割:穿刺比为:\n" + IMPACTFACTOR + "%:" + SLASHFACTOR + "%:" + PUNCTUREFACTOR + "%";
    }

    @Override
    public int min(int lvl) {
        return 1;
    }

    @Override
    public int max(int lvl) {
        return 4;
    }

    @Override
    public int STRReq(int lvl) {
        return 1;
    }

    @Override
    public int DEXReq(int lvl) {
        return 0;
    }

    @Override
    public Item random() {
        quantity = Random.Int(5, 15);
        return this;
    }

    @Override
    public int price() {
        return quantity * 2;
    }

    @Override
    public Damage proc(Char attacker, Char defender, Damage damage) {
        return new PhysicalDamage(Random.NormalIntRange(min(), max()), percentage());
    }
}
