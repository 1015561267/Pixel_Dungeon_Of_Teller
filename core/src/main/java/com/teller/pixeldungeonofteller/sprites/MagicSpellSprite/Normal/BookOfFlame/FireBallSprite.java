package com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.BookOfFlame;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class FireBallSprite extends MagicSpellSprite {
    public FireBallSprite() {
        super();

        texture(Assets.FIREBALLSPELL);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new MovieClip.Animation(8, true);
        idle.frames(frames, 0,1,2,3,4,5,6,7);
        play(idle);
    }
}
