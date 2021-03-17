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
package com.teller.pixeldungeonofteller.sprites;

import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.items.Item;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

    private static final float SPEED = 240f;

    private Callback callback;

    public MissileSprite() {
        super();
        originToCenter();
    }

    public void reset(int from, int to, Item item, Callback listener) {
        if (item == null) {
            reset(from, to, 0, null, listener);
        } else {
            reset(from, to, item.image(), item.glowing(), listener);
        }
    }

    public void reset(int from, int to, int image, Glowing glowing, Callback listener) {
        revive();

        view(image, glowing);

        this.callback = listener;

        point(DungeonTilemap.tileToWorld(from));
        PointF dest = DungeonTilemap.tileToWorld(to);

        PointF d = PointF.diff(dest, point());
        speed.set(d).normalize().scale(SPEED);

        PosTweener tweener = new PosTweener(this, dest, d.length() / SPEED);

        if (image == ItemSpriteSheet.DART || image == ItemSpriteSheet.INCENDIARY_DART
                || image == ItemSpriteSheet.CURARE_DART || image == ItemSpriteSheet.JAVELIN || image == ItemSpriteSheet.KUNAI) {
            angularSpeed = 0;
            angle = 135 - (float) (Math.atan2(d.x, d.y) / 3.1415926 * 180);
            tweener = new PosTweener(this, dest, d.length() / SPEED);
        }
        else if(image ==ItemSpriteSheet.BULLET)
        {
            angularSpeed = 0;
            angle = 135 - (float) (Math.atan2(d.x, d.y) / 3.1415926 * 180);
            tweener = new PosTweener(this, dest, d.length() / (SPEED*2));
        }

        else if(image ==ItemSpriteSheet.DOUBLESHOOT)
        {
            angularSpeed = 0;
            angle = 135 - (float) (Math.atan2(d.x, d.y) / 3.1415926 * 180);
            tweener = new PosTweener(this, dest, d.length() / (SPEED*3));
        }

        else {
            angularSpeed = image == 15 || image == 106 ? 1440 : 720;
            tweener = new PosTweener(this, dest, d.length() / SPEED);
        }


        tweener.listener = this;
        parent.add(tweener);
    }

    @Override
    public void onComplete(Tweener tweener) {
        kill();
        if (callback != null) {
            callback.call();
        }
    }
}
