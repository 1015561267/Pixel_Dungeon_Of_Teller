package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.effects.Splash;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class HolyHealing extends Buff {

    private static final String SUM = "SUM";
    private static final String LEVEL = "level";

    protected int sum;
    protected int level;
    {
        type = buffType.POSITIVE;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SUM, sum);
        bundle.put(LEVEL, level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        sum = bundle.getInt(SUM);
        level = bundle.getInt(LEVEL);
    }

    @Override
    public int icon() {
        return BuffIndicator.HOLYHEALING;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", sum,level);
    }

    public void set(int s,int l)
    {
        this.sum = Math.max(this.sum,s);
        this.level = Math.max(this.level, l);
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            if (sum > 0) {
                int num = Math.min(sum, level);
                if (target.sprite.visible) {
                    target.sprite.emitter().start(Speck.factory(Speck.HOLYHEALING), num / 3, num);
                }

                if (target instanceof Hero) {
                    target.HP += Math.min(num, target.HT - target.HP);
                } else {
                    MagicalDamage dmg = new MagicalDamage();
                    dmg.AddHoly(num);
                    target.damage(dmg, this);
                }
                sum-=num;
            }
            spend(TICK);
            if (sum<= 0) {
                detach();
            }
        } else {
            detach();
        }
        return true;
    }
}
