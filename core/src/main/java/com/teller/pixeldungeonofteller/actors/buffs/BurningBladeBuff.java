package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.watabou.utils.Random;

public class BurningBladeBuff extends Buff
{
    @Override
    public int icon() {
        return BuffIndicator.BURNINGBLADE;
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
        if(Dungeon.level.flamable[ch.pos])
        {
            probability=0.45f;
        }
        else
        { if(ch.SHLD==0||ch.MAXSHLD==0)
            { probability=0.45f; }
            else
            { probability=0.2f+0.25f*(ch.MAXSHLD-ch.SHLD)/ch.MAXSHLD; }
        }
        if(Random.Float(1)<probability){ Buff.affect(ch, Burning.class).reignite(ch); }
    }
}
