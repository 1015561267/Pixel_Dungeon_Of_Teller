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
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.LockedFloor;
import com.teller.pixeldungeonofteller.actors.buffs.Poison;
import com.teller.pixeldungeonofteller.actors.hero.HeroSubClass;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.TomeOfMastery;
import com.teller.pixeldungeonofteller.items.artifacts.LloydsBeacon;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicMapping;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfPsionicBlast;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.PrisonBossLevel;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.traps.SpearTrap;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.TenguSprite;
import com.teller.pixeldungeonofteller.ui.BossHealthBar;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Tengu extends Mob {

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    private boolean ranged=false;

    static {
        RESISTANCES.add(ToxicGas.class);
        RESISTANCES.add(Poison.class);
        RESISTANCES.add(Grim.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
    }

    {
        spriteClass = TenguSprite.class;

        HP = HT = 80;
        ARMOR = 30;
        SlashThreshold=8;
        SHLD = 0;
        MAXSHLD = 0;

        EXP = 20;
        defenseSkill = 20;

        ranged=false;

        HUNTING = new Hunting();

        flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards

        properties.add(Property.BOSS);
    }

    @Override
    protected void onAdd() {
        //when he's removed and re-added to the fight, his time is always set to now.
        spend(-cooldown());
        super.onAdd();
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 15));
        dmg.AddPuncture(Random.NormalIntRange(1, 6));
        dmg.AddImpact(Random.NormalIntRange(1, 6));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 5);
    }

    @Override
    public void damage(Damage dmg, Object src) {
        int beforeHitHP = HP;
        super.damage(dmg, src);
        dmg.effictivehpdamage = beforeHitHP - HP;

        beforeHitHP=dmg.effictivearmordamage+dmg.effictivehpdamage+dmg.effictiveslddamage;

        GLog.w(" beforeHitHP=%d", beforeHitHP);

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = beforeHitHP > HT / 2 ? 1 : 4;
            lock.addTime((dmg.effictivehpdamage + dmg.effictiveslddamage) * multiple);
        }

        //phase 2 of the fight is over
        //if (HP == 0 && beforeHitHP <= HT / 2 && ranged) {
        if (HP == 0 && ranged) {
            GLog.w(" fight2 over");
            ((PrisonBossLevel) Dungeon.level).progress();
            return;
        }

        //int hpBracket = beforeHitHP > HT / 2 ? 12 : 20;

        int hpBracket = ranged ? 12 : 20;

        //phase 1 of the fight is over
        // if (beforeHitHP > HT/2 && HP <= HT/2){

        if ( HP <= 40 ) {
            //HP = (HT / 2) - 1;
            if(!ranged) {
                ranged=true;//FIXME:Implementing shield and armor cause unwanted bug here,tengu's meachine won't work properly if I just copy the raw code
                //if(HP <= 40){ HP = 40;}
                HP = 39;
                ARMOR=30;
                yell(Messages.get(this, "interesting"));
                GLog.w(" fight1 over");
                ((PrisonBossLevel) Dungeon.level).progress();
                BossHealthBar.bleed(true);
            }
            else if(beforeHitHP / hpBracket != 150 / hpBracket)
            {
                jump();
                GLog.w(" fight2 jump");
            }
            //if tengu has lost a certain amount of hp, jump
        } else if// (beforeHitHP / hpBracket != HP / hpBracket) {
            (beforeHitHP / hpBracket != 150 / hpBracket) {
            jump();
            GLog.w(" fight1 jump");
        }
    }

    @Override
    public boolean isAlive() {
        return Dungeon.level.mobs.contains(this) && HP > 0; //Tengu has special death rules, see prisonbosslevel.progress()
    }

    @Override
    public void die(Object cause) {

        if (Dungeon.hero.subClass == HeroSubClass.NONE) {
            Dungeon.level.drop(new TomeOfMastery(), pos).sprite.drop();
        }

        GameScene.bossSlain();
        super.die(cause);

        Badges.validateBossSlain();

        LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
        if (beacon != null) {
            beacon.upgrade();
        }

        yell(Messages.get(this, "defeated"));
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
    }

    //tengu's attack is always visible
    @Override
    protected boolean doAttack(Char enemy) {
        if (enemy == Dungeon.hero)
            Dungeon.hero.resting = false;
        sprite.attack(enemy.pos);
        spend(attackDelay());
        return true;
    }

    private void jump() {

        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
            fieldOfView = new boolean[Dungeon.level.length()];
            Dungeon.level.updateFieldOfView( this, fieldOfView );
        }

        if (enemy == null) enemy = chooseEnemy();
        if (enemy == null) return;

        for (int i = 0; i < 4; i++) {
            int trapPos;
            do {
                trapPos = Random.Int(Dungeon.level.length());
            } while (!fieldOfView[trapPos] || Dungeon.level.solid[trapPos]);
            if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
                Dungeon.level.setTrap(new SpearTrap().reveal(), trapPos);
                Level.set(trapPos, Terrain.TRAP);
                ScrollOfMagicMapping.discover(trapPos);
            }
        }
        if (enemy == null) enemy = chooseEnemy();
        int newPos;
        //if we're in phase 1, want to warp around within the room
        //if (HP > HT / 2) {
        if (!ranged) {
            do {
                newPos = Random.Int(Dungeon.level.length());
            } while (
                    !(Dungeon.level.map[newPos] == Terrain.INACTIVE_TRAP || Dungeon.level.map[newPos] == Terrain.TRAP) ||
                            Dungeon.level.solid[newPos] ||
                            Dungeon.level.adjacent(newPos, enemy.pos) ||
                            Actor.findChar(newPos) != null);

            //otherwise go wherever, as long as it's a little bit away
        } else {
            do {
                newPos = Random.Int(Dungeon.level.length());
            } while (
                    Dungeon.level.solid[newPos] ||
                            Dungeon.level.distance(newPos, enemy.pos) < 8 ||
                            Actor.findChar(newPos) != null);
        }

        if (Dungeon.visible[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);


        sprite.move(pos, newPos);
        move(newPos);

        if (Dungeon.visible[newPos]) CellEmitter.get(newPos).burst(Speck.factory(Speck.WOOL), 6);
        Sample.INSTANCE.play(Assets.SND_PUFF);

        spend(1 / speed());
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        if (HP <= HT / 2) BossHealthBar.bleed(true);
        if (HP == HT) {
            yell(Messages.get(this, "notice_mine", Dungeon.hero.givenName()));
        } else {
            yell(Messages.get(this, "notice_face", Dungeon.hero.givenName()));
        }
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        BossHealthBar.assignBoss(this);
        //if (HP <= HT / 2) BossHealthBar.bleed(true);
        if (ranged) BossHealthBar.bleed(true);
    }

    //tengu is always hunting
    private class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy(enemy) && canAttack(enemy)) {

                return doAttack(enemy);

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else {
                    chooseEnemy();
                    target = enemy.pos;
                }

                spend(TICK);
                return true;

            }
        }
    }
}
