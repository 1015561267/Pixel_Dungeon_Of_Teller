package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;

public class Guard extends FlavourBuff {
    @Override
    public int icon() {
        return BuffIndicator.GUARD;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public void detach() {
        super.detach();
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    public PhysicalDamage getblocked(PhysicalDamage damage) {
        int transferddamage = 0;

        int transferpunceure = Math.min((int) Math.ceil(damage.PUNCTUREDAMAGE * 0.8f), 16);
        damage.PUNCTUREDAMAGE -= transferpunceure;
        if (damage.PUNCTUREDAMAGE < 0) damage.PUNCTUREDAMAGE = 0;
        transferddamage += transferpunceure;

        int transferslash = Math.min((int) Math.ceil(damage.SLASHDAMAGE * 0.8f), 16 - transferpunceure);
        damage.SLASHDAMAGE -= transferslash;
        if (damage.SLASHDAMAGE < 0) damage.SLASHDAMAGE = 0;
        transferddamage += transferslash;

        damage.IMPACTDAMAGE += Math.ceil(transferddamage / 2);
        return damage;
    }
}
