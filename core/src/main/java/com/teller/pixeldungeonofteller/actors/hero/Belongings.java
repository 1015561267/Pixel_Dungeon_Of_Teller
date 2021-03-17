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
package com.teller.pixeldungeonofteller.actors.hero;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.GamesInProgress;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.KindofMisc;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.bags.Bag;
import com.teller.pixeldungeonofteller.items.keys.GoldenKey;
import com.teller.pixeldungeonofteller.items.keys.IronKey;
import com.teller.pixeldungeonofteller.items.keys.Key;
import com.teller.pixeldungeonofteller.items.keys.SkeletonKey;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRemoveCurse;
import com.teller.pixeldungeonofteller.items.wands.Wand;
import com.teller.pixeldungeonofteller.journal.Notes;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Iterator;

public class Belongings implements Iterable<Item> {

    //public static final int BACKPACK_SIZE	= 19;

    public static final int BACKPACK_SIZE = 24;
    private static final String MAINHANDWEAPON = "mainhandweapon";
    private static final String OFFHANDWEAPON = "offhandweapon";
    private static final String ARMOR = "armor";
    private static final String MISC1 = "misc1";
    private static final String MISC2 = "misc2";

    private static final String SPELLBOOK1 ="spellbook1";
    private static final String SPELLBOOK2 ="spellbook2";
    private static final String SPELLBOOK3 ="spellbook3";

    public Bag backpack;
    public KindOfWeapon mainhandweapon = null;
    public KindOfWeapon offhandweapon = null;
    public Armor armor = null;
    public KindofMisc misc1 = null;
    public KindofMisc misc2 = null;

    private Hero owner;
    public Belongings(Hero owner) {
        this.owner = owner;

        backpack = new Bag() {{
            name = Messages.get(Bag.class, "name");
            size = BACKPACK_SIZE;
        }};
        backpack.owner = owner;
    }

    public void storeInBundle(Bundle bundle) {

        backpack.storeInBundle(bundle);

        bundle.put(MAINHANDWEAPON, mainhandweapon);
        bundle.put(OFFHANDWEAPON, offhandweapon);

        bundle.put(ARMOR, armor);
        bundle.put(MISC1, misc1);
        bundle.put(MISC2, misc2);
    }

    public void restoreFromBundle(Bundle bundle) {
        backpack.clear();
        backpack.restoreFromBundle(bundle);

        mainhandweapon = (KindOfWeapon) bundle.get(MAINHANDWEAPON);
        if (mainhandweapon != null) {
            mainhandweapon.activate(owner);
        }

        offhandweapon = (KindOfWeapon) bundle.get(OFFHANDWEAPON);
        if (offhandweapon != null) {
            offhandweapon.activate(owner);
        }

        armor = (Armor) bundle.get(ARMOR);
        if (armor != null) {
            armor.activate(owner);
        }

        misc1 = (KindofMisc) bundle.get(MISC1);
        if (misc1 != null) {
            misc1.activate(owner);
        }

        misc2 = (KindofMisc) bundle.get(MISC2);
        if (misc2 != null) {
            misc2.activate(owner);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Item> T getItem(Class<T> itemClass) {

        for (Item item : this) {
            if (itemClass.isInstance(item)) {
                return (T) item;
            }
        }

        return null;
    }

    public Item getSimilar( Item similar ){

        for (Item item : this) {
            if (item.isSimilar(similar)) {
                return item;
            }
        }

        return null;
    }

    public void identify() {
        for (Item item : this) {
            item.identify();
        }
    }

    public void observe() {
        if (mainhandweapon != null) {
            mainhandweapon.identify();
            Badges.validateItemLevelAquired(mainhandweapon);
        }

        if (offhandweapon != null) {
            offhandweapon.identify();
            Badges.validateItemLevelAquired(offhandweapon);
        }

        if (armor != null) {
            armor.identify();
            Badges.validateItemLevelAquired(armor);
        }
        if (misc1 != null) {
            misc1.identify();
            Badges.validateItemLevelAquired(misc1);
        }
        if (misc2 != null) {
            misc2.identify();
            Badges.validateItemLevelAquired(misc2);
        }

        for (Item item : backpack) {
            item.cursedKnown = true;
        }
    }

    public void uncurseEquipped() {
        ScrollOfRemoveCurse.uncurse(owner, armor, mainhandweapon, offhandweapon, misc1, misc2);
    }

    public Item randomUnequipped() {
        return Random.element(backpack.items);
    }

    public void resurrect(int depth) {
        for (Item item : backpack.items.toArray(new Item[0])) {
            if (item instanceof Key) {
                if (((Key) item).depth == depth) {
                    item.detachAll(backpack);
                }
            } else if (item.unique) {
                item.detachAll(backpack);
                //you keep the bag itself, not its contents.
                if (item instanceof Bag) {
                    ((Bag) item).clear();
                }
                item.collect();
            } else if (!item.isEquipped(owner)) {
                item.detachAll(backpack);
            }
        }

        if (mainhandweapon != null) {
            mainhandweapon.cursed = false;
            mainhandweapon.activate(owner);
        }

        if (offhandweapon != null) {
            offhandweapon.cursed = false;
            offhandweapon.activate(owner);
        }

        if (armor != null) {
            armor.cursed = false;
            armor.activate(owner);
        }

        if (misc1 != null) {
            misc1.cursed = false;
            misc1.activate(owner);
        }
        if (misc2 != null) {
            misc2.cursed = false;
            misc2.activate(owner);
        }
    }

    public int charge(float charge) {

        int count = 0;

        for (Wand.Charger charger : owner.buffs(Wand.Charger.class)) {
            charger.gainCharge(charge);
        }

        return count;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<Item> {

        private int index = 0;

        private Iterator<Item> backpackIterator = backpack.iterator();

        private Item[] equipped = {mainhandweapon, offhandweapon, armor, misc1, misc2};
        private int backpackIndex = equipped.length;

        @Override
        public boolean hasNext() {

            for (int i = index; i < backpackIndex; i++) {
                if (equipped[i] != null) {
                    return true;
                }
            }

            return backpackIterator.hasNext();
        }

        @Override
        public Item next() {

            while (index < backpackIndex) {
                Item item = equipped[index++];
                if (item != null) {
                    return item;
                }
            }

            return backpackIterator.next();
        }

        @Override
        public void remove() {
            switch (index) {
                case 0:
                    equipped[0] = mainhandweapon = null;
                    break;
                case 1:
                    equipped[1] = offhandweapon = null;
                    break;
                case 2:
                    equipped[2] = armor = null;
                    break;
                case 3:
                    equipped[3] = misc1 = null;
                    break;
                case 4:
                    equipped[4] = misc2 = null;
                    break;
                default:
                    backpackIterator.remove();
            }
        }
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle ) {
        if (bundle.contains( ARMOR )){
            info.armorTier = ((Armor)bundle.get( ARMOR )).tier;
        } else {
            info.armorTier = 0;
        }
    }
}
