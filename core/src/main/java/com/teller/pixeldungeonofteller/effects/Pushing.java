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
package com.teller.pixeldungeonofteller.effects;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.features.Door;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class Pushing extends Actor {

    private CharSprite sprite;
    private int from;
    private int to;

    private Callback callback;

    {
        actPriority = Integer.MIN_VALUE; //it's a visual effect, gets priority no matter what
    }

    public Pushing(Char ch, int from, int to) {
        sprite = ch.sprite;
        this.from = from;
        this.to = to;
        this.callback = null;
    }

    public Pushing(Char ch, int from, int to, Callback callback) {
        this(ch, from, to);
        this.callback = callback;
    }

    @Override
    protected boolean act() {
        if (sprite != null) {
            PointF dest = sprite.worldToCamera( to );
            PointF d = PointF.diff( sprite.worldToCamera( from ), dest );
            PosTweener tweener = new PosTweener( sprite, dest, d.length() / 240f );
            tweener.listener = new Tweener.Listener() {
                @Override
                public void onComplete( Tweener tweener ){
                    Actor.remove( Pushing.this );
                    if( callback != null ){
                        callback.call();
                    }
                    next();
                }
            };
            sprite.parent.add( tweener );
            return false;
        } else {
            Actor.remove( Pushing.this );
            return true;
        }
    }

    public static void move( final Char ch, final int newPos, final Callback callback ) {
        // moved this method here to avoid repeatng the same pieces of code over and over
        // it is still not the most elegant
        Actor.addDelayed( new Pushing( ch, ch.pos, newPos, new Callback() {
            @Override
            public void call(){
                if( callback != null ){
                    callback.call();
                }

            }
        }), -1 );
        ch.pos = newPos;
    }

    public static void knockback( final Char ch, int pushedFrom, int Power ) {
        if( Power > 0 ){
            // first, we "remove" target from the tilemap and check where it
            // should land when knocked back, as if it weren't there

            Ballistica vector = new Ballistica(pushedFrom,ch.pos,ch.pos,Ballistica.MAGIC_BOLT);
            //note that pushFrom is the pos where the pusher stand,and we wants a vector so it shouldn't stop at target point
            // then we calcualte where the targer would actually land, considering
            // the maximum distance which it is supposed to be knocked back
            int pushedTo = vector.collisionPos;//this step get the collision point,in order to conclude the distance

            int index = vector.path.indexOf(pushedTo);

            int hitted = vector.path.get(index);
            if(index+1 < vector.path.size())
            {
                hitted = vector.path.get(index+1);
            }//we get one block after collisionpos as it's the pos it should stop when this happens,but not it should when it doesn't happen

            //Ballistica knockway = new Ballistica( ch.pos,hitted,Ballistica.MAGIC_BOLT);//build another ballistica to get real distance how long it really fly
            // note that all ballistica path is a full path that go through the whole map
            Ballistica knockway = new Ballistica( ch.pos,hitted,Ballistica.MAGIC_BOLT);//build another ballistica to get real distance how long it really fly

            Power = Math.min( knockway.dist , Power );//then we compare two inorder to know what will happen first between fly to end and crush into sth

            if (pushedTo == ch.pos)//in some condition.like use blast wave against narrow wall,first collisonPos is same with ch's pos,then Power would always be 0,and we need plus one otherwise code below will be invalid
            { Power++; }

            if( Power > 0 ){

                int stopPos = knockway.path.get(Power);//get the block the char should stay
                int backPos = knockway.path.get(Power-1);
                for(int dist:knockway.subPath(1, knockway.dist))
                {
                    if(Dungeon.level.map[dist] ==Terrain.DOOR)
                    {
                        stopPos = dist;//get the block the char should stay
                        backPos = knockway.path.get(knockway.path.indexOf(dist)-1);
                    }
                }
                // gotta make those final for the sake of using callback mechanics
                    //final int stop = knockway.path.get(Power);//get the block the char should stay
                    //final int knock = knockway.path.get(Power-1);

                    final int finalPos = stopPos;//get the block the char should stay
                    final int knockPos = backPos;

                    final Char pushedInto = Char.findChar( finalPos );
                move( ch , finalPos , new Callback() {
                    @Override
                    public void call(){
                        if( pushedInto != null || Dungeon.level.solid[ finalPos ] )//this means this pos is occupied,so draw back to one block before the path
                        {
                            if(Dungeon.level.map[finalPos] == Terrain.DOOR)
                            {
                                Door.enter(finalPos);
                            }
                            if(Dungeon.level.map[knockPos] == Terrain.CHASM) // FIXME this will cause hero fall before bounce back
                            {
                                move(ch, knockPos, new Callback() {
                                    @Override
                                    public void call() {
                                        Dungeon.level.press( ch.pos, ch );
                                    }
                                });
                                return;
                            }
                            else  hitObstacle( ch, knockPos);
                        }
                        if( ch.isAlive() ){
                            if( ch instanceof Mob ){
                                ((Mob) ch).beckon(ch.pos);
                                ch.delay( 1f );
                            }
                            // apply target's current position and activate traps there
                            // gotta re-check whether mobs killed by knockback activate traps or not
                            //Actor.occupyCell(ch);
                            Dungeon.level.press( ch.pos, ch );
                        }
                        else
                        {
                            ch.die(this);
                        }
                    }
            });
                if( pushedInto != null ){
                    knockback( pushedInto, knockPos, 1 );
                }
            }
            else {
            }
        }
    }

    private static void hitObstacle(final Char ch , int moveTo) {
        // move() method handles changing the target's position value
        move( ch, moveTo, null );
        // make sounds and shake the screen to make this effect meatier
        if( Dungeon.visible[ ch.pos ] ) {
            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.5f );
            Camera.main.shake( 2, 0.1f );
        }
    }



   /* public class Effect extends Visual {

        private static final float DELAY = 0.15f;

        private PointF end;

        private float delay;

        public Effect() {
            super(0, 0, 0, 0);

            point(sprite.worldToCamera(from));
            end = sprite.worldToCamera(to);

            speed.set(2 * (end.x - x) / DELAY, 2 * (end.y - y) / DELAY);
            acc.set(-speed.x / DELAY, -speed.y / DELAY);

            delay = 0;

            if (sprite.parent != null)
                sprite.parent.add(this);
        }

        @Override
        public void update() {
            super.update();
            if ((delay += Game.elapsed) < DELAY) {
                sprite.x = x;
                sprite.y = y;
            } else {
                sprite.point(end);
                killAndErase();
                Actor.remove(Pushing.this);
                if (callback != null) callback.call();
                next();
            }
        }
    }*/
}
