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
import com.teller.pixeldungeonofteller.items.artifacts.HornOfPlenty;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;

public class Hunger extends Buff implements Hero.Doom {

    public static final float HUNGRY = 300f;
    public static final float STARVING = 400f;
    private static final float STEP = 10f;
    private static final String LEVEL = "level";
    private static final String PARTIALDAMAGE = "partialDamage";
    private float level;
    private float partialDamage;

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

        if (Dungeon.level.locked) {
            spend(STEP);
            return true;
        }

        if (target.isAlive()) {

            Hero hero = (Hero) target;

            if (isStarving()) {

                partialDamage += target.HT / 100f;

                if (partialDamage > 1) {
                    target.damage(new AbsoluteDamage((int) (partialDamage), this, target), target);
                    partialDamage -= (int) partialDamage;
                }

            } else {

                float newLevel = level + STEP;
                boolean statusUpdated = false;
                if (newLevel >= STARVING) {

                    GLog.n(Messages.get(this, "onstarving"));
                    hero.resting = false;
                    hero.damage(new AbsoluteDamage(1, this, target), this);
                    statusUpdated = true;

                    hero.interrupt();

                } else if (newLevel >= HUNGRY && level < HUNGRY) {

                    GLog.w(Messages.get(this, "onhungry"));
                    statusUpdated = true;

                }
                level = newLevel;

                if (statusUpdated) {
                    BuffIndicator.refreshHero();
                }

            }

            float step = ((Hero) target).heroClass == HeroClass.ROGUE ? STEP * 1.2f : STEP;
            spend(target.buff(Shadows.class) == null ? step : step * 1.5f);

        } else {

            diactivate();

        }

        return true;
    }

    public void satisfy(float energy) {

        Artifact.ArtifactBuff buff = target.buff(HornOfPlenty.hornRecharge.class);
        if (buff != null && buff.isCursed()) {
            energy *= 0.67f;
            GLog.n(Messages.get(this, "cursedhorn"));
        }

        if (!Dungeon.isChallenged(Challenges.NO_FOOD))
            reduceHunger(energy);
    }

    //directly interacts with hunger, no checks.
    public void reduceHunger(float energy) {

        level -= energy;
        if (level < 0) {
            level = 0;
        } else if (level > STARVING) {
            level = STARVING;
        }

        BuffIndicator.refreshHero();
    }

    public boolean isStarving() {
        return level >= STARVING;
    }

    public int hunger() {
        return (int) Math.ceil(level);
    }

    @Override
    public int icon() {
        if (level < HUNGRY) {
            return BuffIndicator.NONE;
        } else if (level < STARVING) {
            return BuffIndicator.HUNGER;
        } else {
            return BuffIndicator.STARVATION;
        }
    }

    @Override
    public String toString() {
        if (level < STARVING) {
            return Messages.get(this, "hungry");
        } else {
            return Messages.get(this, "starving");
        }
    }

    @Override
    public String desc() {
        String result;
        if (level < STARVING) {
            result = Messages.get(this, "desc_intro_hungry");
        } else {
            result = Messages.get(this, "desc_intro_starving");
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
