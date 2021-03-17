package com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.OldBook;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.watabou.noosa.TextureFilm;

public class LightUpSprite extends MagicSpellSprite {

    public LightUpSprite() {
        super();

        texture(Assets.LIGHTUP);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(4, true);
        idle.frames(frames, 0,1,2,3,4,5,6,7);
        play(idle);
    }

}
