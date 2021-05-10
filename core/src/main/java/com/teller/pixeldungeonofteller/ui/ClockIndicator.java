package com.teller.pixeldungeonofteller.ui;

import com.teller.pixeldungeonofteller.Statistics;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;

public class ClockIndicator extends Tag {
    public ClockIndicator(int color) {
        super(0xff4c4c);
        setSize(24f, 16f);
        flash();
        visible = false;
    }

    private BitmapText time;

    @Override
    protected void createChildren() {
        super.createChildren();
        time = new BitmapText("00:00", PixelScene.pixelFont);
        add(time);
    }

    @Override
    protected void layout() {

        super.layout();

        time.measure();
        time.x = x + (width - time.width()) / 2f;
        time.y = y + (height - time.baseLine()) / 2f;
        PixelScene.align(time);
    }

    @Override
    public void update() {
        visible = Statistics.day.needNotice();

        if( visible )
        {
           time.text( Statistics.day.time );
        }

        super.update();

    }
}
