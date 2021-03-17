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
package com.teller.pixeldungeonofteller.items.scrolls;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Blindness;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.ItemStatusHandler;
import com.teller.pixeldungeonofteller.items.artifacts.UnstableSpellbook;
import com.teller.pixeldungeonofteller.journal.Catalog;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.HeroSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Scroll extends Item {

    public static final String AC_READ = "READ";

    protected static final float TIME_TO_READ = 1f;
    private static final Class<?>[] scrolls = {
            ScrollOfIdentify.class,
            ScrollOfMagicMapping.class,
            ScrollOfRecharging.class,
            ScrollOfRemoveCurse.class,
            ScrollOfTeleportation.class,
            ScrollOfUpgrade.class,
            ScrollOfRage.class,
            ScrollOfTerror.class,
            ScrollOfLullaby.class,
            ScrollOfMagicalInfusion.class,
            ScrollOfPsionicBlast.class,
            ScrollOfMirrorImage.class
    };
    private static final HashMap<String, Integer> runes = new HashMap<String, Integer>() {
        {
            put("KAUNAN", ItemSpriteSheet.SCROLL_KAUNAN);
            put("SOWILO", ItemSpriteSheet.SCROLL_SOWILO);
            put("LAGUZ", ItemSpriteSheet.SCROLL_LAGUZ);
            put("YNGVI", ItemSpriteSheet.SCROLL_YNGVI);
            put("GYFU", ItemSpriteSheet.SCROLL_GYFU);
            put("RAIDO", ItemSpriteSheet.SCROLL_RAIDO);
            put("ISAZ", ItemSpriteSheet.SCROLL_ISAZ);
            put("MANNAZ", ItemSpriteSheet.SCROLL_MANNAZ);
            put("NAUDIZ", ItemSpriteSheet.SCROLL_NAUDIZ);
            put("BERKANAN", ItemSpriteSheet.SCROLL_BERKANAN);
            put("ODAL", ItemSpriteSheet.SCROLL_ODAL);
            put("TIWAZ", ItemSpriteSheet.SCROLL_TIWAZ);
        }
    };
    private static ItemStatusHandler<Scroll> handler;
    public boolean ownedByBook = false;
    protected int initials;
    private String rune;

    {
        stackable = true;
        defaultAction = AC_READ;
    }

    public Scroll() {
        super();
        reset();
    }

    @SuppressWarnings("unchecked")
    public static void initLabels() {
        handler = new ItemStatusHandler<>((Class<? extends Scroll>[]) scrolls, runes);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    public static void saveSelectively(Bundle bundle, ArrayList<Item> items) {
        handler.saveSelectively(bundle, items);
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        handler = new ItemStatusHandler<>((Class<? extends Scroll>[]) scrolls, runes, bundle);
    }

    public static HashSet<Class<? extends Scroll>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Scroll>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == scrolls.length;
    }

    @Override
    public void reset() {
        super.reset();
        image = handler.image(this);
        rune = handler.label(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_READ)) {

            if (hero.buff(Blindness.class) != null) {
                GLog.w(Messages.get(this, "blinded"));
            } else if (hero.buff(UnstableSpellbook.bookRecharge.class) != null
                    && hero.buff(UnstableSpellbook.bookRecharge.class).isCursed()
                    && !(this instanceof ScrollOfRemoveCurse)) {
                GLog.n(Messages.get(this, "cursed"));
            } else {
                curUser = hero;
                curItem = detach(hero.belongings.backpack);
                doRead();
                hero.buff(Noise.class).readScrollNoise();
            }

        }
    }

    abstract protected void doRead();

    protected void readAnimation() {
        curUser.spend(TIME_TO_READ);
        curUser.busy();
        ((HeroSprite) curUser.sprite).read();
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    public void setKnown() {
        if (!isKnown() && !ownedByBook) {
            handler.know(this);
            updateQuickslot();
        }

        if (Dungeon.hero.isAlive()) {
            Catalog.setSeen(getClass());
        }

        Badges.validateAllScrollsIdentified();
    }

    @Override
    public Item identify() {
        setKnown();
        return super.identify();
    }

    @Override
    public String name() {
        return isKnown() ? name : Messages.get(Scroll.class, rune);
    }

    @Override
    public String info() {
        return isKnown() ?
                desc() :
                Messages.get(this, "unknown_desc");
    }

    public Integer initials() {
        return isKnown() ? initials : null;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return isKnown();
    }

    @Override
    public int price() {
        return 30 * quantity;
    }
}
