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
package com.teller.pixeldungeonofteller.actors.blobs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.effects.BlobEmitter;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.plants.Plant;
import com.teller.pixeldungeonofteller.scenes.GameScene;

public class Fire extends Blob {

    @Override
    protected void evolve() {

        boolean[] flamable = Dungeon.level.flamable;
        int cell;
        int fire;

        boolean observe = false;

        for (int i = area.left - 1; i <= area.right; i++) {
            for (int j = area.top - 1; j <= area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {

                    burn(cell);

                    fire = cur[cell] - 1;
                    if (fire <= 0 && flamable[cell]) {

                        Dungeon.level.destroy(cell);

                        observe = true;
                        GameScene.updateMap(cell);

                    }

                } else {

                    if (flamable[cell]
                            && (cur[cell - 1] > 0
                            || cur[cell + 1] > 0
                            || cur[cell - Dungeon.level.width()] > 0
                            || cur[cell + Dungeon.level.width()] > 0)) {
                        fire = 4;
                        burn(cell);
                        area.union(i, j);
                    } else {
                        fire = 0;
                    }

                }

                volume += (off[cell] = fire);
            }
        }

        if (observe) {
            Dungeon.observe();
        }
    }

    private void burn(int pos) {
        Char ch = Actor.findChar(pos);
        if (ch != null) {
            Buff.affect(ch, Burning.class).reignite(ch);
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            heap.burn();
        }

        Plant plant = Dungeon.level.plants.get(pos);
        if (plant != null) {
            plant.wither();
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);
        emitter.start(FlameParticle.FACTORY, 0.03f, 0);
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
