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

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.effects.BadgeBanner;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.ui.RenderedTextBlock;
import com.teller.pixeldungeonofteller.ui.RenderedTextMultiline;
import com.teller.pixeldungeonofteller.ui.Window;
import com.watabou.noosa.Image;

public class WndBadge extends Window {

    private static final int WIDTH = 120;
    private static final int MARGIN = 4;

    public WndBadge(Badges.Badge badge,boolean unlocked) {

        super();

        /*Image icon = BadgeBanner.image(badge.image);
        icon.scale.set(2);
        add(icon);

        //TODO: this used to be centered, should probably figure that out.
        RenderedTextMultiline info = PixelScene.renderMultiline(badge.desc(), 8);
        info.maxWidth(WIDTH - MARGIN * 2);
        PixelScene.align(info);
        add(info);

        float w = Math.max(icon.width(), info.width()) + MARGIN * 2;

        icon.x = (w - icon.width()) / 2f;
        icon.y = MARGIN;
        PixelScene.align(icon);

        info.setPos((w - info.width()) / 2, icon.y + icon.height() + MARGIN);
        resize((int) w, (int) (info.bottom() + MARGIN));

        BadgeBanner.highlight(icon, badge.image);
         */
        Image icon = BadgeBanner.image( badge.image );
        icon.scale.set( 2 );
        if (!unlocked) icon.brightness(0.4f);
        add( icon );

        RenderedTextBlock info = PixelScene.renderTextBlock( badge.desc(), 8 );
        info.maxWidth(WIDTH - MARGIN * 2);
        info.align(RenderedTextBlock.CENTER_ALIGN);
        PixelScene.align(info);
        if (!unlocked) info.hardlight( 0x888888 );
        add(info);

        float w = Math.max( icon.width(), info.width() ) + MARGIN * 2;

        icon.x = (w - icon.width()) / 2f;
        icon.y = MARGIN;
        PixelScene.align(icon);

        info.setPos((w - info.width()) / 2, icon.y + icon.height() + MARGIN);
        resize( (int)w, (int)(info.bottom() + MARGIN) );

        if (unlocked) BadgeBanner.highlight( icon, badge.image );
    }
}
