package com.teller.pixeldungeonofteller.actors.hazards;

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
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

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.sprites.HazardSprite.HazardSprite;
import com.watabou.utils.Bundle;

import java.util.HashSet;
//blob have few support for sprite while buff will have trouble in controlling operation involving multi actor
//source code from yet another pd(along with HazardSprite and other things)
public abstract class Hazard extends Actor {

    public Class<? extends HazardSprite> spriteClass;
    public HazardSprite sprite;

    public int pos;
    public int var;

    {
        actPriority = 1; //take priority over mobs, but not the hero
    }

    @Override
    protected boolean act(){
        return false;
    }

    public abstract void press( int cell, Char ch );

    public HazardSprite sprite() {
        HazardSprite sprite = null;
        try {
            sprite = spriteClass.newInstance();
        } catch (Exception e) {
            return null;
        }
        return sprite;
    }

    public String desc() {
        return null;
    };

    public void destroy() {

        Dungeon.level.hazards.remove(this);
        Actor.remove(this);

    }

    public static <T extends Hazard> T findHazard( int pos, Class<T> hazardClass ) {

        for( Hazard hazard : Dungeon.level.hazards ) {
            if( pos == hazard.pos && hazardClass.isInstance( hazard ) ){
                return hazardClass.cast( hazard );
            }
        }

        return null;
    }

    public static HashSet<Hazard> findHazards(int pos ) {

        HashSet<Hazard> hazards = new HashSet<>();

        for( Hazard hazard : Dungeon.level.hazards ) {
            if( pos == hazard.pos )
                hazards.add( hazard );
        }

        return hazards;
    }

    private static final String POS	= "pos";
    private static final String VAR	= "var";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( POS, pos );
        bundle.put( VAR, var );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        pos = bundle.getInt( POS );
        var = bundle.getInt( VAR );
    }
}

