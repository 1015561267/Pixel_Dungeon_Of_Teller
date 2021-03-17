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
package com.teller.pixeldungeonofteller.items.scrolls;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.MirrorImage;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ScrollOfMirrorImage extends Scroll {

    private static final int NIMAGES = 3;

    {
        initials = 4;
    }

    @Override
    protected void doRead() {

        ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = curUser.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                respawnPoints.add(p);
            }
        }

        int nImages = NIMAGES;
        while (nImages > 0 && respawnPoints.size() > 0) {
            int index = Random.index(respawnPoints);

            MirrorImage mob = new MirrorImage();
            mob.duplicate(curUser);
            GameScene.add(mob);
            ScrollOfTeleportation.appear(mob, respawnPoints.get(index));

            respawnPoints.remove(index);
            nImages--;
        }

        if (nImages < NIMAGES) {
            setKnown();
        }

        Sample.INSTANCE.play(Assets.SND_READ);
        Invisibility.dispel();

        readAnimation();
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
