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
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.effects.particles.SparkParticle;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.traps.LightningTrap;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ShamanSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Shaman extends Mob implements Callback {

    private static final float TIME_TO_ZAP = 1f;
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    static {
        RESISTANCES.add(LightningTrap.Electricity.class);
    }

    {
        spriteClass = ShamanSprite.class;

        HP = HT = 10;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 15;
        MAXSHLD = SHLD;

        defenseSkill = 8;

        EXP = 6;
        maxLvl = 14;

        loot = Generator.Category.SCROLL;
        lootChance = 0.33f;
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(1, 4));
        dmg.AddImpact(Random.NormalIntRange(1,3));
        return dmg;
    }

    @Override
    public int attackSkill(Char target) {
        return 11;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    protected boolean doAttack(Char enemy) {

        if (Dungeon.level.distance(pos, enemy.pos) <= 1) {

            return super.doAttack(enemy);

        } else {

            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
            if (visible) {
                sprite.zap(enemy.pos);
            }

            spend(TIME_TO_ZAP);

            if (hit(this, enemy, true)) {
                int dmg = Random.NormalIntRange(7, 12);
                if (Dungeon.level.water[enemy.pos] && !enemy.flying) {
                    dmg *= 1.5f;
                }

                MagicalDamage damage = new MagicalDamage();
                damage.AddLightning(dmg);
                enemy.damage(damage, LightningTrap.LIGHTNING);

                enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                enemy.sprite.flash();

                if (enemy == Dungeon.hero) {

                    Camera.main.shake(2, 0.3f);

                    if (!enemy.isAlive()) {
                        Dungeon.fail(getClass());
                        GLog.n(Messages.get(this, "zap_kill"));
                    }
                }
            } else {
                enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
            }
            return !visible;
        }
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }
}
