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
package com.teller.pixeldungeonofteller.plants;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.particles.PoisonParticle;
import com.teller.pixeldungeonofteller.items.potions.PotionOfToxicGas;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class Sorrowmoss extends Plant {

    {
        image = 2;
    }

    @Override
    public void activate() {
        Char ch = Actor.findChar(pos);

        if (ch != null) {
            Buff.affect(ch, Poison.class).set(Poison.durationFactor(ch) * (4 + Dungeon.depth / 2));
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.center(pos).burst(PoisonParticle.SPLASH, 3);
        }
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_SORROWMOSS;

            plantClass = Sorrowmoss.class;
            alchemyClass = PotionOfToxicGas.class;
        }
    }
}
