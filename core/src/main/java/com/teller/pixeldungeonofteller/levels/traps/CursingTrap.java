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
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.items.EquipableItem;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.KindofMisc;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.artifacts.Artifact;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.missiles.Boomerang;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class CursingTrap extends Trap {

    {
        color = VIOLET;
        shape = WAVES;
    }

    public static void curse(Hero hero) {
        //items the trap wants to curse because it will create a more negative effect
        ArrayList<Item> priorityCurse = new ArrayList<>();
        //items the trap can curse if nothing else is available.
        ArrayList<Item> canCurse = new ArrayList<>();

        KindOfWeapon mainhandweapon = hero.belongings.mainhandweapon;
        if (mainhandweapon instanceof Weapon && !mainhandweapon.cursed && !(mainhandweapon instanceof Boomerang)) {
            if (((Weapon) mainhandweapon).enchantment == null)
                priorityCurse.add(mainhandweapon);
            else
                canCurse.add(mainhandweapon);
        }

        Armor armor = hero.belongings.armor;
        if (armor != null && !armor.cursed) {
            if (armor.glyph == null)
                priorityCurse.add(armor);
            else
                canCurse.add(armor);
        }

        KindofMisc misc1 = hero.belongings.misc1;
        if (misc1 instanceof Artifact) {
            priorityCurse.add(misc1);
        } else if (misc1 instanceof Ring) {
            canCurse.add(misc1);
        }

        KindofMisc misc2 = hero.belongings.misc2;
        if (misc2 instanceof Artifact) {
            priorityCurse.add(misc2);
        } else if (misc2 instanceof Ring) {
            canCurse.add(misc2);
        }

        Collections.shuffle(priorityCurse);
        Collections.shuffle(canCurse);

        int numCurses = Random.Int(2) == 0 ? 1 : 2;

        for (int i = 0; i < numCurses; i++) {
            if (!priorityCurse.isEmpty()) {
                curse(priorityCurse.remove(0));
            } else if (!canCurse.isEmpty()) {
                curse(canCurse.remove(0));
            }
        }

        EquipableItem.equipCursed(hero);
        GLog.n(Messages.get(CursingTrap.class, "curse"));
    }

    private static void curse(Item item) {
        item.cursed = item.cursedKnown = true;

        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            if (w.enchantment == null) {
                w.enchantment = Weapon.Enchantment.randomCurse();
            }
        }
        if (item instanceof Armor) {
            Armor a = (Armor) item;
            if (a.glyph == null) {
                a.glyph = Armor.Glyph.randomCurse();
            }
        }
    }

    @Override
    public void activate() {
        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
            Sample.INSTANCE.play(Assets.SND_CURSED);
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            for (Item item : heap.items) {
                if (item.isUpgradable())
                    curse(item);
            }
        }

        if (Dungeon.hero.pos == pos) {
            curse(Dungeon.hero);
        }
    }
}
