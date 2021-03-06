package com.teller.pixeldungeonofteller.effects;

import android.opengl.GLES20;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class CircleArc extends Visual {

    private float duration = 0;
    private float lifespan;

    //1f is an entire 360 degree sweep
    private float sweep;
    private boolean dirty;

    private boolean lightMode = true;

    private SmartTexture texture;

    private FloatBuffer vertices;
    private ShortBuffer indices;

    private int nTris;
    private float rad;

    //more triangles means a more precise visual
    public CircleArc( int triangles, float radius ) {

        super( 0, 0, 0, 0 );

        texture = TextureCache.createSolid( 0xFFFFFFFF );

        this.nTris = triangles;
        this.rad = radius;

        vertices = ByteBuffer.
                allocateDirect( (nTris * 2 + 1) * 4 * (Float.SIZE / 8) ).
                order( ByteOrder.nativeOrder() ).
                asFloatBuffer();

        indices = ByteBuffer.
                allocateDirect( nTris * 3 * Short.SIZE / 8 ).
                order( ByteOrder.nativeOrder() ).
                asShortBuffer();

        sweep = 1f;
        updateTriangles();
    }

    public CircleArc color( int color, boolean lightMode ) {
        this.lightMode = lightMode;
        hardlight( color );

        return this;
    }

    public CircleArc show(Visual visual, float duration ) {
        point( visual.center() );
        visual.parent.addToBack( this );

        lifespan = this.duration = duration;

        return this;
    }

    public CircleArc show(Group parent, PointF pos, float duration ) {
        point( pos );
        parent.add( this );

        lifespan = this.duration = duration;

        return this;
    }

    public void setSweep( float sweep ){
        this.sweep = sweep;
        dirty = true;
    }

    private void updateTriangles(){

        dirty = false;
        float v[] = new float[4];

        indices.position( 0 );
        vertices.position( 0 );

        v[0] = 0;
        v[1] = 0;
        v[2] = 0.25f;
        v[3] = 0;
        vertices.put( v );

        v[2] = 0.75f;
        v[3] = 0;

        //starting position is very top by default, use angle to adjust this.
        double start = 2 * (Math.PI - Math.PI*sweep) - Math.PI/2.0;

        for (int i = 0; i < nTris; i++) {

            double a = start + i * Math.PI * 2 / nTris * sweep;
            v[0] = (float)Math.cos( a ) * rad;
            v[1] = (float)Math.sin( a ) * rad;
            vertices.put( v );

            a += 3.1415926f * 2 / nTris * sweep;
            v[0] = (float)Math.cos( a ) * rad;
            v[1] = (float)Math.sin( a ) * rad;
            vertices.put( v );

            indices.put( (short)0 );
            indices.put( (short)(1 + i * 2) );
            indices.put( (short)(2 + i * 2) );
        }

        indices.position( 0 );
    }

    @Override
    public void update() {
        super.update();

        if (duration > 0) {
            if ((lifespan -= Game.elapsed) > 0) {
                sweep = lifespan/duration;
                dirty = true;

            } else {
                killAndErase();
            }
        }
    }

    @Override
    public void draw() {

        super.draw();

        if (dirty) {
            updateTriangles();
        }

        if (lightMode) 	GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );

        NoosaScript script = NoosaScript.get();

        texture.bind();

        script.uModel.valueM4( matrix );
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa );

        script.camera( camera );
        script.drawElements( vertices, indices, nTris * 3 );

        if (lightMode) GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
    }
}