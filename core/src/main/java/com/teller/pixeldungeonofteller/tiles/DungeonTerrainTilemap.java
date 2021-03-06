package com.teller.pixeldungeonofteller.tiles;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;

public class DungeonTerrainTilemap extends DungeonTilemap {

    static DungeonTerrainTilemap instance;

    public DungeonTerrainTilemap(){
        super(Dungeon.level.tilesTex());

        map( Dungeon.level.map, Dungeon.level.width() );

        instance = this;
    }

    @Override
    protected int getTileVisual(int pos, int tile, boolean flat) {
        int visual = DungeonTileSheet.directVisuals.get(tile, -1);
        if (visual != -1) return DungeonTileSheet.getVisualWithAlts(visual, pos);

        if (tile == Terrain.WATER) {
            return DungeonTileSheet.stitchWaterTile(
                    map[pos + PathFinder.CIRCLE4[0]],
                    map[pos + PathFinder.CIRCLE4[1]],
                    map[pos + PathFinder.CIRCLE4[2]],
                    map[pos + PathFinder.CIRCLE4[3]]
            );

        } else if (tile == Terrain.CHASM) {
            return DungeonTileSheet.stitchChasmTile( pos > mapWidth ? map[pos - mapWidth] : -1);
        }

        else if(tile == Terrain.ICE)
        {
            return DungeonTileSheet.stitchIceTile(
                    map[pos + PathFinder.CIRCLE4[0]],
                    map[pos + PathFinder.CIRCLE4[1]],
                    map[pos + PathFinder.CIRCLE4[2]],
                    map[pos + PathFinder.CIRCLE4[3]]
            );
        }



        if (!flat) {
            if ((DungeonTileSheet.doorTile(tile))) {
                return DungeonTileSheet.getRaisedDoorTile(tile, map[pos - mapWidth]);
            } else if (tile == Terrain.WALL || tile == Terrain.WALL_DECO
                    || tile == Terrain.SECRET_DOOR || tile == Terrain.BOOKSHELF){
                return DungeonTileSheet.getRaisedWallTile(
                        tile,
                        pos,
                        (pos+1) % mapWidth != 0 ?   map[pos + 1] : -1,
                        pos + mapWidth < size ?     map[pos + mapWidth] : -1,
                        pos % mapWidth != 0 ?       map[pos - 1] : -1
                );
            } else if (tile == Terrain.SIGN) {
                return DungeonTileSheet.RAISED_SIGN;
            } else if (tile == Terrain.STATUE) {
                return DungeonTileSheet.RAISED_STATUE;
            } else if (tile == Terrain.STATUE_SP) {
                return DungeonTileSheet.RAISED_STATUE_SP;
            } else if (tile == Terrain.BARRICADE) {
                return DungeonTileSheet.RAISED_BARRICADE;
            } else if (tile == Terrain.HIGH_GRASS) {
                return DungeonTileSheet.getVisualWithAlts(
                        DungeonTileSheet.RAISED_HIGH_GRASS,
                        pos);
            } else {
                return DungeonTileSheet.NULL_TILE;
            }
        } else {
            return DungeonTileSheet.getVisualWithAlts(
                    DungeonTileSheet.directFlatVisuals.get(tile),
                    pos);
        }

    }

    public static Image tile(int pos, int tile ) {
        Image img = new Image( instance.texture );
        img.frame( instance.tileset.get( instance.getTileVisual( pos, tile, true ) ) );
        return img;
    }

    @Override
    protected boolean needsRender(int pos) {
        return data[pos] != DungeonTileSheet.WATER ||
                (
                        pos + mapWidth < size &&
                                DungeonTileSheet.wallStitcheable(map[pos]) &&
                                DungeonTileSheet.wallStitcheable(map[pos + mapWidth])
                );
    }
}
