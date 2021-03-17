package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class CombinationCoolDown extends Buff {

    private static final String LEFT = "left";
    private float left;

    public void set(float cooldown) {
        left = cooldown;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat(LEFT);
    }

    @Override
    public int icon() {
        return BuffIndicator.COMBINATIONCOOLDOWN;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public void detach() {
        super.detach();
        Buff.affect(Dungeon.hero, CombinationReady.class);
    }

    public void recharge(float factor) {
        left -= factor * 10;
        if (left <= 0) {
            left = 0;
            detach();
        }
    }

    @Override
    public boolean act() {
        spend(TICK);
        left -= TICK;
        if (left <= 0) {
            detach();
        }
        return true;
    }

    @Override
    public String desc() {
        return Messages.get(CombinationCoolDown.class, "desc", dispTurns(left));
    }
}
