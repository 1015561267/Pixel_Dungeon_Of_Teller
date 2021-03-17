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

import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class AssassinsBlade extends MeleeWeapon {

    static int IMPACTFACTOR = 0;
    static int SLASHFACTOR = 35;
    static int PUNCTUREFACTOR = 65;
    PhysicalPercentage percentage = new PhysicalPercentage(0f, 0.35f, 0.65f);

    private PhysicalPercentage percentage() { return new PhysicalPercentage(0,0.35f,0.65f); }

    {
        image = ItemSpriteSheet.ASSASSINS_BLADE;

        tier = 4;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") + "\n\n此武器实际造成的冲击:切割:穿刺比为:\n" + IMPACTFACTOR + "%:" + SLASHFACTOR + "%:" + PUNCTUREFACTOR + "%";
    }

    @Override
    public int min(int lvl) {
        return 4 + 1 * lvl ;
    }

    @Override
    public int max(int lvl) { return 28 + 5 * lvl ; }

    @Override
    public int STRReq(int lvl) { return 6; }

    @Override
    public PhysicalDamage damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            int rawdamage = imbue.damageFactor(Random.NormalIntRange(min(), max()));

            Char enemy = hero.enemy();
            if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
                rawdamage = imbue.damageFactor(Random.NormalIntRange((min() + max()) / 2, max()));
            }
            return new PhysicalDamage(rawdamage, percentage());
        }
        return super.damageRoll(owner);
    }
}