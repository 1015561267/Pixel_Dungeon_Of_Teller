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
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Amok;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Imp;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.food.Food;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Knuckles;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.MonkSprite;
import com.teller.pixeldungeonofteller.ui.MainHandIndicator;
import com.teller.pixeldungeonofteller.ui.OffHandIndicator;
import com.teller.pixeldungeonofteller.ui.StatusPane;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Monk extends Mob {

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    private static String DISARMHITS = "hitsToDisarm";

    static {
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Terror.class);
    }

    private int hitsToDisarm = 0;

    {
        spriteClass = MonkSprite.class;

        HP = HT = 60;
        ARMOR = 0;
        SlashThreshold=0;
        SHLD = 30;
        MAXSHLD = 30;

        defenseSkill = 30;

        EXP = 11;
        maxLvl = 21;

        loot = new Food();
        lootChance = 0.083f;

        properties.add(Property.UNDEAD);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 6));
        dmg.AddPuncture(Random.NormalIntRange(4, 10));
        dmg.AddImpact(Random.NormalIntRange(4, 12));
        return dmg;

    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    protected float attackDelay() {
        return 0.5f;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    @Override
    public void die(Object cause) {
        Imp.Quest.process(this);
        super.die(cause);
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (enemy == Dungeon.hero) {
            Hero hero = Dungeon.hero;
            KindOfWeapon mainhandweapon = hero.belongings.mainhandweapon;
            KindOfWeapon offhandweapon = hero.belongings.offhandweapon;

            if (mainhandweapon != null && !(mainhandweapon instanceof Knuckles) && !mainhandweapon.cursed) {
                if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(4, 8);
                if (--hitsToDisarm == 0) {
                    hero.belongings.mainhandweapon = null;
                    Dungeon.quickslot.clearItem(mainhandweapon);
                    mainhandweapon.updateQuickslot();
                    GameScene.scene.updateweaponindicator(null,true); //add this to update weaponindicator and avoid bugs
                    Dungeon.level.drop(mainhandweapon, hero.pos).sprite.drop();
                    GLog.w(Messages.get(this, "disarm", mainhandweapon.name()));
                }
            } else if (offhandweapon != null && !(offhandweapon instanceof Knuckles) && !offhandweapon.cursed) {
                if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(4, 8);
                if (--hitsToDisarm == 0) {
                    hero.belongings.offhandweapon = null;
                    Dungeon.quickslot.clearItem(offhandweapon);
                    offhandweapon.updateQuickslot();
                    GameScene.scene.updateweaponindicator(null,false);
                    Dungeon.level.drop(offhandweapon, hero.pos).sprite.drop();
                    GLog.w(Messages.get(this, "disarm", offhandweapon.name()));
                }
            }
        }
        return damage;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DISARMHITS, hitsToDisarm);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        hitsToDisarm = bundle.getInt(DISARMHITS);
    }
}
