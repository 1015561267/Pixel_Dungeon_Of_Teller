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
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Charm;
import com.teller.pixeldungeonofteller.actors.buffs.Light;
import com.teller.pixeldungeonofteller.actors.buffs.Sleep;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfLullaby;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfTeleportation;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Vampiric;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.sprites.SuccubusSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Succubus extends Mob {

    private static final int BLINK_DELAY = 5;
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        RESISTANCES.add(Vampiric.class);
    }

    static {
        IMMUNITIES.add(Sleep.class);
    }

    private int delay = 0;

    {
        spriteClass = SuccubusSprite.class;

        HP = HT = 55;
        ARMOR = 35;
        SlashThreshold=14;
        SHLD = 40;
        MAXSHLD = 40;


        defenseSkill = 25;
        viewDistance = Light.DISTANCE;

        EXP = 12;
        maxLvl = 25;

        loot = new ScrollOfLullaby();
        lootChance = 0.05f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(2, 16));
        dmg.AddPuncture(Random.NormalIntRange(2, 12));
        dmg.AddImpact(Random.NormalIntRange(4, 18));
        return dmg;
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {

        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Charm.class, Charm.durationFactor(enemy) * Random.IntRange(3, 7)).object = id();
            enemy.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
            Sample.INSTANCE.play(Assets.SND_CHARMS);
        }

        return damage;
    }

    @Override
    protected boolean getCloser(int target) {
        if (fieldOfView[target] && Dungeon.level.distance(pos, target) > 2 && delay <= 0) {

            blink(target);
            spend(-1 / speed());
            return true;

        } else {

            delay--;
            return super.getCloser(target);

        }
    }

    private void blink(int target) {

        Ballistica route = new Ballistica(pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

        //can't occupy the same cell as another char, so move back one.
        if (Actor.findChar(cell) != null && cell != this.pos)
            cell = route.path.get(route.dist - 1);

        if (Dungeon.level.avoid[cell]) {
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                cell = route.collisionPos + n;
                if (Dungeon.level.passable[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }
            if (candidates.size() > 0)
                cell = Random.element(candidates);
            else {
                delay = BLINK_DELAY;
                return;
            }
        }

        ScrollOfTeleportation.appear(this, cell);

        delay = BLINK_DELAY;
    }

    @Override
    public int attackSkill(Char target) {
        return 40;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);
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
