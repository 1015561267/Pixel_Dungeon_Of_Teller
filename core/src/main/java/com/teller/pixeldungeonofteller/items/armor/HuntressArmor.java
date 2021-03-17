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

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Callback;

import java.util.HashMap;

//import com.teller.tellerpixeldungeon.items.weapon.weapons.AttachedWeapon.NinjaProsthesis.Shuriken;

public class HuntressArmor extends ClassArmor {


    private HashMap<Callback, Mob> targets = new HashMap<Callback, Mob>();

    {
        image = ItemSpriteSheet.ARMOR_HUNTRESS;
    }

    @Override
    public void doSpecial() {

        Item proto = new NinjaProsthesis.Shuriken();

        for (Mob mob : Dungeon.level.mobs) {
            if (Dungeon.hero.fieldOfView[mob.pos]) {

                Callback callback = new Callback() {
                    @Override
                    public void call() {
                        curUser.attack(targets.get(this));
                        targets.remove(this);
                        if (targets.isEmpty()) {
                            curUser.spendAndNext(curUser.attackDelay());
                        }
                    }
                };

                ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                        reset(curUser.pos, mob.pos, proto, callback);

                targets.put(callback, mob);
            }
        }

        if (targets.size() == 0) {
            GLog.w(Messages.get(this, "no_enemies"));
            return;
        }

        curUser.HP -= (curUser.HP / 3);

        curUser.sprite.zap(curUser.pos);
        curUser.busy();
    }

}