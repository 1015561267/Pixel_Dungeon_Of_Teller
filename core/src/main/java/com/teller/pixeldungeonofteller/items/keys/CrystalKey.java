package com.teller.pixeldungeonofteller.items.keys;

import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class CrystalKey extends Key {

    {
        image = ItemSpriteSheet.CRYSTAL_KEY;
    }

    public CrystalKey() {
        this(0);
    }

    public CrystalKey(int depth) {
        super();
        this.depth = depth;
    }
}