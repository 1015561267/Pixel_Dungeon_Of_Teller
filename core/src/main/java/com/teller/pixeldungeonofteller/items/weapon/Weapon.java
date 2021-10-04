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
package com.teller.pixeldungeonofteller.items.weapon;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.hero.HeroClass;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.rings.RingOfFuror;
import com.teller.pixeldungeonofteller.items.rings.RingOfSharpshooting;
import com.teller.pixeldungeonofteller.items.weapon.curses.Annoying;
import com.teller.pixeldungeonofteller.items.weapon.curses.Displacing;
import com.teller.pixeldungeonofteller.items.weapon.curses.Exhausting;
import com.teller.pixeldungeonofteller.items.weapon.curses.Fragile;
import com.teller.pixeldungeonofteller.items.weapon.curses.Sacrificial;
import com.teller.pixeldungeonofteller.items.weapon.curses.Wayward;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Blazing;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Chilling;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Dazzling;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Eldritch;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Grim;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Lucky;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Projecting;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Shocking;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Stunning;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Unstable;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Vampiric;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Venomous;
import com.teller.pixeldungeonofteller.items.weapon.enchantments.Vorpal;
import com.teller.pixeldungeonofteller.items.weapon.melee.MeleeWeapon;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

abstract public class Weapon extends KindOfWeapon {

    private static final int HITS_TO_KNOW = 20;
    private static final String TXT_TO_STRING = "%s :%d";
    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String ENCHANTMENT = "enchantment";
    private static final String IMBUE = "imbue";


    public int tier;
    public float ACC = 1f;    // Accuracy modifier
    public float DLY = 1f;    // Speed modifier
    public int RCH = 1;    // Reach modifier (only applies to melee hits)
    public int min;
    public int max;
    public Imbue imbue = Imbue.NONE;
    public Enchantment enchantment;

    private int hitsToKnow = HITS_TO_KNOW;

    public int Impactdamage(){return 0;}
    public int Slashdamage() {return 0;}
    public int Puncturedamage(){return 0;}

    @Override
    public Damage proc(Char attacker, Char defender, Damage damage) {

        if (enchantment != null) {
            damage = enchantment.proc(this, attacker, defender, damage);
        }

        if (!levelKnown) {
            if (--hitsToKnow <= 0) {
                levelKnown = true;
                GLog.i(Messages.get(Weapon.class, "identify"));
                Badges.validateItemLevelAquired(this);
            }
        }
        return damage;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, hitsToKnow);
        bundle.put(ENCHANTMENT, enchantment);
        bundle.put(IMBUE, imbue);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if ((hitsToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0) {
            hitsToKnow = HITS_TO_KNOW;
        }
        enchantment = (Enchantment) bundle.get(ENCHANTMENT);
        imbue = bundle.getEnum(IMBUE, Imbue.class);
    }

    @Override
    public float accuracyFactor(Hero hero) {

        int encumbrance = STRReq() - hero.STR();

        if (hasEnchant(Wayward.class))
            encumbrance = Math.max(3, encumbrance + 3);

        float ACC = this.ACC;

        if (this instanceof MissileWeapon) {
            int bonus = RingOfSharpshooting.getBonus(hero, RingOfSharpshooting.Aim.class);
            ACC *= (float) (Math.pow(1.2, bonus));
        }

        return encumbrance > 0 ? (float) (ACC / Math.pow(1.5, encumbrance)) : ACC;
    }

    @Override
    public float speedFactor(Hero hero) {

        int encumrance = STRReq() - hero.STR();
        if (this instanceof MissileWeapon && hero.heroClass == HeroClass.HUNTRESS) {
            encumrance -= 2;
        }

        float DLY = imbue.delayFactor(this.DLY);

        int bonus = RingOfFuror.getBonus(hero, RingOfFuror.Furor.class);

        DLY = (float) (0.2 + (DLY - 0.2) * Math.pow(0.85, bonus));

        return
                (encumrance > 0 ? (float) (DLY * Math.pow(1.2, encumrance)) : DLY);
    }

    @Override
    public int reachFactor(Hero hero) {
        return hasEnchant(Projecting.class) ? RCH + 1 : RCH;
    }

