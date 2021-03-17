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

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.armor.Armor.Glyph;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite.Glowing;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Viscosity extends Glyph {

    private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x8844CC);

    @Override
    public Damage proc(Armor armor, Char attacker, Char defender, Damage damage) {

        if (damage.effictivehpdamage == 0) {
            return damage;
        }

        int level = Math.max(0, armor.level());

        if (Random.Int(level + 4) >= 3) {

            DeferedDamage debuff = defender.buff(DeferedDamage.class);
            if (debuff == null) {
                debuff = new DeferedDamage();
                debuff.attachTo(defender);
            }
            debuff.prolong(damage.effictivehpdamage);

            defender.sprite.showStatus(CharSprite.WARNING, Messages.get(this, "deferred", damage));

            return damage;

        } else {
            return damage;
        }
    }

    @Override
    public Glowing glowing() {
        return PURPLE;
    }

    public static class DeferedDamage extends Buff {

        private static final String DAMAGE = "damage";
        protected int damage = 0;

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DAMAGE, damage);

        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            damage = bundle.getInt(DAMAGE);
        }

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)) {
                postpone(TICK);
                return true;
            } else {
                return false;
            }
        }

        public void prolong(int damage) {
            this.damage += damage;
        }

        @Override
        public int icon() {
            return BuffIndicator.DEFERRED;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public boolean act() {
            if (target.isAlive()) {

                int damageThisTick = Math.max(1, damage / 10);
                target.damage(new AbsoluteDamage(damageThisTick, this, target), this);
                if (target == Dungeon.hero && !target.isAlive()) {

                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "ondeath"));

                    Badges.validateDeathFromGlyph();
                }
                spend(TICK);

                damage -= damageThisTick;
                if (damage <= 0) {
                    detach();
                }

            } else {

                detach();

            }

            return true;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", damage);
        }
    }
}
