package com.teller.pixeldungeonofteller.items.food;

import com.teller.pixeldungeonofteller.actors.buffs.Appetizing;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Hunger;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class HawFlakes extends OverpricedRation {

    {
        image = ItemSpriteSheet.HAW_FLAKES;
        energy = Hunger.STARVING - Hunger.HUNGRY;
        hornValue = 1;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_EAT)) {
            Buff.affect(hero, Appetizing.class, 10f);
        }
    }
}