    @Override
    public PhysicalDamage damageRoll(Char owner) {
        //float damage=1;
        //if (this instanceof MeleeWeapon || (this instanceof MissileWeapon && hero.heroClass == HeroClass.HUNTRESS)) {
        //	int exStr = hero.STR() - STRReq();
        //		if (exStr > 0) {
        //			damage += Random.IntRange( 0, exStr );
        //		}
        //	}
        //PhysicalDamage dmg = super.damageRoll( hero );
        //int damage=imbue.damageFactor(Random.NormalIntRange(min() , max()));
        //PhysicalDamage dmg=new PhysicalDamage(damage,percentage);
        //dmg=new PhysicalDamage(damage,percentage);
        //return dmg;
        PhysicalDamage dmg=new PhysicalDamage();
        dmg.AddImpact(imbue.damageFactor(Impactdamage()));
        dmg.AddSlash(imbue.damageFactor(Slashdamage()));
        dmg.AddPuncture(imbue.damageFactor(Puncturedamage()));
        //if(owner instanceof Hero) { return new PhysicalDamage(imbue.damageFactor(Random.NormalIntRange(heromin(), heromax())), percentage()); }
        //else return new PhysicalDamage(imbue.damageFactor(Random.NormalIntRange(min(), max())), percentage());
        return dmg;
    }

    public int STRReq() {
        return STRReq(level());
    }

    public abstract int STRReq(int lvl);

    public int DEXReq() {
        return DEXReq(level());
    }

    public abstract int DEXReq(int lvl);

    public int STRFACTOR(){
        if (hero.STR < STRReq()) return 0;
        else return hero.STR - STRReq();
    }

    public int DEXFACTOR() {
        if (hero.DEX < DEXReq()) return 0;
        else return hero.DEX - DEXReq();
    }

    public int STRMINSCALE() { return 0; }
    public int DEXMINSCALE() { return 0; }
    public int INTMINSCALE() { return 0; }
    public int STRMAXSCALE() { return 0; }
    public int DEXMAXSCALE() { return 0; }
    public int INTMAXSCALE() { return 0; }

