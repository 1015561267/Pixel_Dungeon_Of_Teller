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
package com.teller.pixeldungeonofteller.windows;

import android.graphics.RectF;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.hero.Belongings;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.EquipableItem;
import com.teller.pixeldungeonofteller.items.Gold;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.bags.Bag;
import com.teller.pixeldungeonofteller.items.bags.PotionBandolier;
import com.teller.pixeldungeonofteller.items.bags.ScrollHolder;
import com.teller.pixeldungeonofteller.items.bags.SeedPouch;
import com.teller.pixeldungeonofteller.items.bags.WandHolster;
import com.teller.pixeldungeonofteller.items.food.Food;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.rings.RingOfMagic;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.wands.Wand;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.melee.MeleeWeapon;
import com.teller.pixeldungeonofteller.items.weapon.missiles.Boomerang;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tamahawk;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.plants.Plant.Seed;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.ui.Icons;
import com.teller.pixeldungeonofteller.ui.ItemSlot;
import com.teller.pixeldungeonofteller.ui.QuickSlotButton;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;

public class WndBag extends WndTabbed {

    protected static final int COLS_P = 5;
    //protected static final int COLS_P    = 4;
    //protected static final int COLS_L    = 6;
    protected static final int COLS_L = 6;

    protected static final int ROWS_P = 6;
    //protected static final int COLS_P    = 4;
    //protected static final int COLS_L    = 6;
    protected static final int ROWS_L = 5;

    //protected static final int SLOT_SIZE	= 28;
    protected static final int SLOT_SIZE = 21;
    protected static final int SLOT_MARGIN = 1;
    protected static final int TITLE_HEIGHT = 12;
    private static Mode lastMode;
    private static Bag lastBag;
    protected int count;
    protected int col;
    protected int row;
    private Listener listener;
    private WndBag.Mode mode;
    private String title;
    private int nCols;
    private int nRows;
    public WndBag(Bag bag, Listener listener, Mode mode, String title) {

        super();

        this.listener = listener;
        this.mode = mode;
        this.title = title;

        lastMode = mode;
        lastBag = bag;

        nCols = PixelDungeonOfTeller.landscape() ? COLS_L : COLS_P;
       // nRows = PixelDungeonOfTeller.landscape() ? COLS_P : COLS_L;
        //nRows = (Belongings.BACKPACK_SIZE + 8 + 1) / nCols + ((Belongings.BACKPACK_SIZE + 8 + 1) % nCols > 0 ? 1 : 0);
        //nRows = (int)Math.ceil((Belongings.BACKPACK_SIZE + 4) / (float)nCols);
        nRows=PixelDungeonOfTeller.landscape() ? ROWS_L : ROWS_P;


        int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
        int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);

        RenderedText txtTitle = PixelScene.renderText(title != null ? title : Messages.titleCase(bag.name()), 9);
        txtTitle.hardlight(TITLE_COLOR);
        txtTitle.x = (int) (slotsWidth - txtTitle.width()) / 2;
        txtTitle.y = (int) (TITLE_HEIGHT - txtTitle.height()) / 2;
        add(txtTitle);

        placeItems(bag);

        resize(slotsWidth, slotsHeight + TITLE_HEIGHT);

        Belongings stuff = Dungeon.hero.belongings;
        Bag[] bags = {
                stuff.backpack,
                stuff.getItem(SeedPouch.class),
                stuff.getItem(ScrollHolder.class),
                stuff.getItem(PotionBandolier.class),
                stuff.getItem(WandHolster.class)};

        for (Bag b : bags) {
            if (b != null) {
                BagTab tab = new BagTab(b);
                add(tab);
                tab.select(b == bag);
            }
        }

