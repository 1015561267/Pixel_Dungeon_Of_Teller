package com.teller.pixeldungeonofteller.effects.particles;

//Created by Teller in 14/7/2019
//inorder to give CursedFlame a visual effect,but the color may need to be improved.

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;

public class CursedFlameParticle extends PixelParticle.Shrinking {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit(Emitter emitter, int index, float x, float y) {
            ((CursedFlameParticle) emitter.recycle(CursedFlameParticle.class)).reset(x, y);
        }

        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public CursedFlameParticle() {
        super();
        color(0x00f400);
        lifespan = 0.6f;
        acc.set(0, -80);
    }

    public void reset(float x, float y) {
        revive();

        this.x = x;
        this.y = y;

        left = lifespan;

        size = 4;
        speed.set(0);
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        am = p > 0.8f ? (1 - p) * 5 : 1;
    }
}