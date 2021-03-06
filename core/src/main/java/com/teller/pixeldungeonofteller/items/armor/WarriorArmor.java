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
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class WarriorArmor extends ClassArmor {

    private static int LEAP_TIME = 1;
    private static int SHOCK_TIME = 3;
    protected static CellSelector.Listener leaper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            if (target != null && target != curUser.pos) {

                Ballistica route = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
                int cell = route.collisionPos;

                //can't occupy the same cell as another char, so move back one.
                if (Actor.findChar(cell) != null && cell != curUser.pos)
                    cell = route.path.get(route.dist - 1);


                curUser.HP -= (curUser.HP / 3);

                final int dest = cell;
                curUser.busy();
                curUser.sprite.jump(curUser.pos, cell, new Callback() {
                    @Override
                    public void call() {
                        curUser.move(dest);
                        Dungeon.level.press(dest, curUser);
                        Dungeon.observe();
                        GameScene.updateFog();

                        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                            Char mob = Actor.findChar(curUser.pos + PathFinder.NEIGHBOURS8[i]);
                            if (mob != null && mob != curUser) {
                                Buff.prolong(mob, Paralysis.class, SHOCK_TIME);
                            }
                        }

                        CellEmitter.center(dest).burst(Speck.factory(Speck.DUST), 10);
                        Camera.main.shake(2, 0.5f);

                        curUser.spendAndNext(LEAP_TIME);
                    }
                });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(WarriorArmor.class, "prompt");
        }
    };

    {
        image = ItemSpriteSheet.ARMOR_WARRIOR;
    }

    @Override
    public void doSpecial() {
        GameScene.selectCell(leaper);
    }
}