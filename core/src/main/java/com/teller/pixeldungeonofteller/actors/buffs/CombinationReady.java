package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;

public class CombinationReady extends Buff {

    @Override
    public int icon() {
        return BuffIndicator.COMBINATIONREADY;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }
}
