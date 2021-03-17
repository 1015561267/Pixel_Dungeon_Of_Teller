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
package com.teller.pixeldungeonofteller.levels.traps;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.MagicMissile;
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class GrimTrap extends Trap {

    {
        color = GREY;
        shape = LARGE_DOT;
    }

    @Override
    public Trap hide() {
        //cannot hide this trap
        return reveal();
    }

    @Override
    public void activate() {
        Char target = Actor.findChar(pos);

        //find the closest char that can be aimed at
        if (target == null) {
            for (Char ch : Actor.chars()) {
                Ballistica bolt = new Ballistica(pos, ch.pos, Ballistica.PROJECTILE);
                if (bolt.collisionPos == ch.pos &&
                        (target == null || Dungeon.level.distance(pos, ch.pos) < Dungeon.level.distance(pos, target.pos))) {
                    target = ch;
                }
            }
        }

        if (target != null) {
            final Char finalTarget = target;
            final GrimTrap trap = this;
            MagicMissile.shadow(target.sprite.parent, pos, target.pos, new Callback() {
                @Override
                public void call() {
                    if (!finalTarget.isAlive()) return;
                    if (finalTarget == Dungeon.hero) {
                        //almost kill the player
                        if (((float) finalTarget.HP / finalTarget.HT) >= 0.9f) {
                            finalTarget.damage(new AbsoluteDamage(finalTarget.HP - 1, this, finalTarget), trap);
                            //kill 'em
                        } else {
                            finalTarget.damage(new AbsoluteDamage(finalTarget.HP, this, finalTarget), trap);
                        }
                        Sample.INSTANCE.play(Assets.SND_CURSED);
                        if (!finalTarget.isAlive()) {
                            Dungeon.fail(GrimTrap.class);
                            GLog.n(Messages.get(GrimTrap.class, "ondeath"));
                        }
                    } else {
                        finalTarget.damage(new AbsoluteDamage(finalTarget.HP, this, finalTarget), this);
                        Sample.INSTANCE.play(Assets.SND_BURNING);
                    }
                    finalTarget.sprite.emitter().burst(ShadowParticle.UP, 10);
                    if (!finalTarget.isAlive()) finalTarget.next();
                }
            });
        } else {
            CellEmitter.get(pos).burst(ShadowParticle.UP, 10);
            Sample.INSTANCE.play(Assets.SND_BURNING);
        }
    }
}
