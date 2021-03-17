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
import com.teller.pixeldungeonofteller.actors.blobs.Blob;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.LockedFloor;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.effects.particles.ElmoParticle;
import com.teller.pixeldungeonofteller.items.artifacts.CapeOfThorns;
import com.teller.pixeldungeonofteller.items.artifacts.LloydsBeacon;
import com.teller.pixeldungeonofteller.items.keys.SkeletonKey;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfPsionicBlast;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.DM300Sprite;
import com.teller.pixeldungeonofteller.ui.BossHealthBar;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class DM300 extends Mob {

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(Grim.class);
        RESISTANCES.add(ScrollOfPsionicBlast.class);
    }

    static {
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Bleeding.class);
    }

    {
        spriteClass = DM300Sprite.class;

        HP = HT = 0;
        ARMOR = 70;
        SlashThreshold=18;
        SHLD = 100;
        MAXSHLD = 100;

        EXP = 30;
        defenseSkill = 18;

        loot = new CapeOfThorns().identify();
        lootChance = 0.333f;

        properties.add(Property.BOSS);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(4, 10));
        dmg.AddPuncture(Random.NormalIntRange(5, 10));
        dmg.AddImpact(Random.NormalIntRange(6, 12));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 28;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
    }

    @Override
    public boolean act() {

        GameScene.add(Blob.seed(pos, 30, ToxicGas.class));

        return super.act();
    }

    @Override
    public void move(int step) {
        super.move(step);

        if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && ARMOR < 70) {
            ARMOR += Random.Int(1, 70 - ARMOR);
            //HP += Random.Int( 1, HT - HP );
            sprite.emitter().burst(ElmoParticle.FACTORY, 5);
            if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
                GLog.n(Messages.get(this, "repair"));
            }
        }

        int[] cells = {
                step - 1, step + 1, step - Dungeon.level.width(), step + Dungeon.level.width(),
                step - 1 - Dungeon.level.width(),
                step - 1 + Dungeon.level.width(),
                step + 1 - Dungeon.level.width(),
                step + 1 + Dungeon.level.width()
        };
        int cell = cells[Random.Int(cells.length)];

        if (Dungeon.visible[cell]) {
            CellEmitter.get(cell).start(Speck.factory(Speck.ROCK), 0.07f, 10);
            Camera.main.shake(3, 0.7f);
            Sample.INSTANCE.play(Assets.SND_ROCKS);

            if (Dungeon.level.water[cell]) {
                GameScene.ripple(cell);
            } else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
                Level.set(cell, Terrain.EMPTY_DECO);
                GameScene.updateMap(cell);
            }
        }

        Char ch = Actor.findChar(cell);
        if (ch != null && ch != this) {
            Buff.prolong(ch, Paralysis.class, 2);
        }
    }

    @Override
    public void damage(Damage dmg, Object src) {
        super.damage(dmg, src);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !immunities().contains(src.getClass()))
            lock.addTime((dmg.effictivehpdamage + dmg.effictiveslddamage) * 1.5f);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        GameScene.bossSlain();
        Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();

        Badges.validateBossSlain();

        LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
        if (beacon != null) {
            beacon.upgrade();
        }

        yell(Messages.get(this, "defeated"));
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell(Messages.get(this, "notice"));
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
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

    public boolean isAlive() {
        return SHLD > 0;
    }
}