        layoutTabs();
    }

    public static WndBag lastBag(Listener listener, Mode mode, String title) {

        if (mode == lastMode && lastBag != null &&
                Dungeon.hero.belongings.backpack.contains(lastBag)) {

            return new WndBag(lastBag, listener, mode, title);

        } else {

            return new WndBag(Dungeon.hero.belongings.backpack, listener, mode, title);

        }
    }

    public static WndBag getBag(Class<? extends Bag> bagClass, Listener listener, Mode mode, String title) {
        Bag bag = Dungeon.hero.belongings.getItem(bagClass);
        return bag != null ?
                new WndBag(bag, listener, mode, title) :
                lastBag(listener, mode, title);
    }

    protected void placeItems(Bag container) {

        // Equipped items
        Belongings stuff = Dungeon.hero.belongings;
        placeItem(stuff.mainhandweapon != null ? stuff.mainhandweapon : new Placeholder(ItemSpriteSheet.WEAPON_HOLDER));
        placeItem(stuff.offhandweapon != null ? stuff.offhandweapon : new Placeholder(ItemSpriteSheet.OFFHANDWEAPON_HOLDER));
        placeItem(stuff.armor != null ? stuff.armor : new Placeholder(ItemSpriteSheet.ARMOR_HOLDER));
        placeItem(stuff.misc1 != null ? stuff.misc1 : new Placeholder(ItemSpriteSheet.RING_HOLDER));
        placeItem(stuff.misc2 != null ? stuff.misc2 : new Placeholder(ItemSpriteSheet.RING_HOLDER));

        boolean backpack = (container == Dungeon.hero.belongings.backpack);
        if (!backpack) {
            count = nCols;
            col = 0;
            row = 1;
        }

        // Items in the bag
        for (Item item : container.items) {
            placeItem(item);
        }

        // Free Space
        while (count - (backpack ? 8 : nCols) < container.size) {
            placeItem(null);
        }

        // Gold
        if (container == Dungeon.hero.belongings.backpack) {
            row = nRows - 1;
            col = nCols - 1;
            placeItem(new Gold(Dungeon.gold));
        }
    }

    protected void placeItem(final Item item) {

        int x = col * (SLOT_SIZE + SLOT_MARGIN);
        int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);

        add(new ItemButton(item).setPos(x, y));

        if (++col >= nCols) {
            col = 0;
            row++;
        }

        count++;
    }

    @Override
    public void onMenuPressed() {
        if (listener == null) {
            hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (listener != null) {
            listener.onSelect(null);
        }
        super.onBackPressed();
    }

    @Override
    protected void onClick(Tab tab) {
        hide();
        GameScene.show(new WndBag(((BagTab) tab).bag, listener, mode, title));
    }

    @Override
    protected int tabHeight() {
        return 20;
    }

    public enum Mode {
        ALL,
        UNIDENTIFED,
        UNIDED_OR_CURSED,
        UPGRADEABLE,
        QUICKSLOT,
        FOR_SALE,
        PAGE,
        WEAPON,
        ARMOR,
        MAGICBOOK,
        ENCHANTABLE,
        WAND,
        SEED,
        FOOD,
        POTION,
        SCROLL,
        EQUIPMENT
    }

    public interface Listener {
        void onSelect(Item item);
    }

    private static class Placeholder extends Item {
        {
            name = null;
        }

        public Placeholder(int image) {
            this.image = image;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }

        @Override
        public boolean isEquipped(Hero hero) {
            return true;
        }
    }

    private class BagTab extends Tab {

        private Image icon;

        private Bag bag;

        public BagTab(Bag bag) {
            super();

            this.bag = bag;

            icon = icon();
            add(icon);
        }

        @Override
        protected void select(boolean value) {
            super.select(value);
            icon.am = selected ? 1.0f : 0.6f;
        }

        @Override
        protected void layout() {
            super.layout();

            icon.copy(icon());
            icon.x = x + (width - icon.width) / 2;
            icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
            if (!selected && icon.y < y + CUT) {
                RectF frame = icon.frame();
                frame.top += (y + CUT - icon.y) / icon.texture.height;
                icon.frame(frame);
                icon.y = y + CUT;
            }
        }

        private Image icon() {
            if (bag instanceof SeedPouch) {
                return Icons.get(Icons.SEED_POUCH);
            } else if (bag instanceof ScrollHolder) {
                return Icons.get(Icons.SCROLL_HOLDER);
            } else if (bag instanceof WandHolster) {
                return Icons.get(Icons.WAND_HOLSTER);
            } else if (bag instanceof PotionBandolier) {
                return Icons.get(Icons.POTION_BANDOLIER);
            } else {
                return Icons.get(Icons.BACKPACK);
            }
        }
    }

    private class ItemButton extends ItemSlot {

        private static final int NORMAL = 0x9953564D;
        private static final int EQUIPPED = 0x9991938C;

        private Item item;
        private ColorBlock bg;

        public ItemButton(Item item) {

            super(item);

            this.item = item;
            if (item instanceof Gold) {
                bg.visible = false;
            }

            width = height = SLOT_SIZE;
        }

        @Override
        protected void createChildren() {
            bg = new ColorBlock(SLOT_SIZE, SLOT_SIZE, NORMAL);
            add(bg);

            super.createChildren();
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;

            super.layout();
        }

        @Override
        public void item(Item item) {

            super.item(item);
            if (item != null) {

                bg.texture(TextureCache.createSolid(item.isEquipped(Dungeon.hero) ? EQUIPPED : NORMAL));
                if (item.cursed && item.cursedKnown) {
                    bg.ra = +0.3f;
                    bg.ga = -0.15f;
                } else if (!item.isIdentified()) {
                    bg.ra = 0.2f;
                    bg.ba = 0.2f;
                }

                if (item.name() == null) {
                    enable(false);
                } else {
                    enable(
                            mode == Mode.FOR_SALE && (item.price() > 0) && (!item.isEquipped(Dungeon.hero) || !item.cursed) ||
                                    mode == Mode.UPGRADEABLE && item.isUpgradable() ||
                                    mode == Mode.UNIDENTIFED && !item.isIdentified() ||
                                    mode == Mode.UNIDED_OR_CURSED && ((item instanceof EquipableItem || item instanceof Wand) && (!item.isIdentified() || item.cursed)) ||
                                    mode == Mode.QUICKSLOT && (item.defaultAction != null && !(item instanceof Tamahawk)) ||
                                    mode == Mode.WEAPON && (item instanceof MeleeWeapon || item instanceof Boomerang) ||
                                    mode == Mode.ARMOR && (item instanceof Armor) ||
                                    mode == Mode.ENCHANTABLE && (item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Armor) ||
                                    mode == Mode.WAND && (item instanceof Wand) ||
                                    mode == Mode.MAGICBOOK  && (item instanceof MagicBook) ||
                                    mode == Mode.SEED && (item instanceof Seed) ||
                                    mode == Mode.FOOD && (item instanceof Food) ||
                                    mode == Mode.POTION && (item instanceof Potion) ||
                                    mode == Mode.SCROLL && (item instanceof Scroll) ||
                                    mode == Mode.PAGE && (item instanceof MagicPage) ||
                                    mode == Mode.EQUIPMENT && (item instanceof EquipableItem) ||
                                    mode == Mode.ALL
                    );
                    //extra logic for cursed weapons or armor
                    if (!active && mode == Mode.UNIDED_OR_CURSED) {
                        if (item instanceof Weapon) {
                            Weapon w = (Weapon) item;
                            enable(w.hasCurseEnchant());
                        }
                        if (item instanceof Armor) {
                            Armor a = (Armor) item;
                            enable(a.hasCurseGlyph());
                        }
                    }
                }
            } else {
                bg.color(NORMAL);
            }
        }

        @Override
        protected void onTouchDown() {
            bg.brightness(1.5f);
            Sample.INSTANCE.play(Assets.SND_CLICK, 0.7f, 0.7f, 1.2f);
        }

        protected void onTouchUp() {
            bg.brightness(1.0f);
        }

        @Override
        protected void onClick() {
            if (!lastBag.contains(item) && !item.isEquipped(Dungeon.hero)) {
                hide();
            } else if (listener != null) {
                hide();
                listener.onSelect(item);
            } else {
                GameScene.show(new WndItem(WndBag.this, item));
            }
        }

        @Override
        protected boolean onLongClick() {
            if (listener == null && item.defaultAction != null && !(item instanceof Tamahawk)) {
                hide();
                Dungeon.quickslot.setSlot(0, item);
                QuickSlotButton.refresh();
                return true;
            } else {
                return false;
            }
        }
    }
}
