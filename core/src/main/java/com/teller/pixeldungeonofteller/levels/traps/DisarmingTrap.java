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
package com.teller.pixeldungeonofteller.levels.traps;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Knuckles;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class DisarmingTrap extends Trap {

    {
        color = RED;
        shape = LARGE_DOT;
    }

    @Override
    public void activate() {
        Heap heap = Dungeon.level.heaps.get(pos);

        if (heap != null) {
            int cell = Dungeon.level.randomRespawnCell();

            if (cell != -1) {
                Item item = heap.pickUp();
                Dungeon.level.drop(item, cell).seen = true;
                for (int i : PathFinder.NEIGHBOURS9)
                    Dungeon.level.visited[cell + i] = true;
                GameScene.updateFog();

                Sample.INSTANCE.play(Assets.SND_TELEPORT);
                CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
            }
        }

        if (Dungeon.hero.pos == pos) {
            Hero hero = Dungeon.hero;
            KindOfWeapon mainhandweapon = hero.belongings.mainhandweapon;
            KindOfWeapon offhandweapon = hero.belongings.offhandweapon;

            if (mainhandweapon != null && !(mainhandweapon instanceof Knuckles) && !mainhandweapon.cursed) {
                int cell = Dungeon.level.randomRespawnCell();
                if (cell != -1) {
                    hero.belongings.mainhandweapon = null;
                    Dungeon.quickslot.clearItem(mainhandweapon);
                    mainhandweapon.updateQuickslot();

                    Dungeon.level.drop(mainhandweapon, cell).seen = true;
                    for (int i : PathFinder.NEIGHBOURS9)
                        Dungeon.level.visited[cell + i] = true;
                    GameScene.updateFog();

                    GLog.w(Messages.get(this, "disarm"));

                    Sample.INSTANCE.play(Assets.SND_TELEPORT);
                    CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
                }
            } else if (offhandweapon != null && !(offhandweapon instanceof Knuckles) && !offhandweapon.cursed) {
                int cell = Dungeon.level.randomRespawnCell();
                if (cell != -1) {
                    hero.belongings.offhandweapon = null;
                    Dungeon.quickslot.clearItem(mainhandweapon);
                    mainhandweapon.updateQuickslot();

                    Dungeon.level.drop(mainhandweapon, cell).seen = true;
                    for (int i : PathFinder.NEIGHBOURS9)
                        Dungeon.level.visited[cell + i] = true;
                    GameScene.updateFog();

                    GLog.w(Messages.get(this, "disarm"));

                    Sample.INSTANCE.play(Assets.SND_TELEPORT);
                    CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
                }
            }
        }
    }
}
