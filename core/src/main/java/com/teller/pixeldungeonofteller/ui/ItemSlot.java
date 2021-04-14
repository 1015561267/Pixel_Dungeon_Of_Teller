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
package com.teller.pixeldungeonofteller.ui;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.DewVial;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.artifacts.Artifact;
import com.teller.pixeldungeonofteller.items.keys.Key;
import com.teller.pixeldungeonofteller.items.keys.SkeletonKey;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.wands.Wand;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.melee.MeleeWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.Flintlock;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.SubmachineGun;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.MagesStaff;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.JavelinBarrel;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;

public class ItemSlot extends Button {

    public static final int DEGRADED = 0xFF4444;
    public static final int UPGRADED = 0x44FF44;
    public static final int FADED = 0x999999;
    public static final int WARNING = 0xFF8800;
    // Special "virtual items"
    public static final Item CHEST = new Item() {
        public int image() {
            return ItemSpriteSheet.CHEST;
        }

    };
    public static final Item LOCKED_CHEST = new Item() {
        public int image() {
            return ItemSpriteSheet.LOCKED_CHEST;
        }

    };
    public static final Item CRYSTAL_CHEST = new Item() {
        public int image() {
            return ItemSpriteSheet.CRYSTAL_CHEST;
        }

    };
    public static final Item TOMB = new Item() {
        public int image() {
            return ItemSpriteSheet.TOMB;
        }

    };
    public static final Item SKELETON = new Item() {
        public int image() {
            return ItemSpriteSheet.BONES;
        }

    };
    public static final Item REMAINS = new Item() {
        public int image() {
            return ItemSpriteSheet.REMAINS;
        }

    };
    private static final float ENABLED = 1.0f;
    private static final float DISABLED = 0.3f;
    private static final String TXT_STRENGTH = ":%d";
    private static final String TXT_DEXTERITY = ":%d";
    private static final String TXT_TYPICAL_STR = "%d?";
    private static final String TXT_KEY_DEPTH = "\u007F%d";
    private static final String TXT_LEVEL = "%+d";
    private static final String TXT_CURSED = "";//"-";
    protected ItemSprite icon;
    protected Item item;
    protected BitmapText topLeft;
    protected BitmapText topRight;
    protected BitmapText bottomRight;
    protected BitmapText bottomLeft;
    protected Image bottomRightIcon;
    protected boolean iconVisible = true;

    public ItemSlot() {
        super();
        icon.visible(false);
        enable(false);
    }

    public ItemSlot(Item item) {
        this();
        item(item);
    }

    @Override
    protected void createChildren() {

        super.createChildren();

        icon = new ItemSprite();
        add(icon);

        topLeft = new BitmapText(PixelScene.pixelFont);
        add(topLeft);

        topRight = new BitmapText(PixelScene.pixelFont);
        add(topRight);

        bottomRight = new BitmapText(PixelScene.pixelFont);
        add(bottomRight);

        bottomLeft = new BitmapText( PixelScene.font1x );
        add(bottomLeft);
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = x + (width - icon.width) / 2;
        icon.y = y + (height - icon.height) / 2;

        if (topLeft != null) {
            topLeft.x = x;
            topLeft.y = y;
        }

        if (topRight != null) {
            topRight.x = x + (width - topRight.width());
            topRight.y = y;
        }

        if (bottomRight != null) {
            bottomRight.x = x + (width - bottomRight.width());
            bottomRight.y = y + (height - bottomRight.height());
        }

        if (bottomRightIcon != null) {
            bottomRightIcon.x = x + (width - bottomRightIcon.width()) - 1;
            bottomRightIcon.y = y + (height - bottomRightIcon.height());
        }
    }

    public void item(Item item) {
        if (this.item == item) {
            if (item != null) icon.frame(item.image());
                updateText(); return;
        }

        this.item = item;

        if (item == null) {
            enable(false);
            icon.visible(false);
            updateText();

        } else {
            enable(true);
            icon.visible(true);
                icon.view(item);
            updateText();
        }
    }

