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
package com.teller.pixeldungeonofteller.actors.mobs.npcs;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.blobs.ToxicGas;
import com.teller.pixeldungeonofteller.actors.blobs.VenomGas;
import com.teller.pixeldungeonofteller.actors.buffs.Burning;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.MirrorSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class MirrorImage extends NPC {

    private static final String TIER = "tier";
    private static final String ATTACK = "attack";
    private static final String DAMAGE = "damage";
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(VenomGas.class);
        IMMUNITIES.add(Burning.class);
    }

    public int tier;
    private int attack;
    private int damage;

    {
        spriteClass = MirrorSprite.class;

        state = HUNTING;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TIER, tier);
        bundle.put(ATTACK, attack);
        bundle.put(DAMAGE, damage);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        tier = bundle.getInt(TIER);
        attack = bundle.getInt(ATTACK);
        damage = bundle.getInt(DAMAGE);
    }

    public void duplicate(Hero hero) {
        tier = hero.tier();
        attack = hero.attackSkill(hero);
        damage = hero.damageRoll().effictivehpdamage;
    }

    @Override
    public int attackSkill(Char target) {
        return attack;
    }

    @Override
    public PhysicalDamage damageRoll() {
        return new PhysicalDamage((hero.belongings.mainhandweapon.max() + hero.belongings.mainhandweapon.min()) / 2, new PhysicalPercentage(0.33f, 0.33f, 0.33f));
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        super.attackProc(enemy, damage);
        destroy();
        sprite.die();
        return damage;
    }

    protected Char chooseEnemy() {

        if (enemy == null || !enemy.isAlive()) {
            HashSet<Mob> enemies = new HashSet<>();
            for (Mob mob : Dungeon.level.mobs) {
                if (mob.hostile && fieldOfView[mob.pos]) {
                    enemies.add(mob);
                }
            }

            enemy = enemies.size() > 0 ? Random.element(enemies) : null;
        }

        return enemy;
    }

    @Override
    public CharSprite sprite() {
        CharSprite s = super.sprite();
        ((MirrorSprite) s).updateArmor(tier);
        return s;
    }

    @Override
    public boolean interact() {

        int curPos = pos;

        moveSprite(pos, hero.pos);
        move(hero.pos);

        hero.sprite.move(hero.pos, curPos);
        hero.move(curPos);

        hero.spend(1 / hero.speed());
        hero.busy();

        return true;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}