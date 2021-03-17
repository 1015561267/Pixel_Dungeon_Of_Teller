package com.teller.pixeldungeonofteller.levels.rooms.standard;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.journal.GuidePage;
import com.teller.pixeldungeonofteller.journal.Document;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class EntranceRoom extends StandardRoom {

    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 5);
    }

    public void paint(Level level) {

        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY);

        for (Room.Door door : connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        do {
            level.entrance = level.pointToCell(random(2));
        } while (level.findMob(level.entrance) != null);
        Painter.set(level, level.entrance, Terrain.ENTRANCE);

        if (Dungeon.depth == 1 && !Document.ADVENTURERS_GUIDE.hasPage(Document.GUIDE_INTRO_PAGE)) {
            int pos;
            do {
                //can't be on bottom row of tiles
                pos = level.pointToCell(new Point(Random.IntRange(left + 1, right - 1),
                        Random.IntRange(top + 1, bottom - 2)));
            } while (pos == level.entrance || level.findMob(level.entrance) != null);
            GuidePage p = new GuidePage();
            p.page(Document.GUIDE_INTRO_PAGE);
            level.drop(p, pos);
        }

        if (Dungeon.depth == 2) {
            if (!Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)) {
                for (Room.Door door : connected.values()) {
                    door.set(Door.Type.HIDDEN);
                }
            }

            if (!Document.ADVENTURERS_GUIDE.hasPage(Document.GUIDE_SEARCH_PAGE)) {
                int pos;
                do {
                    //can't be on bottom row of tiles
                    pos = level.pointToCell(new Point(Random.IntRange(left + 1, right - 1),
                            Random.IntRange(top + 1, bottom - 2)));
                } while (pos == level.entrance || level.findMob(level.entrance) != null);
                GuidePage p = new GuidePage();
                p.page(Document.GUIDE_SEARCH_PAGE);
                level.drop(p, pos);
            }

        }

    }
}

