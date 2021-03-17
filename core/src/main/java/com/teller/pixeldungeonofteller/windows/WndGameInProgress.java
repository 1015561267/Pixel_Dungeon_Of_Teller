package com.teller.pixeldungeonofteller.windows;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.GamesInProgress;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.hero.HeroSubClass;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.InterlevelScene;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.scenes.StartScene;
import com.teller.pixeldungeonofteller.sprites.HeroSprite;
import com.teller.pixeldungeonofteller.ui.RedButton;
import com.teller.pixeldungeonofteller.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.utils.FileUtils;

import java.util.Locale;

public class WndGameInProgress extends Window {

    private static final int WIDTH    = 120;
    private static final int HEIGHT   = 120;

    private int GAP	  = 5;

    private float pos;

    public WndGameInProgress(final int slot){

        final GamesInProgress.Info info = GamesInProgress.check(slot);

        String className = null;
        if (info.subClass != HeroSubClass.NONE){
            className = info.subClass.title();
        } else {
            className = info.heroClass.title();
        }

        IconTitle title = new IconTitle();
        title.icon( HeroSprite.avatar(info.heroClass, info.armorTier) );
        title.label((Messages.get(this, "title", info.level, className)).toUpperCase(Locale.ENGLISH));
        title.color(Window.SHPX_COLOR);
        title.setRect( 0, 0, WIDTH, 0 );
        add(title);

        if (info.challenges > 0) GAP -= 2;

        pos = title.bottom() + GAP;

        if (info.challenges > 0) {
            RedButton btnChallenges = new RedButton( Messages.get(this, "challenges") ) {
                @Override
                protected void onClick() {
                    Game.scene().add( new WndChallenges( info.challenges, false ) );
                }
            };
            float btnW = btnChallenges.reqWidth() + 2;
            btnChallenges.setRect( (WIDTH - btnW)/2, pos, btnW , btnChallenges.reqHeight() + 2 );
            add( btnChallenges );

            pos = btnChallenges.bottom() + GAP;
        }

        pos += GAP;

        statSlot( Messages.get(this, "str"), info.str );
        if (info.shld > 0) statSlot( Messages.get(this, "health"), info.hp + "+" + info.shld + "/" + info.ht );
        else statSlot( Messages.get(this, "health"), (info.hp) + "/" + info.ht );
        statSlot( Messages.get(this, "exp"), info.exp + "/" + Hero.maxExp(info.level) );

        pos += GAP;
        statSlot( Messages.get(this, "gold"), info.goldCollected );
        statSlot( Messages.get(this, "depth"), info.maxDepth );

        pos += GAP;

        RedButton cont = new RedButton(Messages.get(this, "continue")){
            @Override
            protected void onClick() {
                super.onClick();

                GamesInProgress.curSlot = slot;

                Dungeon.hero = null;
                InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
                PixelDungeonOfTeller.switchScene(InterlevelScene.class);
            }
        };

        RedButton erase = new RedButton( Messages.get(this, "erase")){
            @Override
            protected void onClick() {
                super.onClick();

                PixelDungeonOfTeller.scene().add(new WndOptions(
                        Messages.get(WndGameInProgress.class, "erase_warn_title"),
                        Messages.get(WndGameInProgress.class, "erase_warn_body"),
                        Messages.get(WndGameInProgress.class, "erase_warn_yes"),
                        Messages.get(WndGameInProgress.class, "erase_warn_no") ) {
                    @Override
                    protected void onSelect( int index ) {
                        if (index == 0) {
                            FileUtils.deleteDir(GamesInProgress.gameFolder(slot));
                            GamesInProgress.setUnknown(slot);
                            PixelDungeonOfTeller.switchNoFade(StartScene.class);
                        }
                    }
                } );
            }
        };

        cont.setRect(0, HEIGHT - 20, WIDTH/2 -1, 20);
        add(cont);

        erase.setRect(WIDTH/2 + 1, HEIGHT-20, WIDTH/2 - 1, 20);
        add(erase);

        resize(WIDTH, HEIGHT);
    }

    private void statSlot( String label, String value ) {

        RenderedText txt = PixelScene.renderText( label, 8 );
        txt.y = pos;
        add( txt );

        txt = PixelScene.renderText( value, 8 );
        txt.x = WIDTH * 0.6f;
        txt.y = pos;
        PixelScene.align(txt);
        add( txt );

        pos += GAP + txt.baseLine();
    }

    private void statSlot( String label, int value ) {
        statSlot( label, Integer.toString( value ) );
    }
}

