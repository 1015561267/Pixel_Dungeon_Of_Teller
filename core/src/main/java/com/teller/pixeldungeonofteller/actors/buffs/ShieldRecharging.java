package com.teller.pixeldungeonofteller.actors.buffs;

import com.watabou.utils.Bundle;

public class ShieldRecharging extends Buff {

    //private static final float AutoCharge_DELAY = 15f;

    private static final String DELAY = "delay";

    private static final int[] delay = {13, 11, 9, 6, 3, 1};

    private int delayfactor = 0;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DELAY, delayfactor);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        delayfactor = bundle.getInt(DELAY);
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            if (target.SHLD < target.MAXSHLD) {
                LockedFloor lock = target.buff(LockedFloor.class);
                if (lock == null || lock.regenOn()) {
                    target.SHLD++;
                    //spend(delay[delayfactor]);
                    if (delayfactor < 5) {
                        delayfactor += 1;
                    }
                }
            }
            spend(delay[delayfactor]);
        } else {
            diactivate();
        }
        return true;
    }

    public void reset() {
        delayfactor = 0;
        return;
    }
}
