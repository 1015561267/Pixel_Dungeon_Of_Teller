package com.teller.pixeldungeonofteller.sprites.HazardSprite;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.hazards.Frisbee;
import com.teller.pixeldungeonofteller.actors.hazards.Hazard;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class FrisbeeSprite extends HazardSprite {

    private static float ANIM_TIME = 0.25f;

    private float time;

    protected Emitter burning;

    private int pos;

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
    public void link( Hazard hazard ) {

        super.link( hazard );

        burning = GameScene.emitter();

        if( burning != null ){
            burning.pos( this );
            burning.pour( FlameParticle.FACTORY, 0.6f );
        }

        parent.add( burning );
    }

    @Override
    public void update() {
        super.update();

        time += Game.elapsed * 3;

        tint( 1.2f, 1.2f, 1.0f, 0.2f + (float)Math.sin( time ) * 0.1f );

        speed.polar( time, 1.0f );

        if (burning != null) {
            burning.visible = visible;
        }

    }

    public void appear( ) {

        am = 0.0f;

        point(DungeonTilemap.tileToWorld(this.pos));
        PointF dest = DungeonTilemap.tileToWorld(this.pos);

        PointF d = PointF.diff(dest, point());
        speed.set(d).normalize().scale(240f);

        parent.add(
                new PosTweener(this, dest, d.length() / 240f) {
            @Override
            protected void onComplete() {
                parent.erase(this);
            }
        });
    }

    public void disappear() {

        if (burning != null) {
            burning.on = false;
            burning = null;
        }

        parent.add(new AlphaTweener( this, 0.0f, ANIM_TIME ) {
            @Override
            protected void onComplete() {
                parent.erase(this);
            }
        });
    }
}