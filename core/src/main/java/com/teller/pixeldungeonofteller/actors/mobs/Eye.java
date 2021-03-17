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
import com.teller.pixeldungeonofteller.actors.buffs.Light;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.particles.PurpleParticle;
import com.teller.pixeldungeonofteller.items.Dewdrop;
import com.teller.pixeldungeonofteller.items.wands.WandOfDisintegration;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Vampiric;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.EyeSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Eye extends Mob {

    private static final String BEAM_TARGET = "beamTarget";
    private static final String BEAM_COOLDOWN = "beamCooldown";
    private static final String BEAM_CHARGED = "beamCharged";
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(WandOfDisintegration.class);
        RESISTANCES.add(Grim.class);
        RESISTANCES.add(Vampiric.class);
    }

    static {
        IMMUNITIES.add(Terror.class);
    }

    public boolean beamCharged;
    private Ballistica beam;
    private int beamTarget = -1;
    private int beamCooldown;

    {
        spriteClass = EyeSprite.class;

        HP = HT = 15;
        ARMOR = 10;
        SlashThreshold=12;
        SHLD = 120;
        MAXSHLD = 120;

        defenseSkill = 20;
        viewDistance = Light.DISTANCE;

        EXP = 13;
        maxLvl = 25;

        flying = true;

        HUNTING = new Hunting();

        loot = new Dewdrop();
        lootChance = 0.5f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 10));
        dmg.AddImpact(Random.NormalIntRange(6, 26));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    protected boolean canAttack(Char enemy) {

        if (beamCooldown == 0) {
            Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);

            if (enemy.invisible == 0 && fieldOfView[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos)) {
                beam = aim;
                beamTarget = aim.collisionPos;
                return true;
            } else
                //if the beam is charged, it has to attack, will aim at previous location of hero.
                return beamCharged;
        } else
            return super.canAttack(enemy);
    }

    @Override
    protected boolean act() {
        if (beam == null && beamTarget != -1) {
            beam = new Ballistica(pos, beamTarget, Ballistica.STOP_TERRAIN);
            sprite.turnTo(pos, beamTarget);
        }
        if (beamCooldown > 0)
            beamCooldown--;
        return super.act();
    }

    @Override
    protected Char chooseEnemy() {
        if (beamCharged && enemy != null) return enemy;
        return super.chooseEnemy();
    }

    @Override
    protected boolean doAttack(Char enemy) {

        if (beamCooldown > 0) {
            return super.doAttack(enemy);
        } else if (!beamCharged) {
            ((EyeSprite) sprite).charge(enemy.pos);
            spend(attackDelay() * 2f);
            beamCharged = true;
            return true;
        } else {

            spend(attackDelay());

            if (Dungeon.visible[pos]) {
                sprite.zap(beam.collisionPos);
                return false;
            } else {
                deathGaze();
                return true;
            }
        }

    }

    @Override
    public void damage(Damage dmg, Object src) {
        //if (beamCharged) dmg /= 4; //Eye!You will die today!
        if (beamCharged) dmg.multiplie(0.25f);
        super.damage(dmg, src);
    }

    public void deathGaze() {
        if (!beamCharged || beamCooldown > 0 || beam == null)
            return;

        beamCharged = false;
        beamCooldown = Random.IntRange(3, 6);

        boolean terrainAffected = false;

        for (int pos : beam.subPath(1, beam.dist)) {

            if (Dungeon.level.flamable[pos]) {

                Dungeon.level.destroy(pos);
                GameScene.updateMap(pos);
                terrainAffected = true;

            }

            Char ch = Actor.findChar(pos);
            if (ch == null) {
                continue;
            }

            if (hit(this, ch, true)) {
                    MagicalDamage dmg = new MagicalDamage();
                    dmg.AddArcane(Random.NormalIntRange(45, 60));
                    ch.damage(dmg, this);
                if (Dungeon.visible[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }
                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "deathgaze_kill"));
                }
            } else {
                ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        beam = null;
        beamTarget = -1;
        sprite.idle();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BEAM_TARGET, beamTarget);
        bundle.put(BEAM_COOLDOWN, beamCooldown);
        bundle.put(BEAM_CHARGED, beamCharged);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(BEAM_TARGET))
            beamTarget = bundle.getInt(BEAM_TARGET);
        beamCooldown = bundle.getInt(BEAM_COOLDOWN);
        beamCharged = bundle.getBoolean(BEAM_CHARGED);
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    private class Hunting extends Mob.Hunting {
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            //always attack if the beam is charged, no exceptions
            if (beamCharged && enemy != null)
                enemyInFOV = true;
            return super.act(enemyInFOV, justAlerted);
        }
    }
}
