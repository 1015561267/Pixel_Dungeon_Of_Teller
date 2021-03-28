package com.teller.pixeldungeonofteller.actors.hazards;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.items.weapon.weapons.Shield.SawtoothFrisbee;
import com.teller.pixeldungeonofteller.sprites.HazardSprite.FrisbeeSprite;

public class Frisbee extends Hazard {

    private int strength;
    private int duration;

    private SawtoothFrisbee frisbee;


    public Frisbee() {
        super();

        this.pos = 0;
        this.strength = 0;
        this.duration = 0;

        this.frisbee = null;

        spriteClass = FrisbeeSprite.class;
        var = 0;
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
}
