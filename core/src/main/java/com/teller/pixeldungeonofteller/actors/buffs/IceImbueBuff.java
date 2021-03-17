package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.watabou.utils.Random;

public class IceImbueBuff extends Buff
{
    @Override
    public int icon() {
        return BuffIndicator.ICEIMBUE;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    public static void proc(Char ch)
    {
        float probability=0f;

        if(ch.SHLD==0||ch.MAXSHLD==0)
        {
            probability=0.4f;
        }
        else
        {
            probability=0.25f+0.3f*(ch.MAXSHLD-ch.SHLD)/ch.MAXSHLD;
        }
        if(Dungeon.level.water[ch.pos])
        { probability*=2; }
        if(Random.Float(1)<probability){    Buff.prolong(ch, Chill.class, Random.Float(4f)); }

        Chill chill = ch.buff(Chill.class);

        if (chill != null && Random.Int(100) < 10+5*chill.cooldown()) {
            new FlavourBuff() {
                {
                    actPriority = Integer.MIN_VALUE;
                }
                public boolean act() {
                    Buff.affect(target, Frost.class, Frost.duration(target) * Random.Float(1f, 2f));
                    return super.act();
                }
            }.attachTo(ch);
        }
    }
}