    private void updateText() {
        if (bottomRightIcon != null) {
            remove(bottomRightIcon);
            bottomRightIcon = null;
        }

        if (item == null) {
            topLeft.visible = topRight.visible = bottomRight.visible = false;
            return;
        } else {
            topLeft.visible = topRight.visible = bottomRight.visible = true;
        }

        if (item instanceof NinjaProsthesis || item instanceof JavelinBarrel) {
            topLeft.visible = bottomRight.visible = true;

            if (!item.levelKnown) {
                int dex = ((Weapon) item).DEXReq(0);
                topLeft.text(Messages.format(TXT_TYPICAL_STR, dex));
                topLeft.hardlight(WARNING);
            } else {
                int dex = ((Weapon) item).DEXReq();
                topLeft.text(Messages.format(TXT_DEXTERITY, dex));
                if (dex > Dungeon.hero.DEX()) {
                    topLeft.hardlight(DEGRADED);
                } else {
                    topLeft.resetColor();
                }
                topLeft.measure();
                bottomRight.text(item.status());
                bottomRight.measure();
            }
            layout();
            return;
        }

        if(item instanceof MagicBook)
        {
            topLeft.visible = topRight.visible = bottomLeft.visible = bottomRight.visible = false;
            return;
        }

        if(item instanceof Flintlock)
        {
            if (!item.levelKnown) {
                int dex = ((Weapon) item).DEXReq(0);
                topLeft.text(Messages.format(TXT_TYPICAL_STR, dex));
                topLeft.hardlight(WARNING);
                int str= ((Weapon) item).STRReq(0);
                topLeft.text(Messages.format(TXT_TYPICAL_STR,str ));
                topLeft.hardlight(WARNING);
            }
            else
            {
                int dex = ((Weapon) item).DEXReq();
                topLeft.text(Messages.format(TXT_DEXTERITY, dex));
                if (dex > Dungeon.hero.DEX()) {
                    topLeft.hardlight(DEGRADED);
                } else {
                    topLeft.resetColor();
                }

                int str = ((Weapon) item).STRReq();
                topRight.text(Messages.format(TXT_STRENGTH, str));
                if (str > Dungeon.hero.STR()) {
                    topRight.hardlight(DEGRADED);
                } else {
                    topRight.resetColor();
                }
            }
            topLeft.measure();
            topRight.measure();

            bottomRight.visible=true;
            bottomRight.text(item.status());
            bottomRight.measure();
            bottomRight.resetColor();
            layout();
            return;
        }

        if(item instanceof SubmachineGun)
        {
            if (!item.levelKnown) {
                int dex = ((Weapon) item).DEXReq(0);
                topLeft.text(Messages.format(TXT_TYPICAL_STR, dex));
                topLeft.hardlight(WARNING);
            }
            else
            {
                int dex = ((Weapon) item).DEXReq();
                topLeft.text(Messages.format(TXT_DEXTERITY, dex));
                if (dex > Dungeon.hero.DEX()) {
                    topLeft.hardlight(DEGRADED);
                } else {
                    topLeft.resetColor();
                }
            }
            topLeft.measure();
            topRight.measure();
            bottomRight.visible=true;
            bottomRight.text(item.status());
            bottomRight.measure();
            bottomRight.resetColor();
            layout();
            return;
        }
        boolean isArmor = item instanceof Armor;
        boolean isWeapon = item instanceof Weapon;
        if (isArmor || isWeapon) {
            if(isArmor)
            {
                if (!item.levelKnown) {
                    topRight.text(Messages.format(TXT_TYPICAL_STR, isArmor ?
                            ((Armor) item).STRReq(0) :
                            ((Weapon) item).STRReq(0)));
                    topRight.hardlight(WARNING);
                }
                else {
                    int str = isArmor ? ((Armor) item).STRReq() : ((Weapon) item).STRReq();
                    topRight.text(Messages.format(TXT_STRENGTH, str));
                    if (str > Dungeon.hero.STR()) {
                        topRight.hardlight(DEGRADED);
                    } else {
                        topRight.resetColor();
                    }
                }
                topLeft.measure();
                topRight.measure();
                return;
            }
            else
            {
                boolean need_dex =   (((Weapon) item).WeaponType() == KindOfWeapon.Type.DualWield  ||
                        ((Weapon) item).WeaponType() == KindOfWeapon.Type.OffHand ||
                        ((Weapon) item).WeaponType() == KindOfWeapon.Type.Attached );

                boolean need_str = (((Weapon) item).WeaponType() == KindOfWeapon.Type.DualWield  ||
                        ((Weapon) item).WeaponType() == KindOfWeapon.Type.MainHand ||
                        ((Weapon) item).WeaponType() == KindOfWeapon.Type.TwoHanded||
                        ((Weapon) item).WeaponType() == KindOfWeapon.Type.Missile);

                    if (item.levelKnown) {
                        if(need_dex) {
                            int dex = ((Weapon) item).DEXReq();
                            topLeft.text(Messages.format(TXT_DEXTERITY, dex));
                            if (dex > Dungeon.hero.DEX()) {
                                topLeft.hardlight(DEGRADED);
                            } else {
                                topLeft.resetColor();
                            }
                            topLeft.measure();
                        }
                        if(need_str)
                        {
                            int str = ((Weapon) item).STRReq();
                            topRight.text(Messages.format(TXT_STRENGTH, str));
                            if (str > Dungeon.hero.STR()) {
                                topRight.hardlight(DEGRADED);
                            } else {
                                topRight.resetColor();
                            }
                            topRight.measure();
                        }
                    }
                    else
                    {
                        if(need_dex) {
                            int dex = ((Weapon) item).DEXReq(0);
                            topLeft.text(Messages.format(TXT_DEXTERITY, dex));
                            topLeft.hardlight(WARNING);
                            topLeft.measure();
                        }
                        if(need_str) {
                            int str = ((Weapon) item).STRReq(0);
                            topRight.text(Messages.format(TXT_TYPICAL_STR, str));
                            topRight.hardlight(WARNING);
                            topRight.measure();
                        }
                    }
                }
        }



        else if (item instanceof Key && !(item instanceof SkeletonKey)) {
            topRight.text(Messages.format(TXT_KEY_DEPTH, ((Key) item).depth));
            topRight.measure();
        } else {
            topRight.text(null);
        }
        int level = item.visiblyUpgraded();
        if (level != 0) {
            bottomRight.text(item.levelKnown ? Messages.format(TXT_LEVEL, level) : TXT_CURSED);
            bottomRight.measure();
            bottomRight.hardlight(level > 0 ? UPGRADED : DEGRADED);
        } else if (item instanceof Scroll || item instanceof Potion) {
            bottomRight.text(null);
            Integer iconInt;
            if (item instanceof Scroll) {
                iconInt = ((Scroll) item).initials();
            } else {
                iconInt = ((Potion) item).initials();
            }
            if (iconInt != null && iconVisible) {
                bottomRightIcon = new Image(Assets.CONS_ICONS);
                int left = iconInt * 7;
                int top = item instanceof Potion ? 0 : 8;
                bottomRightIcon.frame(left, top, 7, 8);
                add(bottomRightIcon);
            }
        } else {
            bottomRight.text(null);
        }
        if(item.stackable || item instanceof DewVial || item instanceof Wand || item instanceof MagesStaff) {
            topLeft.text(item.status());
            topLeft.measure();
        }

        if(item instanceof Artifact && item.status() != null)
        {
            topLeft.text(item.status());
            topLeft.measure();
        }
        layout();
    }

    public void enable(boolean value) {

        active = value;

        float alpha = value ? ENABLED : DISABLED;
        icon.alpha(alpha);
        topLeft.alpha(alpha);
        topRight.alpha(alpha);
        bottomRight.alpha(alpha);
        if (bottomRightIcon != null) bottomRightIcon.alpha(alpha);
    }

    public void showParams(boolean TL, boolean TR, boolean BR) {
        if (TL) add(topLeft);
        else remove(topLeft);

        if (TR) add(topRight);
        else remove(topRight);

        if (BR) add(bottomRight);
        else remove(bottomRight);
        iconVisible = BR;
    }

    public void setScale( float scale ) {
        topLeft.scale.scale(scale);
        topRight.scale.scale(scale);
        bottomRight.scale.scale(scale);
        bottomLeft.scale.scale(scale);
    }

    public void showStatus( boolean value ) {
        topLeft.visible = value;
        bottomLeft.visible = value;
    }
}
