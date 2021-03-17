package com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.BookOfLight;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class FlashSprite extends MagicSpellSprite {

    public FlashSprite() {
        super();

        texture(Assets.FLASH);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new MovieClip.Animation(8, true);
        idle.frames(frames, 0,1,2,3,4,5,6,7);
        play(idle);
    }
}
