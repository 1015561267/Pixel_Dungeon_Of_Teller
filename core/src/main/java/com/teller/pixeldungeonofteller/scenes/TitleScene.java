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
package com.teller.pixeldungeonofteller.scenes;

import android.opengl.GLES20;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.effects.BannerSprites;
import com.teller.pixeldungeonofteller.effects.Fireball;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.Archs;
import com.teller.pixeldungeonofteller.ui.ExitButton;
import com.teller.pixeldungeonofteller.ui.LanguageButton;
import com.teller.pixeldungeonofteller.ui.PrefsButton;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

import javax.microedition.khronos.opengles.GL10;

public class TitleScene extends PixelScene {

    @Override
    public void create() {

        super.create();

        Music.INSTANCE.play(Assets.THEME, true);
        Music.INSTANCE.volume(PixelDungeonOfTeller.musicVol() / 10f);

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        Image title = BannerSprites.get(BannerSprites.Type.PIXEL_DUNGEON);
        add(title);

        float topRegion = Math.max(105f, h * 0.45f);

        title.x = (w - title.width()) / 2f;
        if (PixelDungeonOfTeller.landscape())
            title.y = (topRegion - title.height()) / 2f;
        else
            title.y = 16 + (topRegion - title.height() - 16) / 2f;

        align(title);

        placeTorch(title.x + 22, title.y + 46);
        placeTorch(title.x + title.width - 22, title.y + 46);

        Image signs = new Image(BannerSprites.get(BannerSprites.Type.PIXEL_DUNGEON_SIGNS)) {
            private float time = 0;

            @Override
            public void update() {
                super.update();
                am = (float) Math.sin(-(time += Game.elapsed));
            }

            @Override
            public void draw() {
                GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
                super.draw();
                GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            }
        };
        signs.x = title.x + (title.width() - signs.width()) / 2f;
        signs.y = title.y;
        add(signs);

        /*DashboardItem btnBadges = new DashboardItem( Messages.get(this, "badges"), 3 ) {
            @Override
            protected void onClick() {
                PixelDungeonOfTeller.switchNoFade( BadgesScene.class );
            }
        };
        add(btnBadges);

        DashboardItem btnAbout = new DashboardItem( Messages.get(this, "about"), 1 ) {
            @Override
            protected void onClick() {
                PixelDungeonOfTeller.switchNoFade( AboutScene.class );
            }
        };
        add( btnAbout );

        DashboardItem btnPlay = new DashboardItem( Messages.get(this, "play"), 0 ) {
            @Override
            protected void onClick() {
                PixelDungeonOfTeller.switchNoFade( StartScene.class );
            }
        };
        add( btnPlay );

        DashboardItem btnRankings = new DashboardItem( Messages.get(this, "rankings"), 2 ) {
            @Override
            protected void onClick() {
                PixelDungeonOfTeller.switchNoFade( RankingsScene.class );
            }
        };
        add( btnRankings );*/

        DashboardItem[] btnsMain = new DashboardItem[]{
                new DashboardItem(Messages.get(this, "play"), 0) {
                    @Override
                    protected void onClick() {
                        PixelDungeonOfTeller.switchNoFade(StartScene.class);

                    }
                },
                new DashboardItem(Messages.get(this, "rankings"), 2) {
                    @Override
                    protected void onClick() {
                        PixelDungeonOfTeller.switchNoFade(RankingsScene.class);
                    }
                },
                new DashboardItem(Messages.get(this, "guide"), 4) {
                    @Override
                    protected void onClick() {
                        PixelDungeonOfTeller.switchNoFade(GuideScene.class);
                    }
                },
                new DashboardItem(Messages.get(this, "badges"), 3) {
                    @Override
                    protected void onClick() {
                        PixelDungeonOfTeller.switchNoFade(BadgesScene.class);
                    }
                },
                new DashboardItem(Messages.get(this, "about"), 1) {
                    @Override
                    protected void onClick() {
                        PixelDungeonOfTeller.switchNoFade(AboutScene.class);
                    }
                },
                new DashboardItem(Messages.get(this, "update"), 5) {
                    @Override
                    protected void onClick() {
                        PixelDungeonOfTeller.switchNoFade(ChangesScene.class);
                    }
                },
        };

        {
            for (DashboardItem btn : btnsMain) add(btn);
            final float btnHeight = btnsMain[0].height();
            final float btnWidth = btnsMain[0].width();
            if (PixelDungeonOfTeller.landscape()) {
                int cols = 3;
                int rows = 2;
                final float btnGapX = (w - btnWidth * cols) / (cols + 1);
                final float btnGapY = 1f;
                for (int i = 0; i < btnsMain.length; ++i) {
                    int r = i / cols;
                    int c = i % cols;
                    btnsMain[i].setPos(btnGapX + (btnWidth + btnGapX) * c,
                            topRegion + 2 + (btnHeight + btnGapY) * r);
                }
            } else {
                int cols = 2;
                int rows = 3;
                final float btnGapX = (w - btnWidth * cols) / (cols + 1);
                final float btnGapY = (h - btnHeight * rows) / (rows + 5);
                for (int i = 0; i < btnsMain.length; ++i) {
                    int r = i % rows;
                    int c = i / rows;
                    btnsMain[i].setPos(btnGapX + (btnWidth + btnGapX) * c,
                            topRegion + 2 + (btnHeight + btnGapY) * r);
                }
            }
        }
        BitmapText version = new BitmapText("v " + Game.version + "", pixelFont);
        version.measure();
        version.hardlight(0xCCCCCC);
        version.x = w - version.width();
        version.y = h - version.height();
        add(version);
        //Button changes = new ChangesButton();
        //changes.setPos( w-changes.width(), h - version.height() - changes.height());
        //add( changes );
        PrefsButton btnPrefs = new PrefsButton();
        btnPrefs.setPos(0, 0);
        add(btnPrefs);
        LanguageButton btnLang = new LanguageButton();
        btnLang.setPos(16, 1);
        add(btnLang);
        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);
        fadeIn();
    }

    private void placeTorch(float x, float y) {
        Fireball fb = new Fireball();
        fb.setPos(x, y);
        add(fb);
    }

    private static class DashboardItem extends Button {

        public static final float SIZE = 48;

        private static final int IMAGE_SIZE = 16;

        private Image image;
        private RenderedText label;

        public DashboardItem(String text, int index) {
            super();

            image.frame(image.texture.uvRect(index * IMAGE_SIZE, 0, (index + 1) * IMAGE_SIZE, IMAGE_SIZE));
            this.label.text(text);

            setSize(SIZE, SIZE / 2);
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            image = new Image(Assets.DASHBOARD);
            add(image);

            label = renderText(8);
            add(label);
        }

        @Override
        protected void layout() {
            super.layout();

            image.x = x + (width - image.width()) / 2;
            image.y = y;
            align(image);

            label.x = x + (width - label.width()) / 2;
            label.y = image.y + image.height() + 2;
            align(label);
        }

        @Override
        protected void onTouchDown() {
            image.brightness(1.5f);
            Sample.INSTANCE.play(Assets.SND_CLICK, 1, 1, 0.8f);
        }

        @Override
        protected void onTouchUp() {
            image.resetColor();
        }
    }
}
