package com.teller.pixeldungeonofteller.ui;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.ui.Button;

public class SpellSlot extends Button {

    private static final float ENABLED = 1.0f;
    private static final float DISABLED = 0.3f;

    protected CharSprite icon;

    public void setScale( float scale ) {

    }

    public SpellSlot() {
        super();
    }

    @Override
    protected void createChildren() {
        super.createChildren();
        icon = new CharSprite();
        add(icon);
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = x + (width - icon.width()) / 2 + 1;
        icon.y = y + (height - icon.height()) / 2;
    }

    public void setIcon(MagicBook mgbook)
    {
        if (icon != null) {
            icon.killAndErase();
            icon = null;
        }
        try {
            icon =mgbook.SpellSprite().newInstance();
            add(icon);
            icon.x = x + (width - icon.width()) / 2 + 1;
            icon.y = y + (height - icon.height()) / 2;
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
        }
    }

    public void enable(boolean value) {
        active = value;
        if (icon != null) {
            icon.alpha(value ? ENABLED : DISABLED);
        }
    }
}
