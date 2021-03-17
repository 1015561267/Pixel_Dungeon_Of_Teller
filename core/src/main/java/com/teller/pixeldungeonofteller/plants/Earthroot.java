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
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.particles.EarthParticle;
import com.teller.pixeldungeonofteller.items.potions.PotionOfParalyticGas;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;

public class Earthroot extends Plant {

    {
        image = 5;
    }

    @Override
    public void activate() {
        Char ch = Actor.findChar(pos);

        if (ch == Dungeon.hero) {
            Buff.affect(ch, Armor.class).level(ch.HT);
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.bottom(pos).start(EarthParticle.FACTORY, 0.05f, 8);
            Camera.main.shake(1, 0.4f);
        }
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_EARTHROOT;

            plantClass = Earthroot.class;
            alchemyClass = PotionOfParalyticGas.class;

            bones = true;
        }
    }

    public static class Armor extends Buff {

        private static final float STEP = 1f;
        private static final String POS = "pos";
        private static final String LEVEL = "level";
        private int pos;
        private int level;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean attachTo(Char target) {
            pos = target.pos;
            return super.attachTo(target);
        }

        @Override
        public boolean act() {
            if (target.pos != pos) {
                detach();
            }
            spend(STEP);
            return true;
        }

        public Damage absorb(Damage damage) {
            if (level <= damage.sum() - damage.sum() / 2) {
                detach();
                if (damage instanceof PhysicalDamage) {
                    if (level > 0) {
                        level -= ((PhysicalDamage) damage).IMPACTDAMAGE;
                        if (level > 0) {
                            level -= ((PhysicalDamage) damage).PUNCTUREDAMAGE;
                            if (level > 0) level -= ((PhysicalDamage) damage).SLASHDAMAGE;
                        }
                    }
                    if (level < 0) level = 0;
                }
                return damage;
            } else {
                level -= damage.sum() - damage.sum() / 2;
                damage.multiplie(1 / 2);
                return damage;
            }
        }

        public void level(int value) {
            if (level < value) {
                level = value;
            }
            pos = target.pos;
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POS, pos);
            bundle.put(LEVEL, level);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            pos = bundle.getInt(POS);
            level = bundle.getInt(LEVEL);
        }
    }
}
