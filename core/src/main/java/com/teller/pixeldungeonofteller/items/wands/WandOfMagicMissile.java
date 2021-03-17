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
package com.teller.pixeldungeonofteller.items.wands;

import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Recharging;
import com.teller.pixeldungeonofteller.effects.SpellSprite;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.MagesStaff;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class WandOfMagicMissile extends DamageWand {

    {
        image = ItemSpriteSheet.WAND_MAGIC_MISSILE;
    }

    public int min(int lvl) {
        return 2 + lvl;
    }

    public int max(int lvl) {
        return 8 + 2 * lvl;
    }

    @Override
    protected void onZap(Ballistica bolt) {

        Char ch = Actor.findChar(bolt.collisionPos);
        if (ch != null) {

            processSoulMark(ch, chargesPerCast());

            MagicalDamage dmg = new MagicalDamage();
            dmg.AddArcane(damageRoll());
            ch.damage(dmg, this);

            ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);

        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, Damage damage) {
        Buff.prolong(attacker, Recharging.class, 1 + staff.level() / 2f);
        SpellSprite.show(attacker, SpellSprite.CHARGE);

    }

    protected int initialCharges() {
        return 3;
    }

}
