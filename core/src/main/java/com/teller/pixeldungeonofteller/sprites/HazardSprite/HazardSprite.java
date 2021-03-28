package com.teller.pixeldungeonofteller.sprites.HazardSprite;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.hazards.Hazard;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Dagger;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.PointF;

public abstract class HazardSprite extends Image {
    private TextureFilm textures;
    public Hazard hazard;

    public HazardSprite() {
        super();

        if (textures == null){
            texture( asset() );
            textures = new TextureFilm( texture, 16, 16 );
        }
    }

    protected abstract String asset();
    public abstract int spritePriority();

    public void link( Hazard hazard ) {

        this.hazard = hazard;
        hazard.sprite = this;

        frame( textures.get( hazard.var ) );

        place( hazard.pos );
    }

    public void place( int cell ) {

        final int csize = DungeonTilemap.SIZE;

        int height = Dungeon.level.getHeight();
        int width = Dungeon.level.getWidth();

        point( new PointF(
                ((cell % height) + 0.5f) * csize - width * 0.5f,
                ((cell / width) + 0.5f) * csize - height * 0.5f));

        origin.set( width / 2, height / 2 );
    }

    public void changeFrame( int var ) {
        frame( textures.get( var ) );
    }

    @Override
    public void update() {
        super.update();
        visible = Dungeon.visible[ hazard.pos ];
    }
}