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
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.ui.CheckBox;
import com.teller.pixeldungeonofteller.ui.OptionSlider;
import com.teller.pixeldungeonofteller.ui.RedButton;
import com.teller.pixeldungeonofteller.ui.Toolbar;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

public class WndSettings extends WndTabbed {

    private static final int WIDTH = 112;
    private static final int HEIGHT = 124;
    private static final int SLIDER_HEIGHT = 25;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP_TINY = 2;
    private static final int GAP_SML = 5;
    private static final int GAP_LRG = 12;
    private static int last_index = 0;
    private ScreenTab screen;
    private UITab ui;
    private AudioTab audio;

    public WndSettings() {
        super();

        screen = new ScreenTab();
        add(screen);

        ui = new UITab();
        add(ui);

        audio = new AudioTab();
        add(audio);

        add(new LabeledTab(Messages.get(this, "screen")) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                screen.visible = screen.active = value;
                if (value) last_index = 0;
            }
        });

        add(new LabeledTab(Messages.get(this, "ui")) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                ui.visible = ui.active = value;
                if (value) last_index = 1;
            }
        });

        add(new LabeledTab(Messages.get(this, "audio")) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                audio.visible = audio.active = value;
                if (value) last_index = 2;
            }
        });

        resize(WIDTH, HEIGHT);

        layoutTabs();

        select(last_index);

    }

    private class ScreenTab extends Group {

        public ScreenTab() {
            super();

            OptionSlider scale = new OptionSlider(Messages.get(this, "scale"),
                    (int) Math.ceil(2 * Game.density) + "X",
                    PixelScene.maxDefaultZoom + "X",
                    (int) Math.ceil(2 * Game.density),
                    PixelScene.maxDefaultZoom) {
                @Override
                protected void onChange() {
                    if (getSelectedValue() != PixelDungeonOfTeller.scale()) {
                        PixelDungeonOfTeller.scale(getSelectedValue());
                        PixelDungeonOfTeller.switchNoFade((Class<? extends PixelScene>) PixelDungeonOfTeller.scene().getClass(), new Game.SceneChangeCallback() {
                            @Override
                            public void beforeCreate() {
                                //do nothing
                            }

                            @Override
                            public void afterCreate() {
                                Game.scene().add(new WndSettings());
                            }
                        });
                    }
                }
            };
            scale.setSelectedValue(PixelScene.defaultZoom);
            scale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
            if ((int) Math.ceil(2 * Game.density) < PixelScene.maxDefaultZoom)
                add(scale);

            OptionSlider brightness = new OptionSlider(Messages.get(this, "brightness"),
                    Messages.get(this, "dark"), Messages.get(this, "bright"), -2, 2) {
                @Override
                protected void onChange() {
                    PixelDungeonOfTeller.brightness(getSelectedValue());
                }
            };
            brightness.setSelectedValue(PixelDungeonOfTeller.brightness());
            brightness.setRect(0, scale.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
            add(brightness);

            CheckBox chkImmersive = new CheckBox(Messages.get(this, "soft_keys")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PixelDungeonOfTeller.immerse(checked());
                }
            };
            chkImmersive.setRect(0, brightness.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
            chkImmersive.checked(PixelDungeonOfTeller.immersed());
            chkImmersive.enable(android.os.Build.VERSION.SDK_INT >= 19);
            add(chkImmersive);

            CheckBox chkSaver = new CheckBox(Messages.get(this, "saver")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    if (checked()) {
                        checked(!checked());
                        PixelDungeonOfTeller.scene().add(new WndOptions(
                                Messages.get(ScreenTab.class, "saver"),
                                Messages.get(ScreenTab.class, "saver_desc"),
                                Messages.get(ScreenTab.class, "okay"),
                                Messages.get(ScreenTab.class, "cancel")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    checked(!checked());
                                    PixelDungeonOfTeller.powerSaver(checked());
                                }
                            }
                        });
                    } else {
                        PixelDungeonOfTeller.powerSaver(checked());
                    }
                }
            };
            chkSaver.setRect(0, chkImmersive.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
            chkSaver.checked(PixelDungeonOfTeller.powerSaver());
            if (PixelScene.maxScreenZoom >= 2) add(chkSaver);

            RedButton btnOrientation = new RedButton(PixelDungeonOfTeller.landscape() ?
                    Messages.get(this, "portrait")
                    : Messages.get(this, "landscape")) {
                @Override
                protected void onClick() {
                    PixelDungeonOfTeller.landscape(!PixelDungeonOfTeller.landscape());
                }
            };
            btnOrientation.setRect(0, chkSaver.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
            add(btnOrientation);
        }
    }

    private class UITab extends Group {

        public UITab() {
            super();

            RenderedText barDesc = PixelScene.renderText(Messages.get(this, "mode"), 9);
            barDesc.x = (WIDTH - barDesc.width()) / 2;
            PixelScene.align(barDesc);
            add(barDesc);

            RedButton btnSplit = new RedButton(Messages.get(this, "split")) {
                @Override
                protected void onClick() {
                    PixelDungeonOfTeller.toolbarMode(Toolbar.Mode.SPLIT.name());
                    Toolbar.updateLayout();
                }
            };
            btnSplit.setRect(1, barDesc.y + barDesc.baseLine() + GAP_TINY, 36, 16);
            add(btnSplit);

            RedButton btnGrouped = new RedButton(Messages.get(this, "group")) {
                @Override
                protected void onClick() {
                    PixelDungeonOfTeller.toolbarMode(Toolbar.Mode.GROUP.name());
                    Toolbar.updateLayout();
                }
            };
            btnGrouped.setRect(btnSplit.right() + 1, barDesc.y + barDesc.baseLine() + GAP_TINY, 36, 16);
            add(btnGrouped);

            RedButton btnCentered = new RedButton(Messages.get(this, "center")) {
                @Override
                protected void onClick() {
                    PixelDungeonOfTeller.toolbarMode(Toolbar.Mode.CENTER.name());
                    Toolbar.updateLayout();
                }
            };
            btnCentered.setRect(btnGrouped.right() + 1, barDesc.y + barDesc.baseLine() + GAP_TINY, 36, 16);
            add(btnCentered);

            CheckBox chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PixelDungeonOfTeller.flipToolbar(checked());
                    Toolbar.updateLayout();
                }
            };
            chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
            chkFlipToolbar.checked(PixelDungeonOfTeller.flipToolbar());
            add(chkFlipToolbar);

            final CheckBox chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PixelDungeonOfTeller.flipTags(checked());
                    GameScene.layoutTags();
                }
            };
            chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
            chkFlipTags.checked(PixelDungeonOfTeller.flipTags());
            add(chkFlipTags);

            OptionSlider slots = new OptionSlider(Messages.get(this, "quickslots"), "0", "4", 0, 4) {
                @Override
                protected void onChange() {
                    PixelDungeonOfTeller.quickSlots(getSelectedValue());
                    Toolbar.updateLayout();
                }
            };
            slots.setSelectedValue(PixelDungeonOfTeller.quickSlots());
            slots.setRect(0, chkFlipTags.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
            add(slots);

            CheckBox chkFont = new CheckBox(Messages.get(this, "system_font")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PixelDungeonOfTeller.switchNoFade((Class<? extends PixelScene>) PixelDungeonOfTeller.scene().getClass(), new Game.SceneChangeCallback() {
                        @Override
                        public void beforeCreate() {
                            PixelDungeonOfTeller.classicFont(!checked());
                        }

                        @Override
                        public void afterCreate() {
                            Game.scene().add(new WndSettings());
                        }
                    });
                }
            };
            chkFont.setRect(0, slots.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
            chkFont.checked(!PixelDungeonOfTeller.classicFont());
            add(chkFont);
        }

    }

    private class AudioTab extends Group {

        public AudioTab() {
            OptionSlider musicVol = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    Music.INSTANCE.volume(getSelectedValue() / 10f);
                    PixelDungeonOfTeller.musicVol(getSelectedValue());
                }
            };
            musicVol.setSelectedValue(PixelDungeonOfTeller.musicVol());
            musicVol.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
            add(musicVol);

            CheckBox musicMute = new CheckBox(Messages.get(this, "music_mute")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PixelDungeonOfTeller.music(!checked());
                }
            };
            musicMute.setRect(0, musicVol.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
            musicMute.checked(!PixelDungeonOfTeller.music());
            add(musicMute);


            OptionSlider SFXVol = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    Sample.INSTANCE.volume(getSelectedValue() / 10f);
                    PixelDungeonOfTeller.SFXVol(getSelectedValue());
                }
            };
            SFXVol.setSelectedValue(PixelDungeonOfTeller.SFXVol());
            SFXVol.setRect(0, musicMute.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
            add(SFXVol);

            CheckBox btnSound = new CheckBox(Messages.get(this, "sfx_mute")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PixelDungeonOfTeller.soundFx(!checked());
                    Sample.INSTANCE.play(Assets.SND_CLICK);
                }
            };
            btnSound.setRect(0, SFXVol.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
            btnSound.checked(!PixelDungeonOfTeller.soundFx());
            add(btnSound);

            resize(WIDTH, (int) btnSound.bottom());
        }

    }
}
