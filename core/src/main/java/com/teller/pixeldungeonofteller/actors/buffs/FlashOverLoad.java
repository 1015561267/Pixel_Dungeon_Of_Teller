package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class FlashOverLoad extends Buff {

    private static final String LEVEL = "level";
    private static final String TURNSTODETACH = "turnstodetach";

    protected int level;
    protected int turns;


    {
        type = buffType.POSITIVE;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(TURNSTODETACH, turns);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        turns = bundle.getInt(TURNSTODETACH);
    }


    public void set() {
        level=2;
        turns=10;
    }

    public void update() {
        level++; turns=10;
    }


    @Override
    public int icon() {
        return BuffIndicator.FLASHOVERLOAD;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", level-1,manacost(),turns);
    }

    @Override
    public boolean act() {
        spend(TICK);
        turns-= TICK;
        if (turns <= 0) {
            detach();
        }
        return true;
    }

    public int manacost()
    {
        if(level==1) return 6;
        else if(level==2) return 12;
        else if(level==3) return 20;
        else return 20+4*level;
    }

    public int attenuation()
    {
        if(level==1) return 2;
        else return 3;
    }

    public int power()
    {
        return level>3 ? 16:4*(level+1);
    }
}
