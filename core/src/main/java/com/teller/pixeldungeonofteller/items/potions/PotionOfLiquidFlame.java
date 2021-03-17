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
package com.teller.pixeldungeonofteller.items.potions;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.blobs.Blob;
import com.teller.pixeldungeonofteller.actors.blobs.Fire;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotionOfLiquidFlame extends Potion {

    {
        initials = 5;
    }

    @Override
    public void shatter(int cell) {

        if (Dungeon.visible[cell]) {
            setKnown();

            splash(cell);
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }

        for (int offset : PathFinder.NEIGHBOURS9) {
            if (Dungeon.level.flamable[cell + offset]
                    || Actor.findChar(cell + offset) != null
                    || Dungeon.level.heaps.get(cell + offset) != null) {

                GameScene.add(Blob.seed(cell + offset, 2, Fire.class));

            } else {

                CellEmitter.get(cell + offset).burst(FlameParticle.FACTORY, 2);

            }
        }
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
