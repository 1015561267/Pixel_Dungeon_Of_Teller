package com.teller.pixeldungeonofteller.actors.buffs;

//Created by Teller in 14/7/2019
//nothing special,less code than burning

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CursedFlame extends Buff implements Hero.Doom {

    private static final float DURATION = 12f;
    private static final String LEFT = "left";
    private float left;

    {
        type = buffType.NEGATIVE;
    }

    public static float duration(Char ch) {
        return DURATION;
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
    public boolean act() {
        if (target.isAlive()) {
            //maximum damage scales from 6 to 2 depending on remaining hp.
            int maxDmg = 3 + Math.round(4 * target.HP / (float) target.HT);
            int damage = Random.Int(1, maxDmg);
            Buff.detach(target, Chill.class);
            if (target instanceof Hero) {
                target.damage(new AbsoluteDamage(damage, this, target), this);
            } else {
                target.damage(new AbsoluteDamage(damage, this, target), this);
            }
        } else {
            detach();
        }
        spend(TICK);
        //left -= TICK;
        //if (left <= 0 ) {
        //    detach();
        //}
        return true;
    }

    public void reignite(Char ch) {
        left = duration(ch);
    }

    @Override
    public int icon() {
        return BuffIndicator.CURSEDFLAME;
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.CURSEDFLAME);
        else target.sprite.remove(CharSprite.State.CURSEDFLAME);
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromFire();
        Dungeon.fail(getClass());
        GLog.n(Messages.get(this, "ondeath"));
    }
}

