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
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.windows.WndInfoBuff;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.SparseArray;

public class BuffIndicator extends Component {

    public static final int NONE = -1;

    //TODO consider creating an enum to store both index, and tint. Saves making separate images for color differences.

    //Reorganize order along with improvement of hunger and resting in 2021/5/12-13

    public static final int SATIATED = 0;
    public static final int PARTIAL = 1;
    public static final int HUNGER = 2;
    public static final int STARVING = 3;
    public static final int RAVENOUS = 4;

    public static final int GUARD = 5;
    public static final int COMBINATIONREADY = 6;
    public static final int COMBINATIONCOOLDOWN = 7;


    //Unused
    public static final int RAGE = 8;
    public static final int SACRIFICE = 9;


    //Positive

    public static final int MIND_VISION = 16;
    public static final int LEVITATION = 17;
    public static final int HEALING = 18;
    public static final int ARMOR = 19;
    public static final int INVISIBLE = 20;
    public static final int SHADOWS = 21;
    public static final int BARKSKIN = 22;
    public static final int IMMUNITY = 23;
    public static final int RECHARGING = 24;
    public static final int LIGHT = 25;
    public static final int BLESS = 26;

    //Negative
    public static final int FIRE = 32;
    public static final int FROST = 33;
    public static final int POISON = 34;
    public static final int PARALYSIS = 35;
    public static final int BLEEDING = 36;
    public static final int CRIPPLE = 37;
    public static final int WEAKNESS = 38;
    public static final int VERTIGO = 39;
    public static final int ROOTS = 40;
    public static final int BLINDNESS = 41;
    public static final int SLOW = 42;
    public static final int OOZE = 43;
    public static final int AMOK = 44;
    public static final int TERROR = 45;
    public static final int HEART = 46;
    public static final int CORRUPT = 47;
    public static final int CURSEDFLAME = 48;


    //By ability
    public static final int FURY = 56;
    public static final int ANGERED = 57;
    public static final int EXHAUSTED = 58;
    public static final int RECOVERING = 59;
    public static final int MARK = 60;
    public static final int COMBO = 61;


    //By equipment
    public static final int DEFERRED = 64;
    public static final int THORNS = 65;
    public static final int FORESIGHT = 66;


    //Unable to be included,or Neutral
    public static final int DROWSY = 72;
    public static final int MAGIC_SLEEP = 73;
    public static final int LOCKED_FLOOR = 74;


    //My work final,may move them above later
    public static final int APPTIZING = 80;
    public static final int FLASHOVERLOAD = 81;
    public static final int HOLYHEALING = 82;

    public static final int SIZE = 7;

    private static BuffIndicator heroInstance;

    private SmartTexture texture;
    private TextureFilm film;

    private SparseArray<BuffIcon> icons = new SparseArray<BuffIcon>();

    private Char ch;

    public BuffIndicator(Char ch) {
        super();

        this.ch = ch;
        if (ch == Dungeon.hero) {
            heroInstance = this;
        }
    }

    public static void refreshHero() {
        if (heroInstance != null) {
            heroInstance.layout();
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        if (this == heroInstance) {
            heroInstance = null;
        }
    }

    @Override
    protected void createChildren() {
        texture = TextureCache.get(Assets.BUFFS_SMALL);
        film = new TextureFilm(texture, SIZE, SIZE);
    }

    @Override
    protected void layout() {
        clear();

        SparseArray<BuffIcon> newIcons = new SparseArray<BuffIcon>();

        for (Buff buff : ch.buffs()) {
            if (buff.icon() != NONE) {
                BuffIcon icon = new BuffIcon(buff);
                icon.setRect(x + members.size() * (SIZE + 2), y, 9, 12);
                add(icon);
                newIcons.put(buff.icon(), icon);
            }
        }

        for (Integer key : icons.keyArray()) {
            if (newIcons.get(key) == null) {
                Image icon = icons.get(key).icon;
                icon.origin.set(SIZE / 2);
                add(icon);
                add(new AlphaTweener(icon, 0, 0.6f) {
                    @Override
                    protected void updateValues(float progress) {
                        super.updateValues(progress);
                        image.scale.set(1 + 5 * progress);
                    }

                    @Override
                    protected void onComplete() {
                        image.killAndErase();
                    }
                });
            }
        }

        icons = newIcons;
    }

    private class BuffIcon extends Button {

        public Image icon;
        private Buff buff;

        public BuffIcon(Buff buff) {
            super();
            this.buff = buff;

            icon = new Image(texture);
            icon.frame(film.get(buff.icon()));
            add(icon);
        }

        @Override
        protected void layout() {
            super.layout();
            icon.x = this.x + 1;
            icon.y = this.y + 2;
        }

        @Override
        protected void onClick() {
            if (buff.icon() != NONE)
                GameScene.show(new WndInfoBuff(buff));
        }
    }
}
