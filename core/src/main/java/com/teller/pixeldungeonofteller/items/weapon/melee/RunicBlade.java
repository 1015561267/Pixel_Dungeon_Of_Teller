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
package com.teller.pixeldungeonofteller.items.weapon.melee;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class RunicBlade extends MeleeWeapon {

    static int IMPACTFACTOR = 15;

    //Essentially it's a tier 4 weapon, with tier 3 base max damage, and tier 5 scaling.
    //equal to tier 4 in damage at +5
    static int SLASHFACTOR = 55;
    static int PUNCTUREFACTOR = 30;
    PhysicalPercentage percentage = new PhysicalPercentage(0.15f, 0.55f, 0.3f);

    {
        image = ItemSpriteSheet.RUNIC_BLADE;

        tier = 4;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") + "\n\n此武器实际造成的冲击:切割:穿刺比为:\n" + IMPACTFACTOR + "%:" + SLASHFACTOR + "%:" + PUNCTUREFACTOR + "%";
    }

    @Override
    public int min(int lvl) {
        return 4 + 1 * lvl + 1 * (Dungeon.hero.DEX) + 1 * (Dungeon.hero.INT);
    }

    @Override
    public int max(int lvl) {
        return 28 + 4 * lvl + 2 * (STRFACTOR()) + 2 * (Dungeon.hero.DEX) + 6 * (Dungeon.hero.INT);
    }

    @Override
    public int STRReq(int lvl) {
        return 6;
    }
    public int DEXMINSCALE() { return 1; }
    public int INTMINSCALE() { return 1; }
    public int STRMAXSCALE() { return 2; }
    public int DEXMAXSCALE() { return 2; }
    public int INTMAXSCALE() { return 6; }
}
