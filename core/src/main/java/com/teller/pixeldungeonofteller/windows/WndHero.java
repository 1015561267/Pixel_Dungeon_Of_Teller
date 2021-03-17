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

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.Statistics;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.sprites.HeroSprite;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Button;

import java.text.DecimalFormat;
import java.util.Locale;

public class WndHero extends WndTabbed {

    private static final int WIDTH = 115;

    private StatsTab stats;
    private BuffsTab buffs;

    private SmartTexture icons;
    private TextureFilm film;

    public WndHero() {

        super();

        icons = TextureCache.get(Assets.BUFFS_LARGE);
        film = new TextureFilm(icons, 16, 16);

        stats = new StatsTab();
        add(stats);

        buffs = new BuffsTab();
        add(buffs);

        add(new LabeledTab(Messages.get(this, "stats")) {
            protected void select(boolean value) {
                super.select(value);
                stats.visible = stats.active = selected;
            }

        });
        add(new LabeledTab(Messages.get(this, "buffs")) {
            protected void select(boolean value) {
                super.select(value);
                buffs.visible = buffs.active = selected;
            }

        });

        resize(WIDTH, (int) Math.max(stats.height(), buffs.height()));

        layoutTabs();

        select(0);
    }

    private class StatsTab extends Group {

        private static final int GAP = 5;

        private float pos;

        public StatsTab() {

            Hero hero = Dungeon.hero;

            IconTitle title = new IconTitle();
            title.icon(HeroSprite.avatar(hero.heroClass, hero.tier()));
            if (hero.givenName().equals(hero.className()))
                title.label(Messages.get(this, "title", hero.lvl, hero.className()).toUpperCase(Locale.ENGLISH));
            else
                title.label((hero.givenName() + "\n" + Messages.get(this, "title", hero.lvl, hero.className())).toUpperCase(Locale.ENGLISH));
            title.color(Window.SHPX_COLOR);
            title.setRect(0, 0, WIDTH, 0);
            add(title);

            pos = title.bottom() + 2 * GAP;

            statSlot(Messages.get(this, "str"), hero.STR());
            statSlot(Messages.get(this, "dex"), hero.DEX());
            statSlot(Messages.get(this, "int"), hero.INT());

            if (hero.belongings.armor != null) {
                if (hero.belongings.armor.levelKnown) {
                    statSlot(Messages.get(this, "armor"), hero.ARMOR);
                    statSlot(Messages.get(this, "shield"), hero.SHLD);
                } else {
                    if (hero.ARMOR != 0) {
                        statSlot(Messages.get(this, "armor"), "???");
                    } else {
                        statSlot(Messages.get(this, "armor"), 0);
                    }
                    if (hero.SHLD != 0) {
                        statSlot(Messages.get(this, "armor"), "???");
                    } else {
                        statSlot(Messages.get(this, "armor"), 0);
                    }
                }
            } else {
                statSlot(Messages.get(this, "armor"), 0);
                statSlot(Messages.get(this, "shield"), 0);
            }

            statSlot(Messages.get(this, "health"), (hero.HP) + "/" + hero.HT);

            statSlot(Messages.get(this, "mana"), (hero.MANA) + "/" + hero.MANACAP);

            statSlot(Messages.get(this, "exp"), hero.exp + "/" + hero.maxExp());

            if(hero.buff(Noise.class)!=null)
            {
                statSlot(Messages.get(this, "noise"), hero.buff(Noise.class).getNoise());
            }

            statSlot(Messages.get(this, "stealth"), hero.stealthLevel());

            pos += GAP;

            statSlot(Messages.get(this, "gold"), Statistics.goldCollected);
            statSlot(Messages.get(this, "depth"), Statistics.deepestFloor);

            pos += GAP;
        }

        private void statSlot(String label, String value) {

            RenderedText txt = PixelScene.renderText(label, 8);
            txt.y = pos;
            add(txt);

            txt = PixelScene.renderText(value, 8);
            txt.x = WIDTH * 0.6f;
            txt.y = pos;
            PixelScene.align(txt);
            add(txt);

            pos += GAP + txt.baseLine();
        }

        private void statSlot(String label, int value) {
            statSlot(label, Integer.toString(value));
        }

        public float height() {
            return pos;
        }
    }

    private class BuffsTab extends Group {

        private static final int GAP = 2;

        private float pos;

        public BuffsTab() {
            for (Buff buff : Dungeon.hero.buffs()) {
                if (buff.icon() != BuffIndicator.NONE) {
                    BuffSlot slot = new BuffSlot(buff);
                    slot.setRect(0, pos, WIDTH, slot.icon.height());
                    add(slot);
                    pos += GAP + slot.height();
                }
            }
        }

        public float height() {
            return pos;
        }

        private class BuffSlot extends Button {

            Image icon;
            RenderedText txt;
            private Buff buff;

            public BuffSlot(Buff buff) {
                super();
                this.buff = buff;
                int index = buff.icon();

                icon = new Image(icons);
                icon.frame(film.get(index));
                icon.y = this.y;
                add(icon);

                txt = PixelScene.renderText(buff.toString(), 8);
                txt.x = icon.width + GAP;
                txt.y = this.y + (int) (icon.height - txt.baseLine()) / 2;
                add(txt);

            }

            @Override
            protected void layout() {
                super.layout();
                icon.y = this.y;
                txt.x = icon.width + GAP;
                txt.y = pos + (int) (icon.height - txt.baseLine()) / 2;
            }

            @Override
            protected void onClick() {
                GameScene.show(new WndInfoBuff(buff));
            }
        }
    }
}
