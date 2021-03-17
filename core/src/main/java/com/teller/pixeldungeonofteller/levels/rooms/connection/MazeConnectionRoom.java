package com.teller.pixeldungeonofteller.levels.rooms.connection;

import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.features.Maze;
import com.teller.pixeldungeonofteller.levels.painters.Painter;

public class MazeConnectionRoom extends ConnectionRoom {

    @Override
    public void paint(Level level) {
        super.paint(level);

        Painter.fill(level, this, 1, Terrain.EMPTY);

        //true = space, false = wall
        boolean[][] maze = Maze.generate(this);

        Painter.fill(level, this, 1, Terrain.EMPTY);
        for (int x = 0; x < maze.length; x++)
            for (int y = 0; y < maze[0].length; y++) {
                if (maze[x][y] == Maze.FILLED) {
                    Painter.fill(level, x + left, y + top, 1, 1, Terrain.WALL);
                }
            }

        for (Door door : connected.values()) {
            door.set( Door.Type.TUNNEL );
        }
    }
}

