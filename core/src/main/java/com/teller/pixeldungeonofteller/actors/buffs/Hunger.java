/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Challenges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.hero.HeroClass;
import com.teller.pixeldungeonofteller.items.artifacts.Artifact;
import com.teller.pixeldungeonofteller.items.artifacts.ChaliceOfBlood;
import com.teller.pixeldungeonofteller.items.artifacts.HornOfPlenty;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;

public class Hunger extends Buff implements Hero.Doom {

    //public static final float PARTIAL = 300f;
    //public static final float DEFAULT = 400f;

    //FIXME Now that auto-regenerate buff have nothing to do now,so emerge it together like what YAPD did(improved resting and hunger also from it),may fix this later

    public static final float REGENERATION_RATE	= 0.002f;

    public static final float SATIATED = 500f;
    public static final float PARTIAL = 400f;

    public static final float HUNGER = 100f;


    public static final float DEFAULT = 0f;

    public static final float STARVING = -50f;
    public static final float RAVENOUS = -300f;


    private static final float STEP = 1f;
    private static final String LEVEL = "level";
    private static final String PARTIALDAMAGE = "partialDamage";

    private float level = SATIATED;
    private float partialDamage = 0f;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
        bundle.put(PARTIALDAMAGE, partialDamage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getFloat(LEVEL);
        partialDamage = bundle.getFloat(PARTIALDAMAGE);
    }

    @Override
    public boolean act() {

        LockedFloor lock = target.buff(LockedFloor.class);

        if (lock != null && !lock.regenOn()) {
            spend(STEP);
            return true;
        }

        if (target.isAlive()) {

            Hero hero = (Hero) target;

            float modifier = REGENERATION_RATE * hero.HT;
            float variation = 0f;
            boolean interrupted = false;

            if(level <= DEFAULT)
            {
                if(level <= RAVENOUS) variation = -2.0f;
                    else if (level <= STARVING) variation = -1.0f;//Hunger itself just don't regenerate nor lose hp
            }
            else
            {
                if(hero.resting) variation = 3.0f;
                else if( level > PARTIAL) variation = 1.5f;
                else if(level > HUNGER) variation = 1f;
                else  variation = 0.5f;
            }
            variation *= modifier;

            if(variation > 0 )
            {
                ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff(ChaliceOfBlood.chaliceRegen.class);
                    if (regenBuff != null) {
                        if (regenBuff.isCursed())
                            variation = variation / 1.5f;
                        else
                            variation = variation * 10f / regenBuff.itemLevel();
                    }

                        partialDamage += variation;
                        if( partialDamage >= 1.0f ) {
                            target.HP = (int) Math.min(target.HT, target.HP + Math.floor(partialDamage));
                            if (target.HP == target.HT) {
                                if (((Hero) target).resting) {
                                    GLog.p(Messages.get(this, "rested"));
                                }
                                ((Hero) target).resting = false;
                            }
                            partialDamage = partialDamage % 1.0f;
                        }
            }
            else if(variation < 0) {
                partialDamage += variation;
                if (partialDamage <= -1.0f) {
                    GameScene.flash(0x110000);
                    target.damage(new AbsoluteDamage((int)Math.ceil(partialDamage * -1.0f), this, target), this);//note that daamage will minus the value,while the value here is already negative
                    //I feel myself totally stupid when carefully think about this for 10 mins then realize it
                    partialDamage = partialDamage % 1.0f;
                    hero.resting = false;
                    hero.interrupt();
                    interrupted = true;
                }
            }

            float isRougue = ((Hero) target).heroClass == HeroClass.ROGUE ?  1.2f : 1f;
            float haveShadow = target.buff(Shadows.class) == null ? 1f : 1.5f ;

            float beLevel = level;
            level -= STEP / isRougue / haveShadow;

            verify(beLevel,level,interrupted);

            spend(STEP);
        } else {
            diactivate();
        }

        return true;
    }

