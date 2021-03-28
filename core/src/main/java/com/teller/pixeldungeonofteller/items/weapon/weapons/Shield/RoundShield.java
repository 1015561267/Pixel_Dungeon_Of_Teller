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
package com.teller.pixeldungeonofteller.items.weapon.weapons.Shield;

import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Guard;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RoundShield extends Shield {

    private static final float TIME_TO_GUARD = 1;
    private static final String AC_GUARD = "GUARD";
    {
        image = ItemSpriteSheet.ROUND_SHIELD;
        defaultAction = AC_GUARD;
        tier = 3;
    }

    @Override
    public Type WeaponType() {
        return Type.OffHand;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean doEquip(final Hero hero) {
        if (super.doEquip(hero)) {
            identify();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int min(int lvl) {
        return 0;
    }

    @Override
    public int max(int lvl) {
        return 0;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.belongings.offhandweapon == this) {
            actions.add(AC_GUARD);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_GUARD)) {
            if (hero.belongings.offhandweapon == this) {
                curUser.sprite.operate(curUser.pos);
                curUser.spend(TIME_TO_GUARD);
                curUser.busy();
                if (hero.buff(Guard.class) != null) {
                    Buff.detach(hero, Guard.class);
                }
                Buff.affect(hero, Guard.class, 4f);
            } else {
                GLog.w(Messages.get(this, "needtoequip"));
            }
        }
    }

    @Override
    public int STRReq(int lvl) {
        return 0;
    }

    @Override
    public int DEXReq(int lvl) {
        return 1;
    }

    @Override
    public int defenseFactor(Hero hero) {
        return 5 + 2 * level();     //5 extra defence, plus 2 per level;
    }

}