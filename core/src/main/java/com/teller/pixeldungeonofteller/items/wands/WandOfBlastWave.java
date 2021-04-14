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
package com.teller.pixeldungeonofteller.items.wands;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.effects.Effects;
import com.teller.pixeldungeonofteller.effects.MagicMissile;
import com.teller.pixeldungeonofteller.effects.Pushing;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.MagesStaff;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfBlastWave extends DamageWand {

    {
        image = ItemSpriteSheet.WAND_BLAST_WAVE;

        collisionProperties = Ballistica.PROJECTILE;
    }

    public static void throwChar(final Char ch, final Ballistica trajectory, int power) {
        int dist = Math.min(trajectory.dist, power);

        if (ch.properties().contains(Char.Property.BOSS))
            dist /= 2;

        if (dist == 0 || ch.properties().contains(Char.Property.IMMOVABLE)) return;

        if (Actor.findChar(trajectory.path.get(dist)) != null) {
            dist--;
        }

        final int newPos = trajectory.path.get(dist);

        if (newPos == ch.pos) return;

        final int finalDist = dist;
        final int initialpos = ch.pos;

        Actor.addDelayed(new Pushing(ch, ch.pos, newPos, new Callback() {
            public void call() {
                if (initialpos != ch.pos) {
                    //something cased movement before pushing resolved, cancel to be safe.
                    ch.sprite.place(ch.pos);
                    return;
                }
                ch.pos = newPos;
                if (ch.pos == trajectory.collisionPos) {
                    ch.damage(new AbsoluteDamage(Random.NormalIntRange((finalDist + 1) / 2, finalDist), this, ch), this);
                    Paralysis.prolong(ch, Paralysis.class, Random.NormalIntRange((finalDist + 1) / 2, finalDist));
                }
                Dungeon.level.press(ch.pos, ch);
            }
        }), -1);
    }

    public int min(int lvl) {
        return 1 + lvl;
    }

    public int max(int lvl) {
        return 5 + 3 * lvl;
    }

    @Override
    protected void onZap(Ballistica bolt) {
        Sample.INSTANCE.play(Assets.SND_BLAST);

        BlastWave.blast(bolt.collisionPos);

        int damage = damageRoll();

        //presses all tiles in the AOE first
        for (int i : PathFinder.NEIGHBOURS9) {
            Dungeon.level.press(bolt.collisionPos + i, Actor.findChar(bolt.collisionPos + i));
        }


        //throws other chars around the center.
        for (int i : PathFinder.NEIGHBOURS8) {
            Char ch = Actor.findChar(bolt.collisionPos + i);
            if (ch != null) {

                processSoulMark(ch, chargesPerCast());
                PhysicalDamage dmg = new PhysicalDamage();
                dmg.AddImpact(Math.round(damage * 0.667f));
                ch.damage(dmg, this);

                if (ch.isAlive()) {
                    //Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                    int strength = 2 + Math.round(level() / 2f);

                    //Integer chpos = ch.pos;
                    //Integer userpos = curUser.pos;

                    //GLog.n(bolt.collisionPos.toString());
                    //GLog.h(chpos.toString());
                    //GLog.w(userpos.toString());

                    Pushing.knockback( ch, bolt.collisionPos, strength);
                }
            }
        }

        //throws the char at the center of the blast
        Char ch = Actor.findChar(bolt.collisionPos);
        if (ch != null) {
            processSoulMark(ch, chargesPerCast());
            PhysicalDamage dmg = new PhysicalDamage();
            dmg.AddImpact(Math.round(damage));
            ch.damage(dmg, this);
            //ch.damage(new AbsoluteDamage(damage,this,ch), this);
            if (ch.isAlive() )
                //&& bolt.path.size() > bolt.dist + 1) {
                //Ballistica trajectory = new Ballistica(ch.pos, bolt.path.get(bolt.dist + 1), Ballistica.MAGIC_BOLT);
            {
                int strength = level() + 3;

                //Integer chpos = ch.pos;
                //Integer userpos = curUser.pos;

                //GLog.w(chpos.toString());
                //GLog.h(userpos.toString());

                Pushing.knockback( ch, curUser.pos, strength);
                //throwChar(ch, trajectory, strength);
            }
        }
        if (!curUser.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(this, "ondeath"));
        }
    }

    @Override
    //behaves just like glyph of Repulsion
    public void onHit(MagesStaff staff, Char attacker, Char defender, Damage damage) {
        int level = Math.max(0, staff.level());
        // lvl 0 - 25%
        // lvl 1 - 40%
        // lvl 2 - 50%
        if (Random.Int(level + 4) >= 3) {
            int oppositeHero = defender.pos + (defender.pos - attacker.pos);

            Pushing.knockback(defender,curUser.pos,2);

            //Ballistica trajectory = new Ballistica(defender.pos, oppositeHero, Ballistica.MAGIC_BOLT);
            //throwChar(defender, trajectory, 2);
        }
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.slowness(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0x664422);
        particle.am = 0.6f;
        particle.setLifespan(3f);
        particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
        particle.setSize(1f, 2f);
        particle.radiateXY(2.5f);
    }

    public static class BlastWave extends Image {

        private static final float TIME_TO_FADE = 0.2f;

        private float time;

        public BlastWave() {
            super(Effects.get(Effects.Type.RIPPLE));
            origin.set(width / 2, height / 2);
        }

        public static void blast(int pos) {
            Group parent = Dungeon.hero.sprite.parent;
            BlastWave b = (BlastWave) parent.recycle(BlastWave.class);
            parent.bringToFront(b);
            b.reset(pos);
        }

        public void reset(int pos) {
            revive();

            x = (pos % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
            y = (pos / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

            time = TIME_TO_FADE;
        }

        @Override
        public void update() {
            super.update();

            if ((time -= Game.elapsed) <= 0) {
                kill();
            } else {
                float p = time / TIME_TO_FADE;
                alpha(p);
                scale.y = scale.x = (1 - p) * 3;
            }
        }

    }
}
