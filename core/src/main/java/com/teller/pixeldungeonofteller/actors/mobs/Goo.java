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
package com.teller.pixeldungeonofteller.actors.mobs;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.Blob;
import com.teller.pixeldungeonofteller.actors.blobs.GooWarn;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.LockedFloor;
import com.teller.pixeldungeonofteller.actors.buffs.Ooze;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.effects.particles.ElmoParticle;
import com.teller.pixeldungeonofteller.items.artifacts.LloydsBeacon;
import com.teller.pixeldungeonofteller.items.keys.SkeletonKey;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfPsionicBlast;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.GooSprite;
import com.teller.pixeldungeonofteller.ui.BossHealthBar;
import com.teller.pixeldungeonofteller.utils.BArray;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Goo extends Mob {

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(ToxicGas.class);
        RESISTANCES.add(Grim.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
    }

    private final String PUMPEDUP = "pumpedup";
    private int pumpedUp = 0;

    {
        HP = HT = 60;
        ARMOR = 15;
        SlashThreshold=3;
        SHLD = 5;
        MAXSHLD = 5;

        EXP = 10;
        defenseSkill = 8;
        spriteClass = GooSprite.class;

        loot = new LloydsBeacon().identify();
        lootChance = 0.333f;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();

        int Slashmin = (HP * 3 / 2 <= HT) ? 2  : 1 ;
        int Slashmax =  8;

        int Puncturemin = (HP * 3 / 2 <= HT) ? 1 : 0;
        int Puncturemax = (HP * 3 / 2 <= HT) ? 2 : 0;

        int Impactmin = 1;
        int Impactmax = 4;

        if (pumpedUp > 0) {
            pumpedUp = 0;
            PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 2);
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE)
                    CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
            }
            Sample.INSTANCE.play(Assets.SND_BURNING);
            dmg.AddSlash(Random.NormalIntRange(Slashmin * 3, Slashmax * 3));
            dmg.AddPuncture(Random.NormalIntRange(Puncturemin * 3, Puncturemax * 3));
            dmg.AddImpact(Random.NormalIntRange(Impactmin * 3, Impactmax * 3));
            return dmg;
        } else {
            dmg.AddSlash(Random.NormalIntRange(Slashmin, Slashmax));
            dmg.AddPuncture(Random.NormalIntRange(Puncturemin, Puncturemax));
            dmg.AddImpact(Random.NormalIntRange(Impactmin, Impactmax));
            return dmg;
        }
    }

    @Override
    public int attackSkill(Char target) {
        int attack = 10;
        if (HP * 3 / 2 <= HT) attack = 15;
        if (pumpedUp > 0) attack *= 2;
        return attack;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return (int) (super.defenseSkill(enemy) * ((HP * 3 / 2 <= HT) ? 1.5 : 1));
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public boolean act() {

        if (Dungeon.level.water[pos] && HP < HT) {
            sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
            if (HP * 2 == HT) {
                BossHealthBar.bleed(false);
                ((GooSprite) sprite).spray(false);
            }
            HP++;
        }

        return super.act();
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return (pumpedUp > 0) ? distance(enemy) <= 2 : super.canAttack(enemy);
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Ooze.class);
            enemy.sprite.burst(0x000000, 5);
        }
        if (pumpedUp > 0) {
            Camera.main.shake(3, 0.2f);
        }

        return damage;
    }

    @Override
    protected boolean doAttack(Char enemy) {
        if (pumpedUp == 1) {
            ((GooSprite) sprite).pumpUp();
            PathFinder.buildDistanceMap(pos, BArray.not(Dungeon.level.solid, null), 2);
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE)
                    GameScene.add(Blob.seed(i, 2, GooWarn.class));
            }
            pumpedUp++;

            spend(attackDelay());

            return true;
        } else if (pumpedUp >= 2 || Random.Int((HP * 3 / 2 <= HT) ? 2 : 5) > 0) {

            boolean visible = Dungeon.visible[pos];

            if (visible) {
                if (pumpedUp >= 2) {
                    ((GooSprite) sprite).pumpAttack();
                } else
                    sprite.attack(enemy.pos);
            } else {
                attack(enemy);
            }

            spend(attackDelay());

            return !visible;

        } else {

            pumpedUp++;

            ((GooSprite) sprite).pumpUp();

            for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
                int j = pos + PathFinder.NEIGHBOURS9[i];
                if (!Dungeon.level.solid[j]) {
                    GameScene.add(Blob.seed(j, 2, GooWarn.class));
                }
            }

            if (Dungeon.visible[pos]) {
                sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "!!!"));
                GLog.n(Messages.get(this, "pumpup"));
            }

            spend(attackDelay());

            return true;
        }
    }

    @Override
    public boolean attack(Char enemy) {
        boolean result = super.attack(enemy);
        pumpedUp = 0;
        return result;
    }

    @Override
    protected boolean getCloser(int target) {
        pumpedUp = 0;
        return super.getCloser(target);
    }

    @Override
    public void move(int step) {
        Dungeon.level.seal();
        super.move(step);
    }

    @Override
    public void damage(Damage dmg, Object src) {
        boolean bleeding = (HP * 3 / 2 <= HT);
        super.damage(dmg, src);
        if ((HP * 2 <= HT) && !bleeding) {
            BossHealthBar.bleed(true);
            GLog.w(Messages.get(this, "enraged_text"));
            sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "enraged"));
            ((GooSprite) sprite).spray(true);
            yell(Messages.get(this, "gluuurp"));
        }
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime((dmg.effictivehpdamage + dmg.effictiveslddamage) * 2);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        Dungeon.level.unseal();

        GameScene.bossSlain();
        Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();

        Badges.validateBossSlain();

        yell(Messages.get(this, "defeated"));
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell(Messages.get(this, "notice"));
    }

    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        bundle.put(PUMPEDUP, pumpedUp);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

        super.restoreFromBundle(bundle);

        pumpedUp = bundle.getInt(PUMPEDUP);
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP * 2 <= HT)) BossHealthBar.bleed(true);

    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }
}
