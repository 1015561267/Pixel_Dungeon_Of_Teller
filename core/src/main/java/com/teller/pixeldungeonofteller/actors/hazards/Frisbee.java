package com.teller.pixeldungeonofteller.actors.hazards;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.hero.HeroAction;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.items.weapon.weapons.Shield.SawtoothFrisbee;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.HazardSprite.HazardSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Frisbee extends Hazard {

    private int strength;
    private int duration;

    private SawtoothFrisbee frisbee;
    private static final String FRISBEE = "frisbee";
    private static final String DURATION = "duration";

    //protected Emitter burning;

    public Frisbee() {
        super();

        this.pos = 0;
        this.strength = 0;
        this.duration = 0;
        this.frisbee = null;
        spriteClass = FrisbeeSprite.class;
        var = 0;
    }

    public void setValues( int pos, int duration ,SawtoothFrisbee frisbee) {
        this.pos = pos;
        this.duration = duration;
        this.frisbee = frisbee;
    }


    @Override
    public boolean act() {
        duration--;
        if( duration > 0 ){
            spend( TICK );
            Char ch = Actor.findChar(pos);
            if (ch != null) {

                ch.damage(frisbee.damageRoll(Dungeon.hero),this);

                if (ch == Dungeon.hero && !ch.isAlive())
                    Dungeon.fail(getClass());
            }

        } else {
            ((FrisbeeSprite)sprite).disappear();
            if(Dungeon.hero.curAction instanceof HeroAction.Ascend || Dungeon.hero.curAction instanceof HeroAction.Descend)
            {
                frisbee.fastreturn(pos);
            }
            else frisbee.retrieve(pos);
            destroy();
        }
        return true;
    }

    public void returnAndDestroy() {
        ((FrisbeeSprite) sprite).disappear();
        frisbee.fastreturn(pos);
        destroy();
        return;
    }

    @Override
    public void press(int cell, Char ch) {

    }

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( FRISBEE, frisbee );
        bundle.put( DURATION, duration );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        frisbee = (SawtoothFrisbee) bundle.get( FRISBEE );
        duration = bundle.getInt( DURATION );

    }

    public static class FrisbeeSprite extends HazardSprite {
        @Override
        protected String asset(){
            return Assets.FRISBEE;
        }

        @Override
        public int spritePriority(){
            return 3;
        }

        public void link( Hazard hazard ) {
            super.link( hazard );

            //burning = GameScene.emitter();
            //if( burning != null ){
            //    burning.pos( this );
            //    burning.pour( FlameParticle.FACTORY, 0.6f );
            //}
            //parent.add( burning );

            angularSpeed = 1440;
            origin.set( width / 2, height - DungeonTilemap.SIZE / 2 );
        }

        @Override
        public void update() {
            super.update();

            //if (burning != null) {
            //    burning.visible = visible;
            //}
        }

        public void appear( ) {
            am = 0.0f;
            parent.add(new AlphaTweener( this, 1.0f, 0.25f ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }

        public void disappear() {

            //if (burning != null) {
            //    burning.on = false;
            //    burning = null;
            //}

            parent.add(new AlphaTweener( this, 0.0f, 0.25f ) {
                @Override
                protected void onComplete() {
                    parent.erase(this);
                }
            });
        }
    }
}
