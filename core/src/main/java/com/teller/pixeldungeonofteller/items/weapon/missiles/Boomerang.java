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

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.buffs.EarthImbue;
import com.teller.pixeldungeonofteller.actors.buffs.FireImbue;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.hero.HeroSubClass;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class Boomerang extends Weapon {
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

        usesTargeting = true;

        defaultAction = AC_THROW;
    }

    @Override
    public Type WeaponType() {
        return Type.Attached;
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
        return actions;
    }

    @Override
    public int STRReq(int lvl) {
        return 0;
    }

    @Override
    public int DEXReq(int lvl) { return 1; }

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
        GameScene.scene.offhandupdate();
        return this;
    }

    @Override
    public Item degrade() {
        return super.degrade();
    }

    @Override
    public Damage proc(Char attacker, Char defender, Damage damage) {
            if (attacker instanceof Hero) {
            circleBack(defender.pos, (Hero) attacker);
        }
        return new PhysicalDamage(imbue.damageFactor(Random.NormalIntRange(min(), max())), percentage());
    }


    protected void miss(int cell) {
        circleBack(cell, curUser);
    }

    private void circleBack(int from, Hero owner) {
        ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                reset(from, curUser.pos, curItem, null);
        if (throwEquiped) {
            owner.belongings.offhandweapon = this;
            Dungeon.quickslot.replaceSimilar(this);
            updateQuickslot();
            GameScene.scene.offhandupdate();
        } else if (!collect(curUser.belongings.backpack)) {
            Dungeon.level.drop(this, owner.pos).sprite.drop();
        }
    }

    @Override
    public void cast(Hero user, int dst) {
        throwEquiped = isEquipped(user) && !cursed;
        if (throwEquiped)
        {
            Dungeon.hero.belongings.offhandweapon = null;
            Dungeon.quickslot.convertToPlaceholder(this);
            GameScene.scene.offhandupdate();
        }
        super.cast(user, dst);

    }

    @Override
    protected void onThrow(int cell) {
        Char enemy = Actor.findChar(cell);
        if (enemy != null && enemy != curUser && throwEquiped) {
            Invisibility.dispel();
            if(curUser.hit(curUser,enemy,false))
            {
                Damage damage = new PhysicalDamage();
                damage = this.proc(curUser,enemy,damage);
                enemy.damage(damage,this);

                int dr = enemy.drRoll();


                int effectiveDamage = Math.max(damage.effictivehpdamage - dr, 0);

                enemy.sprite.bloodBurstA(enemy.sprite.center(), effectiveDamage);
                enemy.sprite.flash();
                //TODO: consider revisiting this and shaking in more cases.
                float shake = 0f;
                if (enemy == hero)
                    shake = effectiveDamage / (enemy.HT / 4);
                if (shake > 1f)
                    Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
            }
            else {
                String defense = enemy.defenseVerb();
                enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                Sample.INSTANCE.play(Assets.SND_MISS);
                miss(cell);
            }
        }
        else
        {
            super.onThrow(cell);
        }
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
