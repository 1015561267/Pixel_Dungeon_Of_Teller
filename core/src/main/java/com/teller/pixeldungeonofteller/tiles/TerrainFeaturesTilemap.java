package com.teller.pixeldungeonofteller.tiles;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.traps.Trap;
import com.teller.pixeldungeonofteller.plants.Plant;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.PointF;
import com.watabou.utils.SparseArray;

public class TerrainFeaturesTilemap extends DungeonTilemap {

    private static TerrainFeaturesTilemap instance;

    private SparseArray<Plant> plants;
    private SparseArray<Trap> traps;

    public TerrainFeaturesTilemap(SparseArray<Plant> plants, SparseArray<Trap> traps) {
        super(Assets.TERRAIN_FEATURES);

        this.plants = plants;
        this.traps = traps;

        map( Dungeon.level.map, Dungeon.level.width() );

        instance = this;
    }

    protected int getTileVisual(int pos, int tile, boolean flat){
        if (traps.get(pos) != null){
            Trap trap = traps.get(pos);
            if (!trap.visible)
                return -1;
            else
                return (trap.active ? trap.color : Trap.BLACK) + (trap.shape * 16);
        }

        if (plants.get(pos) != null){
            return plants.get(pos).image + 7*16;
        }

        if (tile == Terrain.HIGH_GRASS){
            return 9 + 16*((Dungeon.depth-1)/5) + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
        } else if (tile == Terrain.GRASS) {
            return 11 + 16*((Dungeon.depth-1)/5) + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
        } else if (tile == Terrain.EMBERS) {
            return 13 + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
        }

        return -1;
    }

    public static Image tile(int pos, int tile ) {
        Image img = new Image( instance.texture );
        img.frame( instance.tileset.get( instance.getTileVisual( pos, tile, true ) ) );
        return img;
    }

    public void growPlant( final int pos ){
        final Image plant = tile( pos, map[pos] );
        plant.origin.set( 8, 12 );
        plant.scale.set( 0 );
        plant.point( DungeonTilemap.tileToWorld( pos ) );

        parent.add( plant );

        parent.add( new ScaleTweener( plant, new PointF(1, 1), 0.2f ) {
            protected void onComplete() {
                plant.killAndErase();
                killAndErase();
                updateMapCell(pos);
            }
        } );
    }

    @Override
    protected boolean needsRender(int pos) {
        return data[pos] != -1;
    }
}
