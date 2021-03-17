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
package com.teller.pixeldungeonofteller.items.weapon.missiles;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Boomerang extends MissileWeapon {

    static int IMPACTFACTOR = 80;
    static int SLASHFACTOR = 20;
    static int PUNCTUREFACTOR = 0;

    private PhysicalPercentage percentage() { return new PhysicalPercentage(0.8f,0.2f,0); }

    private boolean throwEquiped;

    {
        image = ItemSpriteSheet.BOOMERANG;

        stackable = false;

        unique = true;
        bones = false;
    }

    @Override
    public int min(int lvl) {
        return 1 + 1 * lvl;
    }

    @Override
    public int max(int lvl) { return 6 + 2 * lvl;}

    public int STRMINSCALE() { return 1; }
    public int DEXMINSCALE() { return 1; }
    public int STRMAXSCALE() { return 1; }
    public int DEXMAXSCALE() { return 1; }
    public int INTMAXSCALE() { return 2; }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (!isEquipped(hero)) actions.add(AC_EQUIP);
        return actions;
    }

    @Override
    public int STRReq(int lvl) {
        return 1;
    }

    @Override
    public int DEXReq(int lvl) {
        return 0;
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public Item upgrade() {
        return upgrade(false);
    }

    @Override
    public Item upgrade(boolean enchant) {
        super.upgrade(enchant);
        updateQuickslot();
        return this;
    }

    @Override
    public Item degrade() {
        return super.degrade();
    }

    @Override
    public Damage proc(Char attacker, Char defender, Damage damage) {
        if (attacker instanceof Hero && ((Hero) attacker).rangedWeapon == this) {
            circleBack(defender.pos, (Hero) attacker);
        }
        return new PhysicalDamage(imbue.damageFactor(Random.NormalIntRange(min(), max())), percentage());
    }

    @Override
    protected void miss(int cell) {
        circleBack(cell, curUser);
    }

    private void circleBack(int from, Hero owner) {

        ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                reset(from, curUser.pos, curItem, null);

        if (throwEquiped) {
            owner.belongings.mainhandweapon = this;
            owner.spend(-TIME_TO_EQUIP);
            Dungeon.quickslot.replaceSimilar(this);
            updateQuickslot();
        } else if (!collect(curUser.belongings.backpack)) {
            Dungeon.level.drop(this, owner.pos).sprite.drop();
        }
    }

    @Override
    public void cast(Hero user, int dst) {
        throwEquiped = isEquipped(user) && !cursed;
        if (throwEquiped) Dungeon.quickslot.convertToPlaceholder(this);
        super.cast(user, dst);
    }

    @Override
    public String desc() {
        String info = super.desc();
        switch (imbue) {
            case LIGHT:
                info += "\n\n" + Messages.get(Weapon.class, "lighter");
                break;
            case HEAVY:
                info += "\n\n" + Messages.get(Weapon.class, "heavier");
                break;
            case NONE:
        }
        return info + "\n\n此武器实际造成的冲击:切割:穿刺比为:\n" + IMPACTFACTOR + "%:" + SLASHFACTOR + "%:" + PUNCTUREFACTOR + "%";
    }
}
