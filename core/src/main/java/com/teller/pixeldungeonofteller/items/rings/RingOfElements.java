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
package com.teller.pixeldungeonofteller.items.rings;

import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.actors.buffs.Venom;
import com.teller.pixeldungeonofteller.actors.mobs.Eye;
import com.teller.pixeldungeonofteller.actors.mobs.Warlock;
import com.teller.pixeldungeonofteller.actors.mobs.Yog;
import com.teller.pixeldungeonofteller.levels.traps.LightningTrap;
import com.watabou.utils.Random;

import java.util.HashSet;

public class RingOfElements extends Ring {

    public static final HashSet<Class<?>> FULL;
    private static final HashSet<Class<?>> EMPTY = new HashSet<Class<?>>();

    static {
        FULL = new HashSet<Class<?>>();
        FULL.add(Burning.class);
        FULL.add(ToxicGas.class);
        FULL.add(Poison.class);
        FULL.add(Venom.class);
        FULL.add(LightningTrap.Electricity.class);
        FULL.add(Warlock.class);
        FULL.add(Eye.class);
        FULL.add(Yog.BurningFist.class);
    }

    @Override
    protected RingBuff buff() {
        return new Resistance();
    }

    public class Resistance extends RingBuff {
        public HashSet<Class<?>> resistances() {
            if (Random.Int(level() + 2) >= 2) {
                return FULL;
            } else {
                return EMPTY;
            }
        }

        public float durationFactor() {
            return level() < 0 ? 1 : (1 + 0.5f * level()) / (1 + level());
        }
    }
}
