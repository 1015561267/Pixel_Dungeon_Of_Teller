package com.teller.pixeldungeonofteller.levels.rooms.standard;

import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Imp;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.ImpShopkeeper;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.rooms.special.ShopRoom;
import com.watabou.utils.Bundle;

public class ImpShopRoom extends ShopRoom {

    private boolean impSpawned = false;

    //force a certain size here to guarantee enough room for 48 items, and the same center space
    @Override
    public int minWidth() {
        return 9;
    }
    public int minHeight() {
        return 9;
    }
    public int maxWidth() { return 9; }
    public int maxHeight() { return 9; }

    @Override
    public int maxConnections(int direction) {
        return 2;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );
        Painter.fill( level, this, 3, Terrain.WATER);

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

        if (Imp.Quest.isCompleted()){
            impSpawned = true;
            placeItems(level);
            placeShopkeeper(level);
        } else {
            impSpawned = false;
        }

    }

    @Override
    protected void placeShopkeeper(Level level) {

        int pos = level.pointToCell(center());

        Mob shopkeeper = new ImpShopkeeper();
        shopkeeper.pos = pos;
        level.mobs.add( shopkeeper );

    }

    //fix for connections not being bundled normally
    @Override
    public Door entrance() {
        return connected.isEmpty() ? new Door(left, top+2) : super.entrance();
    }

    private void spawnShop(Level level){
        impSpawned = true;
        super.paint(level);
    }

    private static final String IMP = "imp_spawned";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(IMP, impSpawned);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        impSpawned = bundle.getBoolean(IMP);
    }

    @Override
    public void onLevelLoad(Level level) {
        super.onLevelLoad(level);

        if (Imp.Quest.isCompleted() && !impSpawned){
            impSpawned = true;
            placeItems(level);
            placeShopkeeper(level);
        }
    }
}

