package com.teller.pixeldungeonofteller.levels.rooms.secret;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Hunger;
import com.teller.pixeldungeonofteller.items.food.ChargrilledMeat;
import com.teller.pixeldungeonofteller.items.food.Food;
import com.teller.pixeldungeonofteller.items.food.Pasty;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.plants.BlandfruitBush;
import com.watabou.utils.Point;

public class SecretLarderRoom extends SecretRoom {

    @Override
    public int minHeight() {
        return 6;
    }

    @Override
    public int minWidth() {
        return 6;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, Terrain.WALL);
        Painter.fill(level, this, 1, Terrain.EMPTY_SP);

        Point c = center();

        Painter.fill(level, c.x-1, c.y-1, 3, 3, Terrain.WATER);
        Painter.set(level, c, Terrain.GRASS);

        level.plant(new BlandfruitBush.Seed(), level.pointToCell(c));

        int extraFood = (int)(Hunger.DEFAULT - Hunger.PARTIAL) * (1 + Dungeon.depth / 5);

        while (extraFood > 0){
            Food food;
            if (extraFood >= Hunger.DEFAULT){
                food = new Pasty();
                extraFood -= Hunger.DEFAULT;
            } else {
                food = new ChargrilledMeat();
                extraFood -= (Hunger.DEFAULT - Hunger.PARTIAL);
            }
            int foodPos;
            do {
                foodPos = level.pointToCell(random());
            } while (level.map[foodPos] != Terrain.EMPTY_SP || level.heaps.get(foodPos) != null);
            level.drop(food, foodPos);
        }

        entrance().set(Door.Type.HIDDEN);
    }


}

