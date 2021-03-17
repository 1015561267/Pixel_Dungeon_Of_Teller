package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Random;

public class Appetizing extends FlavourBuff {

    public static final float DURATION = 10f;

    @Override
    public int icon() {
        return BuffIndicator.APPTIZING;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    public void exqute(Char hero, float energy) {
        if (Random.Int(2) == 0) {
            (hero.buff(Hunger.class)).satisfy(energy / 2);
            GLog.i(Messages.get(this, "satisfy"));
        } else {
            GLog.w(Messages.get(this, "choke"));
        }
    }
}
