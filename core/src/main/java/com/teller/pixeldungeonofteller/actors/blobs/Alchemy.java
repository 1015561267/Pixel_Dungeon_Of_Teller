/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.teller.pixeldungeonofteller.actors.blobs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.journal.Notes;
import com.teller.pixeldungeonofteller.effects.BlobEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Alchemy extends Blob {

    protected int pos;

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        if (volume > 0)
            for (int i=0; i < cur.length; i++) {
                if (cur[i] > 0) {
                    pos = i;
                    break;
                }
            }
    }

    @Override
    protected void evolve() {
        //volume = off[pos] = cur[pos];
        //area.union(pos%Dungeon.level.width(), pos/Dungeon.level.width());

        //if (Dungeon.visible[pos]) {
        //    Notes.add( Notes.Landmark.ALCHEMY );
        //}
        int cell;
        for (int i=area.top-1; i <= area.bottom; i++) {
            for (int j = area.left-1; j <= area.right; j++) {
                cell = j + i* Dungeon.level.width();
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell];
                    volume += off[cell];
                    //if (off[cell] > 0 && Dungeon.level.heroFOV[cell]){
                    if (off[cell] > 0 && Dungeon.level.visited[cell]){
                        Notes.add( Notes.Landmark.ALCHEMY );
                    }
                    //for pre-0.6.2 saves
                    //while (off[cell] > 0 && Dungeon.level.heaps.get(cell) != null){

                     //   int n;
                    //    do {
                     //       n = cell + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
                     //   } while (!Dungeon.level.passable[n]);
                     //   Dungeon.level.drop( Dungeon.level.heaps.get(cell).pickUp(), n ).sprite.drop( pos );
                    //}
                }
            }
        }
    }

    public static void transmute( int cell ) {
        Heap heap = Dungeon.level.heaps.get( cell );
        if (heap != null) {
            Item result = heap.transmute();
            if (result != null) {
                Dungeon.level.drop( result, cell ).sprite.drop( cell );
            }
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.start( Speck.factory( Speck.BUBBLE ), 0.33f, 0 );
    }
}
