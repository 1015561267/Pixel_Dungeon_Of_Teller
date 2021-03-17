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
package com.teller.pixeldungeonofteller.items.potions;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.blobs.Fire;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.buffs.Ooze;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Splash;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.ItemStatusHandler;
import com.teller.pixeldungeonofteller.journal.Catalog;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Potion extends Item {

    public static final String AC_DRINK = "DRINK";

    private static final float TIME_TO_DRINK = 1f;
    private static final Class<?>[] potions = {
            PotionOfHealing.class,
            PotionOfExperience.class,
            PotionOfToxicGas.class,
            PotionOfLiquidFlame.class,
            PotionOfStrength.class,
            PotionOfParalyticGas.class,
            PotionOfLevitation.class,
            PotionOfMindVision.class,
            PotionOfPurity.class,
            PotionOfInvisibility.class,
            PotionOfMight.class,
            PotionOfFrost.class
    };
    private static final HashMap<String, Integer> colors = new HashMap<String, Integer>() {
        {
            put("crimson", ItemSpriteSheet.POTION_CRIMSON);
            put("amber", ItemSpriteSheet.POTION_AMBER);
            put("golden", ItemSpriteSheet.POTION_GOLDEN);
            put("jade", ItemSpriteSheet.POTION_JADE);
            put("turquoise", ItemSpriteSheet.POTION_TURQUOISE);
            put("azure", ItemSpriteSheet.POTION_AZURE);
            put("indigo", ItemSpriteSheet.POTION_INDIGO);
            put("magenta", ItemSpriteSheet.POTION_MAGENTA);
            put("bistre", ItemSpriteSheet.POTION_BISTRE);
            put("charcoal", ItemSpriteSheet.POTION_CHARCOAL);
            put("silver", ItemSpriteSheet.POTION_SILVER);
            put("ivory", ItemSpriteSheet.POTION_IVORY);
        }
    };
    private static ItemStatusHandler<Potion> handler;
    public boolean ownedByFruit = false;
    protected Integer initials;
    private String color;

    {
        stackable = true;
        defaultAction = AC_DRINK;
    }

    public Potion() {
        super();
        reset();
    }

    @SuppressWarnings("unchecked")
    public static void initColors() {
        handler = new ItemStatusHandler<>((Class<? extends Potion>[]) potions, colors);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    public static void saveSelectively(Bundle bundle, ArrayList<Item> items) {
        handler.saveSelectively(bundle, items);
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        handler = new ItemStatusHandler<>((Class<? extends Potion>[]) potions, colors, bundle);
    }

    public static HashSet<Class<? extends Potion>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Potion>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == potions.length;
    }

    @Override
    public void reset() {
        super.reset();
        image = handler.image(this);
        color = handler.label(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_DRINK);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_DRINK)) {

            if (isKnown() && (
                    this instanceof PotionOfLiquidFlame ||
                            this instanceof PotionOfToxicGas ||
                            this instanceof PotionOfParalyticGas)) {

                GameScene.show(
                        new WndOptions(Messages.get(Potion.class, "harmful"),
                                Messages.get(Potion.class, "sure_drink"),
                                Messages.get(Potion.class, "yes"), Messages.get(Potion.class, "no")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    drink(hero);
                                }
                            }

                        }
                );

            } else {
                drink(hero);
            }

        }
    }

    @Override
    public void doThrow(final Hero hero) {

        if (isKnown() && (
                this instanceof PotionOfExperience ||
                        this instanceof PotionOfHealing ||
                        this instanceof PotionOfMindVision ||
                        this instanceof PotionOfStrength ||
                        this instanceof PotionOfInvisibility ||
                        this instanceof PotionOfMight)) {

            GameScene.show(
                    new WndOptions(Messages.get(Potion.class, "beneficial"),
                            Messages.get(Potion.class, "sure_throw"),
                            Messages.get(Potion.class, "yes"), Messages.get(Potion.class, "no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Potion.super.doThrow(hero);
                                hero.buff(Noise.class).throwPotionNoise();
                            }
                        }

                    }
            );

        } else {
            super.doThrow(hero);
            hero.buff(Noise.class).throwPotionNoise();
        }


    }

    protected void drink(Hero hero) {

        detach(hero.belongings.backpack);

        hero.spend(TIME_TO_DRINK);
        hero.busy();
        apply(hero);

        Sample.INSTANCE.play(Assets.SND_DRINK);

        hero.sprite.operate(hero.pos);
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {

            super.onThrow(cell);

        } else {

            Dungeon.level.press(cell, null);
            shatter(cell);

        }
    }

    public void apply(Hero hero) {
        shatter(hero.pos);
    }

    public void shatter(int cell) {
        if (Dungeon.visible[cell]) {
            GLog.i(Messages.get(Potion.class, "shatter"));
            Sample.INSTANCE.play(Assets.SND_SHATTER);
            splash(cell);
        }
    }

    @Override
    public void cast(final Hero user, int dst) {
        super.cast(user, dst);
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    public void setKnown() {
        if (!ownedByFruit) {
            if (!isKnown()) {
                handler.know(this);
                updateQuickslot();
            }

            if (Dungeon.hero.isAlive()) {
                Catalog.setSeen(getClass());
            }

            Badges.validateAllPotionsIdentified();
        }
    }

    @Override
    public Item identify() {
        setKnown();
        return this;
    }

    @Override
    public String name() {
        return isKnown() ? super.name() : Messages.get(Potion.class, color);
    }

    @Override
    public String info() {
        return isKnown() ?
                desc() :
                Messages.get(Potion.class, "unknown_desc");
    }

    public Integer initials() {
        return isKnown() ? initials : null;
    }

    @Override
    public boolean isIdentified() {
        return isKnown();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    protected void splash(int cell) {
        final int color = ItemSprite.pick(image, 8, 10);
        Splash.at(cell, color, 5);

        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        if (fire != null)
            fire.clear(cell);


        Char ch = Actor.findChar(cell);
        if (ch != null) {
            Buff.detach(ch, Burning.class);
            Buff.detach(ch, Ooze.class);
            Splash.at( ch.sprite.center(), color, 5 );
        } else {
            Splash.at( cell, color, 5 );
        }
    }

    @Override
    public int price() {
        return 30 * quantity;
    }
}
