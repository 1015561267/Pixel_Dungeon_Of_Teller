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

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.Blob;
import com.teller.pixeldungeonofteller.actors.blobs.Fire;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Amok;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.buffs.Charm;
import com.teller.pixeldungeonofteller.actors.buffs.LockedFloor;
import com.teller.pixeldungeonofteller.actors.buffs.Ooze;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.actors.buffs.Sleep;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.actors.buffs.Vertigo;
import com.teller.pixeldungeonofteller.effects.Pushing;
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.items.keys.SkeletonKey;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfPsionicBlast;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.BurningFistSprite;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.LarvaSprite;
import com.teller.pixeldungeonofteller.sprites.RottingFistSprite;
import com.teller.pixeldungeonofteller.sprites.YogSprite;
import com.teller.pixeldungeonofteller.ui.BossHealthBar;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Yog extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {

        IMMUNITIES.add(Grim.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Charm.class);
        IMMUNITIES.add(Sleep.class);
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(ScrollOfPsionicBlast.class);
        IMMUNITIES.add(Vertigo.class);
    }

    {
        spriteClass = YogSprite.class;

        HP = HT = 750;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 0;
        MAXSHLD = 0;

        EXP = 50;

        state = PASSIVE;

        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.DEMONIC);
    }

    public Yog() {
        super();
    }

    public void spawnFists() {
        RottingFist fist1 = new RottingFist();
        BurningFist fist2 = new BurningFist();

        do {
            fist1.pos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
            fist2.pos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!Dungeon.level.passable[fist1.pos] || !Dungeon.level.passable[fist2.pos] || fist1.pos == fist2.pos);

        GameScene.add(fist1);
        GameScene.add(fist2);

        notice();
    }

    @Override
    protected boolean act() {
        //heals 1 health per turn
        HP = Math.min(HT, HP + 1);

        return super.act();
    }

    @Override
    public void damage(Damage dmg, Object src) {

        HashSet<Mob> fists = new HashSet<>();

        for (Mob mob : Dungeon.level.mobs)
            if (mob instanceof RottingFist || mob instanceof BurningFist)
                fists.add(mob);

        for (Mob fist : fists)
            fist.beckon(pos);

        //dmg >>= fists.size();//Yog know has no bonus defense...for now

        if(fists.size()>0)
        {
            if(fists.size()==1) dmg.multiplie(0.5f);
            else dmg.multiplie(0.25f);
        }

        super.damage(dmg, src);


        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime((dmg.effictivehpdamage + dmg.effictiveslddamage) * 0.5f);

    }

    @Override
    public Damage defenseProc(Char enemy, Damage damage) {

        ArrayList<Integer> spawnPoints = new ArrayList<>();

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                spawnPoints.add(p);
            }
        }

        if (spawnPoints.size() > 0) {
            Larva larva = new Larva();
            larva.pos = Random.element(spawnPoints);

            GameScene.add(larva);
            Actor.addDelayed(new Pushing(larva, pos, larva.pos), -1);
        }

        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof BurningFist || mob instanceof RottingFist || mob instanceof Larva) {
                mob.aggro(enemy);
            }
        }

        return super.defenseProc(enemy, damage);
    }

    @Override
    public void beckon(int cell) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void die(Object cause) {

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof BurningFist || mob instanceof RottingFist) {
                mob.die(cause);
            }
        }

        GameScene.bossSlain();
        Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
        super.die(cause);

        yell(Messages.get(this, "defeated"));
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell(Messages.get(this, "notice"));
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        BossHealthBar.assignBoss(this);
    }

    public static class RottingFist extends Mob {

        private static final int REGENERATION = 4;
        private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
        private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

        static {
            RESISTANCES.add(ToxicGas.class);
            RESISTANCES.add(Grim.class);
            RESISTANCES.add(ScrollOfPsionicBlast.class);
        }

        static {
            IMMUNITIES.add(Amok.class);
            IMMUNITIES.add(Sleep.class);
            IMMUNITIES.add(Terror.class);
            IMMUNITIES.add(Poison.class);
            IMMUNITIES.add(Vertigo.class);
        }

        {
            spriteClass = RottingFistSprite.class;

            HP = HT = 300;
            ARMOR = 100;
            SlashThreshold=24;
            SHLD = 50;
            MAXSHLD = 50;

            defenseSkill = 25;

            EXP = 0;

            state = WANDERING;

            properties.add(Property.BOSS);
            properties.add(Property.DEMONIC);
        }

        @Override
        public int attackSkill(Char target) {
            return 36;
        }

        @Override
        public PhysicalDamage damageRoll() {
            PhysicalDamage dmg = new PhysicalDamage();
            dmg.AddSlash(Random.NormalIntRange(5, 18));
            dmg.AddPuncture(Random.NormalIntRange(5, 20));
            dmg.AddImpact(Random.NormalIntRange(5, 24));
            return dmg;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 15);
        }

        @Override
        public Damage attackProc(Char enemy, Damage damage) {
            if (Random.Int(3) == 0) {
                Buff.affect(enemy, Ooze.class);
                enemy.sprite.burst(0xFF000000, 5);
            }
            return damage;
        }

        @Override
        public boolean act() {

            if (Dungeon.level.water[pos] && HP < HT) {
                sprite.emitter().burst(ShadowParticle.UP, 2);
                HP += REGENERATION;
            }

            return super.act();
        }

        @Override
        public void damage(Damage dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null) lock.addTime((dmg.effictivehpdamage + dmg.effictiveslddamage) * 0.5f);
        }

        @Override
        public HashSet<Class<?>> resistances() {
            return RESISTANCES;
        }

        @Override
        public HashSet<Class<?>> immunities() {
            return IMMUNITIES;
        }
    }

    public static class BurningFist extends Mob {

        private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
        private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

        static {
            RESISTANCES.add(ToxicGas.class);
            RESISTANCES.add(Grim.class);

        }

        static {
            IMMUNITIES.add(Amok.class);
            IMMUNITIES.add(Sleep.class);
            IMMUNITIES.add(Terror.class);
            IMMUNITIES.add(Burning.class);
            IMMUNITIES.add(ScrollOfPsionicBlast.class);
            IMMUNITIES.add(Vertigo.class);
        }

        {
            spriteClass = BurningFistSprite.class;

            HP = HT = 250;
            ARMOR = 75;
            SlashThreshold=20;
            SHLD = 75;
            MAXSHLD = 75;

            defenseSkill = 25;

            EXP = 0;

            state = WANDERING;

            properties.add(Property.BOSS);
            properties.add(Property.DEMONIC);
        }

        @Override
        public int attackSkill(Char target) {
            return 36;
        }

        @Override
        public Damage damageRoll() {
            MagicalDamage dmg = new MagicalDamage();
            dmg.AddCursedFlame(Random.NormalIntRange(18, 28));
            return dmg;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 15);
        }

        @Override
        protected boolean canAttack(Char enemy) {
            return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        }

        @Override
        public boolean attack(Char enemy) {

            if (!Dungeon.level.adjacent(pos, enemy.pos)) {
                spend(attackDelay());

                if (hit(this, enemy, true)) {

                    Damage dmg = damageRoll();
                    enemy.damage(dmg, this);

                    enemy.sprite.bloodBurstA(sprite.center(), dmg.effictivehpdamage);
                    enemy.sprite.flash();

                    if (!enemy.isAlive() && enemy == Dungeon.hero) {
                        Dungeon.fail(getClass());
                        GLog.n(Messages.get(Char.class, "kill", name));
                    }
                    return true;

                } else {
                    enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
                    return false;
                }
            } else {
                return super.attack(enemy);
            }
        }

        @Override
        public boolean act() {

            for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
                GameScene.add(Blob.seed(pos + PathFinder.NEIGHBOURS9[i], 2, Fire.class));
            }

            return super.act();
        }

        @Override
        public void damage(Damage dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null) lock.addTime((dmg.effictivehpdamage + dmg.effictiveslddamage) * 0.5f);
        }

        @Override
        public HashSet<Class<?>> resistances() {
            return RESISTANCES;
        }

        @Override
        public HashSet<Class<?>> immunities() {
            return IMMUNITIES;
        }
    }

    public static class Larva extends Mob {
        {
            spriteClass = LarvaSprite.class;

            HP = HT = 25;
            ARMOR = 0;
            SlashThreshold=0;
            SHLD = 10;
            MAXSHLD = 10;

            defenseSkill = 20;

            EXP = 0;

            state = HUNTING;

            properties.add(Property.DEMONIC);
        }

        @Override
        public int attackSkill(Char target) {
            return 30;
        }

        @Override
        public PhysicalDamage damageRoll() {
            PhysicalDamage dmg = new PhysicalDamage();
            dmg.AddSlash(Random.NormalIntRange(5, 18));
            dmg.AddPuncture(Random.NormalIntRange(2, 10));
            dmg.AddImpact(Random.NormalIntRange(5, 12));
            return dmg;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 8);
        }

    }
}
