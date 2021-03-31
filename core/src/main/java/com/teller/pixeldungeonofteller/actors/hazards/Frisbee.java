package com.teller.pixeldungeonofteller.actors.hazards;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.items.weapon.weapons.Shield.SawtoothFrisbee;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.HazardSprite.HazardSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Frisbee extends Hazard {

    private int strength;
    private int duration;

    private SawtoothFrisbee frisbee;


    public static Class<? extends HazardSprite> spriteClass = FrisbeeSprite.class;

    public Frisbee() {
        super();

        this.pos = 0;
        this.strength = 0;
        this.duration = 0;
        this.frisbee = null;

        spriteClass = FrisbeeSprite.class;

        //sprite = new FrisbeeSprite();
        var = 0;
    }



    public HazardSprite sprite() {
        HazardSprite sprite = null;
        try {
            sprite = spriteClass.newInstance();
        } catch (Exception e) {
           if (sprite == null)
           {
               sprite = new FrisbeeSprite();
           }
        }
        return sprite;
    }

    public void setValues( int pos, int duration ,SawtoothFrisbee frisbee) {
        this.pos = pos;

        //this.strength = strength;
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
            frisbee.retrieve(pos);
            ((FrisbeeSprite)sprite).disappear();
            destroy();
        }
        return true;
    }

    @Override
    public void press(int cell, Char ch) {

    }

    public static class FrisbeeSprite extends HazardSprite {

        private static float ANIM_TIME = 0.25f;

        private float time;

        protected Emitter burning;


        public void link( Hazard hazard ) {

            super.link(hazard);

            burning = GameScene.emitter();

            if( burning != null ){
                burning.pos( this );
                burning.pour( FlameParticle.FACTORY, 0.6f );
            }
            parent.add( burning );
        }

        public FrisbeeSprite(){
            super();
            time = 0.0f;
            origin.set(width / 2, height / 2);
        }

        @Override
        protected String asset(){
            return Assets.FRISBEE;
        }

        @Override
        public int spritePriority(){
            return 3;
        }

        @Override
        public void update() {
            super.update();
            //time += Game.elapsed * 3;
            //tint( 1.2f, 1.2f, 1.0f, 0.2f + (float)Math.sin( time ) * 0.1f );
            //speed.polar( time, 1.0f );
            //if (burning != null) {
            //    burning.visible = visible;
            //}

            if (burning != null) {
                burning.visible = visible;
            }
        }

        public void appear( ) {
//            angularSpeed = 0;
//            angle = 135 - (float) (Math.atan2(d.x, d.y) / 3.1415926 * 180);
//            PosTweener tweener = new PosTweener(this, dest, d.length() / 240f);

            //frame( ItemSpriteSheet.film.get( ItemSpriteSheet.SAWTOOTHFRISBEE ));
            am = 0.0f;

            final int csize = DungeonTilemap.SIZE;

            //origin.set( width / 2, height - DungeonTilemap.SIZE / 2 );

            angularSpeed = 1440;

            //parent.add(tweener);
 //           parent.add( new PosTweener(this, dest, d.length() / 240f) {
 //               @Override
 //               protected void onComplete() {
 //                   parent.erase(this);
 //               }
  //          });

            //parent.add( new PosTweener(this, dest, d.length() / 240f) {
            //parent.add(new AlphaTweener( this, 1.0f, ANIM_TIME ) {
            //    @Override
            //    protected void onComplete() {
            //        parent.erase(this);
            //    }
            //});
        }

        public void disappear() {
            //if (burning != null) {
            //    burning.on = false;
            //    burning = null;
            //}
            if (burning != null) {
                burning.on = false;
                burning = null;
            }
            this.destroy();
            this.killAndErase();
        }
    }
}
