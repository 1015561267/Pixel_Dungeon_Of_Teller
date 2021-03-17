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
import com.teller.pixeldungeonofteller.actors.buffs.Corruption;
import com.teller.pixeldungeonofteller.actors.buffs.Terror;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.Gold;
import com.teller.pixeldungeonofteller.items.Honeypot;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.artifacts.MasterThievesArmband;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ThiefSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Thief extends Mob {

    private static final String ITEM = "item";
    public Item item;

    {
        spriteClass = ThiefSprite.class;

        HP = HT = 20;
        ARMOR = 8;
        SlashThreshold=4;
        SHLD = 0;
        MAXSHLD = 0;

        defenseSkill = 12;

        EXP = 5;
        maxLvl = 10;

        loot = new MasterThievesArmband().identify();
        lootChance = 0.01f;

        FLEEING = new Fleeing();

        properties.add(Property.DEMONIC);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ITEM, item);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        item = (Item) bundle.get(ITEM);
    }

    @Override
    public float speed() {
        if (item != null) return (5 * super.speed()) / 6;
        else return super.speed();
    }

    @Override
    public PhysicalDamage damageRoll() {
        PhysicalDamage dmg = new PhysicalDamage();
        dmg.AddSlash(Random.NormalIntRange(1, 6));
        return dmg;
    }

    @Override
    protected float attackDelay() {
        return 0.5f;
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        if (item != null) {
            Dungeon.level.drop(item, pos).sprite.drop();
            //updates position
            if (item instanceof Honeypot.ShatteredPot)
                ((Honeypot.ShatteredPot) item).setHolder(this);
        }
    }

    @Override
    protected Item createLoot() {
        if (!Dungeon.limitedDrops.armband.dropped()) {
            Dungeon.limitedDrops.armband.drop();
            return super.createLoot();
        } else
            return new Gold(Random.NormalIntRange(100, 250));
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 3);
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        if (item == null && enemy instanceof Hero && steal((Hero) enemy)) {
            state = FLEEING;
        }

        return damage;
    }

    @Override
    public Damage defenseProc(Char enemy, Damage damage) {
        if (state == FLEEING) {
            Dungeon.level.drop(new Gold(), pos).sprite.drop();
        }
        return super.defenseProc(enemy, damage);
    }

    protected boolean steal(Hero hero) {

        Item item = hero.belongings.randomUnequipped();

        if (item != null && !item.unique && item.level() < 1) {

            GLog.w(Messages.get(Thief.class, "stole", item.name()));
            if (!item.stackable || hero.belongings.getSimilar(item) == null) {
                Dungeon.quickslot.convertToPlaceholder(item);
            }
            item.updateQuickslot();

            if (item instanceof Honeypot) {
                this.item = ((Honeypot) item).shatter(this, this.pos);
                item.detach(hero.belongings.backpack);
            } else {
                this.item = item.detach(hero.belongings.backpack);
                if (item instanceof Honeypot.ShatteredPot)
                    ((Honeypot.ShatteredPot) item).setHolder(this);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String description() {
        String desc = super.description();

        if (item != null) {
            desc += Messages.get(this, "carries", item.name());
        }

        return desc;
    }

    private class Fleeing extends Mob.Fleeing {
        @Override
        protected void nowhereToRun() {
            if (buff(Terror.class) == null && buff(Corruption.class) == null) {
                if (enemySeen) {
                    sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Mob.class, "rage"));
                    state = HUNTING;
                } else {

                    int count = 32;
                    int newPos;
                    do {
                        newPos = Dungeon.level.randomRespawnCell();
                        if (count-- <= 0) {
                            break;
                        }
                    } while (newPos == -1 || Dungeon.visible[newPos] || Dungeon.level.distance(newPos, pos) < (count / 3));

                    if (newPos != -1) {

                        if (Dungeon.visible[pos])
                            CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
                        pos = newPos;
                        sprite.place(pos);
                        sprite.visible = Dungeon.visible[pos];
                        if (Dungeon.visible[pos])
                            CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);

                    }

                    if (item != null) GLog.n(Messages.get(Thief.class, "escapes", item.name()));
                    item = null;
                    state = WANDERING;
                }
            } else {
                super.nowhereToRun();
            }
        }
    }
}
