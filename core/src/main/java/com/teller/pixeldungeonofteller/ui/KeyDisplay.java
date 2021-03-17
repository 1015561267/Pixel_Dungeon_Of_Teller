package com.teller.pixeldungeonofteller.ui;

import android.graphics.RectF;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.journal.Notes;
import com.teller.pixeldungeonofteller.items.keys.CrystalKey;
import com.teller.pixeldungeonofteller.items.keys.GoldenKey;
import com.teller.pixeldungeonofteller.items.keys.IronKey;
import com.teller.pixeldungeonofteller.items.keys.Key;
import com.teller.pixeldungeonofteller.items.keys.SkeletonKey;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Quad;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.Visual;

import java.nio.FloatBuffer;
import java.util.LinkedHashMap;

public class KeyDisplay extends Visual {

    private float[] vertices = new float[16];
    private FloatBuffer quads;
    private Vertexbuffer buffer;

    private SmartTexture tx = TextureCache.get(Assets.MENU);

    private boolean dirty = true;
    private int[] keys;

    //mapping of key types to slots in the array, 0 is reserved for black (missed) keys
    //this also determines the order these keys will appear (lower first)
    //and the order they will be truncated if there is no space (higher first, larger counts first)
    private static final LinkedHashMap<Class<? extends Key>, Integer> keyMap = new LinkedHashMap<>();
    static {
        keyMap.put(SkeletonKey.class, 1);
        keyMap.put(CrystalKey.class, 2);
        keyMap.put(GoldenKey.class, 3);
        keyMap.put(IronKey.class, 4);
    }

    private int totalKeys = 0;

    public KeyDisplay() {
        super(0, 0, 0, 0);
    }

    public void updateKeys(){
        keys = new int[keyMap.size()+1];

        for (Notes.KeyRecord rec : Notes.getRecords(Notes.KeyRecord.class)){
            if (rec.depth() < Dungeon.depth){
                //only ever 1 black key
                keys[0] = 1;
            } else if (rec.depth() == Dungeon.depth){
                keys[keyMap.get(rec.type())] += rec.quantity();
            }
        }

        totalKeys = 0;
        for (int k : keys){
            totalKeys += k;
        }
        dirty = true;
    }

    public int keyCount(){
        return totalKeys;
    }

    @Override
    public void draw() {
        super.draw();
        if (dirty){

            updateVertices();

            quads.limit(quads.position());
            if (buffer == null)
                buffer = new Vertexbuffer(quads);
            else
                buffer.updateVertices(quads);

        }

        NoosaScript script = NoosaScript.get();

        tx.bind();

        script.camera( camera() );

        script.uModel.valueM4( matrix );
        script.lighting(
                rm, gm, bm, am,
                ra, ga, ba, aa );
        script.drawQuadSet( buffer, totalKeys, 0 );
    }

    private void updateVertices(){
        //assumes shorter key sprite
        int maxRows = (int)(height +1) / 5;

        //1 pixel of padding between each key
        int maxPerRow = (int)(width + 1) / 4;

        int maxKeys = maxPerRow * maxRows;


        while (totalKeys > maxKeys){
            Class<? extends Key> mostType = null;
            int mostNum = 0;
            for (Class<?extends Key> k : keyMap.keySet()){
                if (keys[keyMap.get(k)] >= mostNum){
                    mostType = k;
                    mostNum = keys[keyMap.get(k)];
                }
            }
            keys[keyMap.get(mostType)]--;
            totalKeys--;
        }

        int rows = (int)Math.ceil(totalKeys / (float)maxPerRow);

        boolean shortKeys = (rows * 8) > height;
        float left;
        if (totalKeys > maxPerRow){
            left = 0;
        } else {
            left = (width + 1 - (totalKeys*4))/2;
        }
        float top = (height + 1 - (rows * (shortKeys ? 5 : 8)))/2;
        quads = Quad.createSet(totalKeys);
        for (int i = 0; i < totalKeys; i++){
            int keyIdx = 0;

            if (i == 0 && keys[0] > 0){
                //black key
                keyIdx = 0;

            } else {
                for (int j = 1; j < keys.length; j++){
                    if (keys[j] > 0){
                        keys[j]--;
                        keyIdx = j;
                        break;
                    }
                }
            }

            //texture coordinates
            RectF r = tx.uvRect(43 + 3*keyIdx, shortKeys ? 8 : 0,
                    46 + 3*keyIdx, shortKeys ? 12 : 7);

            vertices[2] = r.left;
            vertices[3] = r.top;

            vertices[6] = r.right;
            vertices[7] = r.top;

            vertices[10] = r.right;
            vertices[11] = r.bottom;

            vertices[14] = r.left;
            vertices[15] = r.bottom;

            //screen coordinates
            vertices[0] = left;
            vertices[1] = top;

            vertices[4] = left + 3;
            vertices[5] = top;

            vertices[8] = left + 3;
            vertices[9] = top + (shortKeys ? 4 : 7);

            vertices[12] = left;
            vertices[13] = top + (shortKeys ? 4 : 7);

            quads.put(vertices);

            //move to the right for more keys, drop down if the row is done
            left += 4;
            if (left + 3 > width){
                left = 0;
                top += (shortKeys ? 5 : 8);
            }
        }

        dirty = false;

    }
}