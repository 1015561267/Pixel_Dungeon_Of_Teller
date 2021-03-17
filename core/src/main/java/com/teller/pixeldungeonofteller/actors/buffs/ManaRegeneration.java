package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.artifacts.ChaliceOfBlood;

public class ManaRegeneration extends Buff {
    private static final float REGENERATION_DELAY = 1;
    @Override
    public boolean act() {
        if (target.isAlive()) {
            if (((Hero) target).MANA < ((Hero) target).MANACAP ) {
                LockedFloor lock = target.buff(LockedFloor.class);
                //if(lock!=null)
                 //   inboss=true;
                if ((lock == null || lock.regenOn())) {
                    ((Hero) target).MANA += 1;
                }
            }
                //spend(inboss?(int)Math.ceil(5-((Hero) target).INT/3):2*(int)Math.ceil(5-((Hero) target).INT/3));
            if(((Hero) target).INT<15)
            {
                spend((int)Math.ceil(5-((Hero) target).INT/3));
            }
            else
                spend(REGENERATION_DELAY);
        } else {
            diactivate();
        }
        return true;
    }
    }
