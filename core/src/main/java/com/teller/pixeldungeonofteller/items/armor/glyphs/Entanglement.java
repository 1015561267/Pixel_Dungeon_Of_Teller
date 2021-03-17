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
package com.teller.pixeldungeonofteller.items.armor.glyphs;

import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Roots;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.particles.EarthParticle;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.armor.Armor.Glyph;
import com.teller.pixeldungeonofteller.plants.Earthroot;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Entanglement extends Glyph {

    private static ItemSprite.Glowing BROWN = new ItemSprite.Glowing(0x663300);

    @Override
    public Damage proc(Armor armor, Char attacker, Char defender, Damage damage) {

        int level = Math.max(0, armor.level());

        if (Random.Int(3) == 0) {

            Buff.prolong(defender, Roots.class, 5);
            Buff.affect(defender, Earthroot.Armor.class).level(5 + level);
            CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY, 0.05f, 8);
            Camera.main.shake(1, 0.4f);

        }

        return damage;
    }

    @Override
    public Glowing glowing() {
        return BROWN;
    }

}
