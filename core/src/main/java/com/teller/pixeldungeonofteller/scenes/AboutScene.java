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

import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.effects.Flare;
import com.teller.pixeldungeonofteller.ui.Archs;
import com.teller.pixeldungeonofteller.ui.ExitButton;
import com.teller.pixeldungeonofteller.ui.Icons;
import com.teller.pixeldungeonofteller.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;

public class AboutScene extends PixelScene {

    private static final String TTL_DOT = "Dungeon Of Teller";

    private static final String TXT_TELLER = "Design , Code , &Graphics: Teller";

    private static final String TXT_PICTURE= "Picture and Icons : Passerby";

    private static final String TXT_DATA   = "Statistics and Balance : Trident";

    private static final String TTL_SHPX = "Shattered Pixel Dungeon";

    //private static final String TXT_SHPX =
    //        "Design, Code, & Graphics: Evan";

    //private static final String LNK_SHPX = "ShatteredPixel.com";

    private static final String TTL_WATA = "Pixel Dungeon";

    //private static final String TXT_WATA =
    //        "Code & Graphics: Watabou\n" +
    //                "Music: Cube_Code";
    //private static final String LNK_WATA = "pixeldungeon.watabou.ru";

    @Override
    public void create() {
        super.create();
        final float colWidth = Camera.main.width / (PixelDungeonOfTeller.landscape() ? 2 : 1);
        final float colTop = (Camera.main.height / 2) - (PixelDungeonOfTeller.landscape() ? 30 : 90);
        final float wataOffset = PixelDungeonOfTeller.landscape() ? colWidth : 0;

        RenderedText dot = renderText(TTL_DOT,16);
        dot.hardlight(0x706220);
        add(dot);
        dot.x = (colWidth - dot.width())/2;
        dot.y = colTop;
        align(dot);

        Image teller = Icons.TELLER.get();
        teller.x = (colWidth - teller.width()) / 2;
        teller.y = dot.y+dot.height();
        align(teller);
        add(teller);
        new Flare(14, 96).color(0x706220, true).show(teller, 0).angularSpeed = +20;

        RenderedText Tellertitle = renderText(TXT_TELLER,8);
        Tellertitle.hardlight(Window.TITLE_COLOR);
        add(Tellertitle);
        Tellertitle.x = (colWidth - Tellertitle.width())/2;
        Tellertitle.y = teller.y + teller.height()+2;
        align(Tellertitle);

        Image passerby = Icons.PASSERBY.get();
        passerby.x = (colWidth - passerby.width()) / 2;
        passerby.y = Tellertitle.y+Tellertitle.height()+1;
        align(passerby);
        add(passerby);
        new Flare(7, 96).color(0x6C34A8, true).show(passerby, 0).angularSpeed = +15;


        RenderedText Passerbytitle = renderText(TXT_PICTURE,8);
        Passerbytitle.hardlight(0x6C34A8);
        add( Passerbytitle);
        Passerbytitle.x = (colWidth -  Passerbytitle.width())/2;
        Passerbytitle.y = passerby.y + passerby.height()+1;
        align( Passerbytitle);

        Image trident =Icons.TRIDENT.get();
        trident.x = (colWidth - trident.width()) / 2;
        trident.y = Passerbytitle.y+Passerbytitle.height() + 1;
        align(trident);
        add(trident);
        new Flare(14, 64).color(0x0D047B, true).show(trident, 0).angularSpeed = +15;

        RenderedText Tridenttitle = renderText(TXT_DATA,8);
        Tridenttitle.hardlight(0x0C90D0);
        add( Tridenttitle);
        Tridenttitle.x = (colWidth -  Tridenttitle.width())/2;
        Tridenttitle.y = trident.y + trident.height()+2;
        align(Tridenttitle);

        Image shpx = Icons.SHPX.get();
        if (PixelDungeonOfTeller.landscape()) {
            shpx.y = colTop;
            shpx.x = teller.x + colWidth;
        } else {
            shpx.x = (colWidth - shpx.width()) / 2;
            shpx.y = Tridenttitle.y + Tridenttitle.height() + 20;
        }
        align(shpx);
        add(shpx);

        new Flare(7, 64).color(0x225511, true).show(shpx, 0).angularSpeed = +10;

        RenderedText shpxtitle = renderText(TTL_SHPX, 8);
        shpxtitle.hardlight(Window.SHPX_COLOR);
        add(shpxtitle);

        shpxtitle.x = (colWidth - shpxtitle.width()) / 2;
        if (PixelDungeonOfTeller.landscape())
            shpxtitle.x += colWidth;
        shpxtitle.y = shpx.y + shpx.height + 2;
        align(shpxtitle);

        // pixel dungeon
        Image wata = Icons.WATA.get();
        wata.x = wataOffset + (colWidth - wata.width()) / 2;
        wata.y = shpxtitle.y + shpxtitle.height()+20;
        align(wata);
        add(wata);

        new Flare(7, 64).color(0x112233, true).show(wata, 0).angularSpeed = +20;

        RenderedText wataTitle = renderText(TTL_WATA, 8);
        wataTitle.hardlight(Window.TITLE_COLOR);
        add(wataTitle);

        wataTitle.x = wataOffset + (colWidth - wataTitle.width()) / 2;
        wataTitle.y = wata.y + wata.height + 2;
        align(wataTitle);


        //Image shpx = Icons.SHPX.get();
        //shpx.x = (colWidth - shpx.width()) / 2;
        //shpx.y = colTop;
        //align(shpx);
        //add(shpx);
        //new Flare(7, 64).color(0x225511, true).show(shpx, 0).angularSpeed = +20;
        //RenderedText shpxtitle = renderText(TTL_SHPX, 8);
        //shpxtitle.hardlight(Window.SHPX_COLOR);
        //add(shpxtitle);
        //shpxtitle.x = (colWidth - shpxtitle.width()) / 2;
        //shpxtitle.y = shpx.y + shpx.height + 2;
        //align(shpxtitle);

        //RenderedTextMultiline shpxtext = renderMultiline(TXT_SHPX, 8);
        //shpxtext.maxWidth((int) Math.min(colWidth, 120));
        //add(shpxtext);

        //shpxtext.setPos((colWidth - shpxtext.width()) / 2, shpxtitle.y + shpxtitle.height() + 12);
        //align(shpxtext);

        //RenderedTextMultiline shpxlink = renderMultiline(LNK_SHPX, 8);
        //shpxlink.maxWidth(shpxtext.maxWidth());
        //shpxlink.hardlight(Window.SHPX_COLOR);
        //add(shpxlink);

        //shpxlink.setPos((colWidth - shpxlink.width()) / 2, shpxtext.bottom() + 6);
        //align(shpxlink);

        //TouchArea shpxhotArea = new TouchArea(shpxlink.left(), shpxlink.top(), shpxlink.width(), shpxlink.height()) {
        //    @Override
        //    protected void onClick(Touch touch) {
        //        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + LNK_SHPX));
        //        Game.instance.startActivity(intent);
        //    }
        //};
        //add(shpxhotArea);

        //Image wata = Icons.WATA.get();
        //wata.x = wataOffset + (colWidth - wata.width()) / 2;
        //wata.y = PixelDungeonOfTeller.landscape() ?
       //         colTop :
                //shpxlink.top()
        //                + wata.height + 20;
        //align(wata);
        //add(wata);

        //new Flare(7, 64).color(0x112233, true).show(wata, 0).angularSpeed = +20;

        //RenderedText wataTitle = renderText(TTL_WATA, 8);
        //wataTitle.hardlight(Window.TITLE_COLOR);
        //add(wataTitle);

        //wataTitle.x = wataOffset + (colWidth - wataTitle.width()) / 2;
        //wataTitle.y = wata.y + wata.height + 2;
        //align(wataTitle);

        //RenderedTextMultiline wataText = renderMultiline(TXT_WATA, 8);
        //wataText.maxWidth((int) Math.min(colWidth, 120));
        //add(wataText);

        //wataText.setPos(wataOffset + (colWidth - wataText.width()) / 2, wataTitle.y + wataTitle.height() + 12);
        //align(wataText);

        //RenderedTextMultiline wataLink = renderMultiline(LNK_WATA, 8);
        //wataLink.maxWidth((int) Math.min(colWidth, 120));
        //wataLink.hardlight(Window.TITLE_COLOR);
        //add(wataLink);

        //wataLink.setPos(wataOffset + (colWidth - wataLink.width()) / 2, wataText.bottom() + 6);
        //align(wataLink);

        //TouchArea hotArea = new TouchArea(wataLink.left(), wataLink.top(), wataLink.width(), wataLink.height()) {
        //    @Override
        //    protected void onClick(Touch touch) {
        //        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + LNK_WATA));
        //        Game.instance.startActivity(intent);
        //    }
        //};
        //add(hotArea);


        Archs archs = new Archs();
        archs.setSize(Camera.main.width, Camera.main.height);
        addToBack(archs);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(Camera.main.width - btnExit.width(), 0);
        add(btnExit);
        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        PixelDungeonOfTeller.switchNoFade(TitleScene.class);
    }
}
