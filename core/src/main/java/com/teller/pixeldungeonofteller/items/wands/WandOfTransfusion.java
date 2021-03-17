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
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Charm;
import com.teller.pixeldungeonofteller.actors.buffs.Corruption;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.effects.Beam;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.effects.particles.BloodParticle;
import com.teller.pixeldungeonofteller.effects.particles.LeafParticle;
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.MagesStaff;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.plants.Plant;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfTransfusion extends Wand {

    private static final String FREECHARGE = "freecharge";
    private boolean freeCharge = false;

    {
        image = ItemSpriteSheet.WAND_TRANSFUSION;

        collisionProperties = Ballistica.PROJECTILE;
    }

    @Override
    protected void onZap(Ballistica beam) {

        for (int c : beam.subPath(0, beam.dist))
            CellEmitter.center(c).burst(BloodParticle.BURST, 1);

        int cell = beam.collisionPos;

        Char ch = Actor.findChar(cell);
        Heap heap = Dungeon.level.heaps.get(cell);

        //this wand does a bunch of different things depending on what it targets.

        //if we find a character..
        if (ch != null && ch instanceof Mob) {

            processSoulMark(ch, chargesPerCast());

            //heals an ally, or charmed/corrupted enemy
            if (((Mob) ch).ally || ch.buff(Charm.class) != null || ch.buff(Corruption.class) != null) {

                int missingHP = ch.HT - ch.HP;
                //heals 30%+3%*lvl missing HP.
                int healing = (int) Math.ceil((missingHP * (0.30f + (0.03f * level()))));
                ch.HP += healing;
                ch.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 + level() / 2);
                ch.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healing);

                //harms the undead
            } else if (ch.properties().contains(Char.Property.UNDEAD)) {

                //deals 30%+5%*lvl total HP.
                int damage = (int) Math.ceil(ch.HT * (0.3f + (0.05f * level())));

                MagicalDamage dmg = new MagicalDamage();
                dmg.AddHoly(damage);
                ch.damage(dmg, this);

                ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10 + level());
                Sample.INSTANCE.play(Assets.SND_BURNING);

                //charms an enemy
            } else {

                float duration = 5 + level();
                Buff.affect(ch, Charm.class, Charm.durationFactor(ch) * duration).object = curUser.id();

                duration *= Random.Float(0.75f, 1f);
                Buff.affect(curUser, Charm.class, Charm.durationFactor(ch) * duration).object = ch.id();

                ch.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                curUser.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);

            }


            //if we find an item...
        } else if (heap != null && heap.type == Heap.Type.HEAP) {
            Item item = heap.peek();

            //30% + 10%*lvl chance to uncurse the item and reset it to base level if degraded.
            if (item != null && Random.Float() <= 0.3f + level() * 0.1f) {
                if (item.cursed) {
                    item.cursed = false;
                    CellEmitter.get(cell).start(ShadowParticle.UP, 0.05f, 10);
                    Sample.INSTANCE.play(Assets.SND_BURNING);
                }

                int lvldiffFromBase = item.level() - (item instanceof Ring ? 1 : 0);
                if (lvldiffFromBase < 0) {
                    item.upgrade(-lvldiffFromBase);
                    CellEmitter.get(cell).start(Speck.factory(Speck.UP), 0.2f, 3);
                    Sample.INSTANCE.play(Assets.SND_BURNING);
                }
            }

            //if we find some trampled grass...
        } else if (Dungeon.level.map[cell] == Terrain.GRASS) {

            //regrow one grass tile, suuuuuper useful...
            Level.set(cell, Terrain.HIGH_GRASS);
            GameScene.updateMap(cell);
            CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);

            //If we find embers...
        } else if (Dungeon.level.map[cell] == Terrain.EMBERS) {

            //30% + 3%*lvl chance to grow a random plant, or just regrow grass.
            if (Random.Float() <= 0.3f + level() * 0.03f) {
                Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), cell);
                CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 8);
                GameScene.updateMap(cell);
            } else {
                Level.set(cell, Terrain.HIGH_GRASS);
                GameScene.updateMap(cell);
                CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
            }

        } else
            return; //don't damage the hero if we can't find a target;

        if (!freeCharge) {
            damageHero();
        } else {
            freeCharge = false;
        }
    }

    //this wand costs health too
    private void damageHero() {
        // 15% of max hp
        int damage = (int) Math.ceil(curUser.HT * 0.15f);

        MagicalDamage dmg = new MagicalDamage();
        dmg.AddHollow(damage);
        curUser.damage(dmg, this);

        if (!curUser.isAlive()) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(this, "ondeath"));
        }
    }

    @Override
    protected int initialCharges() {
        return 1;
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, Damage damage) {
        // lvl 0 - 10%
        // lvl 1 - 18%
        // lvl 2 - 25%
        if (Random.Int(level() + 10) >= 9) {
            //grants a free use of the staff
            freeCharge = true;
            GLog.p(Messages.get(this, "charged"));
            attacker.sprite.emitter().burst(BloodParticle.BURST, 20);
        }
    }

    @Override
    protected void fx(Ballistica beam, Callback callback) {
        curUser.sprite.parent.add(
                new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.tileCenterToWorld(beam.collisionPos)));
        callback.call();
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0xCC0000);
        particle.am = 0.6f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.PI2), 2f);
        particle.setSize(1f, 2f);
        particle.radiateXY(0.5f);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        freeCharge = bundle.getBoolean(FREECHARGE);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FREECHARGE, freeCharge);
    }

}
