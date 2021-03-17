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
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Beam;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.bags.Bag;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class DisintegrationTrap extends Trap {

    {
        color = VIOLET;
        shape = LARGE_DOT;
    }

    @Override
    public void activate() {

        if (Dungeon.visible[pos]) {
            PixelDungeonOfTeller.scene().add(new Beam.DeathRay(DungeonTilemap.tileCenterToWorld(pos - 1),
                    DungeonTilemap.tileCenterToWorld(pos + 1)));
            PixelDungeonOfTeller.scene().add(new Beam.DeathRay(DungeonTilemap.tileCenterToWorld(pos - Dungeon.level.width()),
                    DungeonTilemap.tileCenterToWorld(pos + Dungeon.level.width())));
            Sample.INSTANCE.play(Assets.SND_RAY);
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) heap.explode();

        Char ch = Actor.findChar(pos);
        if (ch != null) {
            ch.damage(new AbsoluteDamage(Math.max(ch.HT / 5, Random.Int(ch.HP / 2, 2 * ch.HP / 3)), this, ch), this);
            if (ch == Dungeon.hero) {
                Hero hero = (Hero) ch;
                if (!hero.isAlive()) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "ondeath"));
                } else {
                    Item item = hero.belongings.randomUnequipped();
                    Bag bag = hero.belongings.backpack;
                    //bags do not protect against this trap
                    if (item instanceof Bag) {
                        bag = (Bag) item;
                        item = Random.element(bag.items);
                    }
                    if (item == null || item.level() > 0 || item.unique) return;
                    if (!item.stackable) {
                        item.detachAll(bag);
                        GLog.w(Messages.get(this, "one", item.name()));
                    } else {
                        int n = Random.NormalIntRange(1, (item.quantity() + 1) / 2);
                        for (int i = 1; i <= n; i++)
                            item.detach(bag);
                        GLog.w(Messages.get(this, "some", item.name()));
                    }
                }
            }
        }

    }
}