    public void verify(float before , float now , boolean interrupted)
    {
        boolean statusUpdated = false;
        boolean needZeroing = false;

        Hero hero = (Hero) target;

        if(before < PARTIAL && now >=  PARTIAL )
        {
            statusUpdated = true;
            GLog.p(Messages.get(this, "up_satiated"));
        }
        else if(before < HUNGER && now >= HUNGER)
        {
            statusUpdated = true;
            GLog.i(Messages.get(this, "up_normal"));
        }
        else if(before < DEFAULT && now >= DEFAULT)
        {
            statusUpdated = true;
            needZeroing = true;
            GLog.w(Messages.get(this, "up_partial"));
        }

        else if(before > PARTIAL && now <= PARTIAL)
        {
            statusUpdated = true;
            GLog.i(Messages.get(this, "down_normal"));

        }
        else if(before > HUNGER && now <= HUNGER)
        {
            statusUpdated = true;
            GLog.w(Messages.get(this, "down_partial"));

        }
        else if(before > DEFAULT && now <= DEFAULT)
        {
            statusUpdated = true;
            needZeroing = true;
            GLog.w(Messages.get(this, "down_hunger"));

        }
        else if(before > STARVING && now <= STARVING)
        {
            statusUpdated = true;
            GLog.n(Messages.get(this, "down_starving"));
        }
        else if(before > RAVENOUS && now <= RAVENOUS)
        {
            statusUpdated = true;
            GLog.n(Messages.get(this, "down_ravenous"));

        }

        if(needZeroing)
        {
            partialDamage = 0f;
        }

        if(statusUpdated)
        {
            BuffIndicator.refreshHero();
            if (!interrupted) {
                ((Hero) target).interrupt();
            }
        }
    }

    public void satisfy(float energy) {

        Artifact.ArtifactBuff buff = target.buff(HornOfPlenty.hornRecharge.class);
        if (buff != null && buff.isCursed()) {
            energy *= 0.67f;
            GLog.n(Messages.get(this, "cursedhorn"));
        }

        if (!Dungeon.isChallenged(Challenges.NO_FOOD)) {
            if(level<= DEFAULT)
            {
                level = DEFAULT;
            }
            reduceHunger(energy);
        }
    }

    //directly interacts with hunger, no checks.
    public void reduceHunger(float energy) {

        float beLevel = level;
        level += energy;
        verify(beLevel,level,false);
        BuffIndicator.refreshHero();
    }

    public boolean isStarving() {
        return level <= DEFAULT;
    }

    public int hunger() {
        return (int) Math.ceil(level);
    }

    @Override
    public int icon() {
        if (level > PARTIAL) {
            return BuffIndicator.SATIATED;
        } else if (level > HUNGER) {
            return BuffIndicator.NONE;
        } else if (level > DEFAULT) {
            return BuffIndicator.PARTIAL;
        }
        else if (level > STARVING) {
            return BuffIndicator.HUNGER;
        }
        else if (level > RAVENOUS) {
            return BuffIndicator.STARVING;
        }
        else return BuffIndicator.RAVENOUS;
    }

    @Override
    public String toString() {
        if (level > PARTIAL) {
            return Messages.get(this, "satiated");
        } else if (level >  HUNGER) {
            return null;
        }
        else if (level >  DEFAULT) {
            return Messages.get(this, "partial");
        }
        else if (level > STARVING) {
            return Messages.get(this, "hungry");
        }
        else if (level > RAVENOUS) {
            return Messages.get(this, "starving");
        }else {
            return Messages.get(this, "ravenous");
        }
    }

    @Override
    public String desc() {
        String result = null;

        if (level >   PARTIAL) {
            result =  Messages.get(this, "desc_satiated");
        } else if (level >  HUNGER) { }
        else if (level >  DEFAULT) {
            result =  Messages.get(this, "desc_partial");
        }
        else if (level > STARVING) {
            result =  Messages.get(this, "desc_hungry");
        }
        else if (level > RAVENOUS) {
            result =  Messages.get(this, "desc_starving");
        }else {
            result =  Messages.get(this, "desc_ravenous");
        }

        result += Messages.get(this, "desc");

        return result;
    }

    @Override
    public void onDeath() {

        Badges.validateDeathFromHunger();

        Dungeon.fail(getClass());
        GLog.n(Messages.get(this, "ondeath"));
    }
}