    @Override
    public String info() {
        String info = desc();

        if (levelKnown) {
            info += "\n\n" + Messages.get(this, "stats_known", tier, imbue.damageFactor(min()), imbue.damageFactor(max()), STRReq(), DEXReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += "\n " + Messages.get(Weapon.class, "too_heavy");
            }
            if (DEXReq() > Dungeon.hero.DEX()) {
                info += "\n " + Messages.get(Weapon.class, "no_enough_dex");
            }
        } else {
            info += "\n\n" + Messages.get(this, "stats_unknown", tier, min(0), max(0), STRReq(0), DEXReq());
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
            if (DEXReq(0) > Dungeon.hero.DEX()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_no_enough_dex");
            }
        }

        info +="\n " +Messages.get(Weapon.class, "type");

        switch (WeaponType())
        {
            case MainHand:info +=  Messages.get(Weapon.class, "main_hand");break;
            case OffHand:info += Messages.get(Weapon.class, "off_hand");break;
            case DualWield:info += Messages.get(Weapon.class, "dual_wield");break;
            case TwoHanded:info += Messages.get(Weapon.class, "two_handed");break;
            case Attached:info += Messages.get(Weapon.class, "attached");break;
        }

        String stats_desc = Messages.get(this, "stats_desc");

        switch (stealth())
        {
            case 0: info += "\n\n" + Messages.get(Weapon.class, "no_stealth");break;
            case 1: info += "\n\n" + Messages.get(Weapon.class, "low_stealth");break;
            case 2: info += "\n\n" + Messages.get(Weapon.class, "normal_stealth");break;
            case 3: info += "\n\n" + Messages.get(Weapon.class, "high_stealth");break;
            case 4: info += "\n\n" + Messages.get(Weapon.class, "excellent_stealth");break;
        }

        if (!stats_desc.equals("") && !stats_desc.equals("!!!NO TEXT FOUND!!!"))
            info += "\n\n" + stats_desc;

        switch (imbue) {
            case LIGHT:
                info += "\n\n" + Messages.get(Weapon.class, "lighter");
                break;
            case HEAVY:
                info += "\n\n" + Messages.get(Weapon.class, "heavier");
                break;
            case NONE:
        }
        if (enchantment != null && (cursedKnown || !enchantment.curse())) {
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }
        if (cursed && isEquipped(Dungeon.hero)) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        }
        return info;
    }


    public Item upgrade(boolean enchant) {

        if (enchant && (enchantment == null || enchantment.curse())) {
            enchant(Enchantment.random());
        } else if (!enchant && Random.Float() > Math.pow(0.9, level())) {
            enchant(null);
        }

        return super.upgrade();
    }

    @Override
    public String name() {
        return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name(super.name()) : super.name();
    }

    @Override
    public Item random() {
        float roll = Random.Float();
        if (roll < 0.3f) {
            //30% chance to be level 0 and cursed
            enchant(Enchantment.randomCurse());
            cursed = true;
            return this;
        } else if (roll < 0.75f) {
            //45% chance to be level 0
        } else if (roll < 0.95f) {
            //15% chance to be +1
            upgrade(1);
        } else {
            //5% chance to be +2
            upgrade(2);
        }

        //if not cursed, 10% chance to be enchanted (7% overall)
        if (Random.Int(10) == 0)
            enchant();

        return this;
    }

    public Weapon enchant(Enchantment ench) {
        enchantment = ench;
        return this;
    }

    public Weapon enchant() {

        Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
        Enchantment ench = Enchantment.random();
        while (ench.getClass() == oldEnchantment) {
            ench = Enchantment.random();
        }

        return enchant(ench);
    }

    public boolean hasEnchant(Class<? extends Enchantment> type) {
        return enchantment != null && enchantment.getClass() == type;
    }

    public boolean hasGoodEnchant() {
        return enchantment != null && !enchantment.curse();
    }

    public boolean hasCurseEnchant() {
        return enchantment != null && enchantment.curse();
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.glowing() : null;
    }

    public enum Imbue {
        NONE(1.0f, 1.00f),
        LIGHT(0.7f, 0.67f),
        HEAVY(1.5f, 1.67f);

        private float damageFactor;
        private float delayFactor;

        Imbue(float dmg, float dly) {
            damageFactor = dmg;
            delayFactor = dly;
        }

        public int damageFactor(int dmg) {
            return Math.round(dmg * damageFactor);
        }

        public float delayFactor(float dly) {
            return dly * delayFactor;
        }

        public float getDamageFactor() {
            return this.damageFactor;
        }

    }

    public static abstract class Enchantment implements Bundlable {

        private static final Class<?>[] enchants = new Class<?>[]{
                Blazing.class, Venomous.class, Vorpal.class, Shocking.class,
                Chilling.class, Eldritch.class, Lucky.class, Projecting.class, Unstable.class, Dazzling.class,
                Grim.class, Stunning.class, Vampiric.class,};
        private static final float[] chances = new float[]{
                10, 10, 10, 10,
                5, 5, 5, 5, 5, 5,
                2, 2, 2};

        private static final Class<?>[] curses = new Class<?>[]{
                Annoying.class, Displacing.class, Exhausting.class, Fragile.class, Sacrificial.class, Wayward.class
        };

        @SuppressWarnings("unchecked")
        public static Enchantment random() {
            try {
                return ((Class<Enchantment>) enchants[Random.chances(chances)]).newInstance();
            } catch (Exception e) {
                PixelDungeonOfTeller.reportException(e);
                return null;
            }
        }

        @SuppressWarnings("unchecked")
        public static Enchantment randomCurse() {
            try {
                return ((Class<Enchantment>) Random.oneOf(curses)).newInstance();
            } catch (Exception e) {
                PixelDungeonOfTeller.reportException(e);
                return null;
            }
        }

        public abstract Damage proc(Weapon weapon, Char attacker, Char defender, Damage damage);

        public String name() {
            if (!curse())
                return name(Messages.get(this, "enchant"));
            else
                return name(Messages.get(Item.class, "curse"));
        }

        public String name(String weaponName) {
            return Messages.get(this, "name", weaponName);
        }

        public String desc() {
            return Messages.get(this, "desc");
        }

        public boolean curse() {
            return false;
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
        }

        @Override
        public void storeInBundle(Bundle bundle) {
        }
        public abstract ItemSprite.Glowing glowing();
    }

    @Override
    public boolean doEquip(final Hero hero) {
        if(super.doEquip(hero))
        {
            GameScene.scene.updateweaponindicator(this,(hero.belongings.mainhandweapon==this));
            return true;
        }
        else return false;
    }

    public boolean doUnequip(Hero hero, boolean collect, boolean single) {

        Boolean Ismainhand=(hero.belongings.mainhandweapon==this);

        if(super.doUnequip(hero,collect,single))
        {
            GameScene.scene.hideweaponindicator(this,Ismainhand);
            return true;
        }
        else return false;
    }
}
