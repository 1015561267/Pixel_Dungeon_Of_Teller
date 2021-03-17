package com.teller.pixeldungeonofteller.levels.rooms.connection;

import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class TunnelRoom extends ConnectionRoom {

    public void paint(Level level) {

        int floor = level.tunnelTile();

        Point c = center();

        if (width() > height() || (width() == height() && Random.Int( 2 ) == 0)) {

            int from = right - 1;
            int to = left + 1;

            for (Door door : connected.values()) {

                int step = door.y < c.y ? +1 : -1;

                if (door.x == left) {

                    from = left + 1;
                    for (int i=door.y; i != c.y; i += step) {
                        Painter.set( level, from, i, floor );
                    }

                } else if (door.x == right) {

                    to = right - 1;
                    for (int i=door.y; i != c.y; i += step) {
                        Painter.set( level, to, i, floor );
                    }

                } else {
                    if (door.x < from) {
                        from = door.x;
                    }
                    if (door.x > to) {
                        to = door.x;
                    }

                    for (int i=door.y+step; i != c.y; i += step) {
                        Painter.set( level, door.x, i, floor );
                    }
                }
            }

            for (int i=from; i <= to; i++) {
                Painter.set( level, i, c.y, floor );
            }

        } else {

            int from = bottom - 1;
            int to = top + 1;

            for (Door door : connected.values()) {

                int step = door.x < c.x ? +1 : -1;

                if (door.y == top) {

                    from = top + 1;
                    for (int i=door.x; i != c.x; i += step) {
                        Painter.set( level, i, from, floor );
                    }

                } else if (door.y == bottom) {

                    to = bottom - 1;
                    for (int i=door.x; i != c.x; i += step) {
                        Painter.set( level, i, to, floor );
                    }

                } else {
                    if (door.y < from) {
                        from = door.y;
                    }
                    if (door.y > to) {
                        to = door.y;
                    }

                    for (int i=door.x+step; i != c.x; i += step) {
                        Painter.set( level, i, door.y, floor );
                    }
                }
            }

            for (int i=from; i <= to; i++) {
                Painter.set( level, c.x, i, floor );
            }
        }

        for (Door door : connected.values()) {
            door.set( Door.Type.TUNNEL );
        }
    }

    protected Rect getConnectionSpace(){
        Point c = getDoorCenter();

        return new Rect(c.x, c.y, c.x, c.y);
    }

    //returns a point equidistant from all doors this room has
    protected final Point getDoorCenter(){
        PointF doorCenter = new PointF(0, 0);

        for (Door door : connected.values()) {
            doorCenter.x += door.x;
            doorCenter.y += door.y;
        }

        Point c = new Point((int)doorCenter.x / connected.size(), (int)doorCenter.y / connected.size());
        if (Random.Float() < doorCenter.x % 1) c.x++;
        if (Random.Float() < doorCenter.y % 1) c.y++;
        c.x = (int)GameMath.gate(left+1, c.x, right-1);
        c.y = (int)GameMath.gate(top+1, c.y, bottom-1);

        return c;
    }
}

