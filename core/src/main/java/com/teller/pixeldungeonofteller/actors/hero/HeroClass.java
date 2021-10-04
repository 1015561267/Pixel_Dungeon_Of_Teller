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
package com.teller.pixeldungeonofteller.actors.hero;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Challenges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.BrokenSeal;
import com.teller.pixeldungeonofteller.items.SewingKit;
import com.teller.pixeldungeonofteller.items.armor.ClothArmor;
import com.teller.pixeldungeonofteller.items.armor.PlateArmor;
import com.teller.pixeldungeonofteller.items.artifacts.CloakOfShadows;
import com.teller.pixeldungeonofteller.items.artifacts.EtherealChains;
import com.teller.pixeldungeonofteller.items.bags.WandHolster;
import com.teller.pixeldungeonofteller.items.food.Food;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.Flash;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.Healing;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.HolyBomb;
import com.teller.pixeldungeonofteller.items.pages.Spell.OldBook.LightUp;
import com.teller.pixeldungeonofteller.items.pages.Spell.OldBook.MagicMissile;
import com.teller.pixeldungeonofteller.items.potions.PotionOfExperience;
import com.teller.pixeldungeonofteller.items.potions.PotionOfHealing;
import com.teller.pixeldungeonofteller.items.potions.PotionOfInvisibility;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLevitation;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLiquidFlame;
import com.teller.pixeldungeonofteller.items.potions.PotionOfMindVision;
import com.teller.pixeldungeonofteller.items.potions.PotionOfParalyticGas;
import com.teller.pixeldungeonofteller.items.potions.PotionOfPurity;
import com.teller.pixeldungeonofteller.items.potions.PotionOfStrength;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.rings.RingOfAccuracy;
import com.teller.pixeldungeonofteller.items.rings.RingOfHaste;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfIdentify;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicMapping;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicalInfusion;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfPsionicBlast;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRage;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRecharging;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfUpgrade;
import com.teller.pixeldungeonofteller.items.wands.WandOfBlastWave;
import com.teller.pixeldungeonofteller.items.wands.WandOfFireblast;
import com.teller.pixeldungeonofteller.items.wands.WandOfFrost;
import com.teller.pixeldungeonofteller.items.wands.WandOfMagicMissile;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.missiles.Boomerang;
import com.teller.pixeldungeonofteller.items.weapon.missiles.Dart;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.Gauntlet;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.HiddenBlade;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Dagger;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Knuckles;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Sai;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tamahawk;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.Flintlock;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.HandCannon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.SubmachineGun;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.BookOfLight;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.OldBook;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Dirk;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Flail;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.MagesStaff;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Whip;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.WornShortsword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.JavelinBarrel;
import com.teller.pixeldungeonofteller.items.weapon.weapons.Shield.SawtoothFrisbee;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Nunchaku;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.plants.Blindweed;
import com.watabou.utils.Bundle;

public enum HeroClass {

    WARRIOR("warrior"),
    MAGE("mage"),
    ROGUE("rogue"),
    HUNTRESS("huntress");

    private static final String CLASS = "class";
    private String title;
    private HeroSubClass[] subClasses;

    HeroClass( String title, HeroSubClass...subClasses ) {
        this.title = title;
        this.subClasses = subClasses;
    }

    private static void initCommon(Hero hero) {
        if (!Dungeon.isChallenged(Challenges.NO_ARMOR))//(hero.belongings.armor = new ClothArmor()).identify();
            new ClothArmor().identify().collect();
        if (!Dungeon.isChallenged(Challenges.NO_FOOD))
            new Food().identify().collect();

        new Flintlock().identify().collect();
        new Flintlock().identify().collect();

        for (int i = 0; i < 10; i++) {
            new SewingKit().identify().collect();
            new PotionOfStrength().identify();
            new ScrollOfRecharging().identify();
            new ScrollOfMagicMapping().identify();
            new ScrollOfRage().identify();
            new PotionOfLevitation().identify();
        }

        new PotionOfLiquidFlame().collect();
        new ScrollOfIdentify().collect();
        new ScrollOfIdentify().collect();
        new ScrollOfIdentify().collect();
        new PotionOfLiquidFlame().collect();

        Ring ring = new RingOfAccuracy();
        ring.cursed = true;
        ring.collect();

        new HiddenBlade().identify().collect();
        //new NinjaProsthesis().identify().collect();
        //new Gauntlet().identify().collect();
        //new JavelinBarrel().identify().collect();
        //new Nunchaku().identify().collect();
        new OldBook().addRaw().identify().collect();
        new BookOfLight().addRaw().identify().collect();
        new SubmachineGun().identify().collect();

        //new Tamahawk().identify().collect();
        new Tamahawk().identify().collect();

        new WandHolster().collect();
        Dungeon.limitedDrops.wandBag.drop();

        new SawtoothFrisbee().identify().collect();

        new HandCannon().identify().collect();

        new WandOfBlastWave().identify().collect();

        new WandOfFrost().identify().collect();

        new WandOfFireblast().identify().collect();

        MagicPage m1 = new MagicPage();
        m1.getspell(new MagicMissile()).collect();

        MagicPage m2 = new MagicPage();
        m2.getspell(new MagicMissile()).collect();

        MagicPage m3 = new MagicPage();
        m3.getspell(new LightUp()).collect();

        MagicPage m7 = new MagicPage();
        m7.getspell(new Healing()).collect();
    }

    private static void initWarrior(Hero hero) {
        (hero.belongings.mainhandweapon = new WornShortsword()).identify();
        Dart darts = new Dart(8);
        darts.identify().collect();
        BrokenSeal seal = new BrokenSeal();
        seal.collect();
        Dungeon.quickslot.setSlot(0, seal);
        Dungeon.quickslot.setSlot(1, darts);
        new PotionOfHealing().setKnown();
    }

    private static void initMage(Hero hero) {
        MagesStaff staff;

        if (Badges.isUnlocked(Badges.Badge.TUTORIAL_MAGE)) {
            staff = new MagesStaff(new WandOfMagicMissile());
        } else {
            staff = new MagesStaff();
            new WandOfMagicMissile().identify().collect();
        }

        (hero.belongings.mainhandweapon = staff).identify();
        hero.belongings.mainhandweapon.activate(hero);

        Dungeon.quickslot.setSlot(0, staff);

        new ScrollOfUpgrade().setKnown();
    }

    private static void initRogue(Hero hero) {

        (hero.belongings.mainhandweapon = new Dagger().enchant(Weapon.Enchantment.random())).identify();

        CloakOfShadows cloak = new CloakOfShadows();
        (hero.belongings.misc1 = cloak).identify();
        hero.belongings.misc1.activate(hero);

        Dart darts = new Dart(8);
        darts.identify().collect();

        Dungeon.quickslot.setSlot(0, cloak);
        Dungeon.quickslot.setSlot(1, darts);

        new ScrollOfMagicMapping().setKnown();
    }

    private static void initHuntress(Hero hero) {

        (hero.belongings.mainhandweapon = new Knuckles()).identify();
        Boomerang boomerang = new Boomerang();
        boomerang.identify().collect();

        //Dungeon.quickslot.setSlot(0, boomerang);

        new PotionOfMindVision().setKnown();
    }

    public static HeroClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(CLASS);
        return value.length() > 0 ? valueOf(value) : ROGUE;
    }

    public void initHero(Hero hero) {

        hero.heroClass = this;

        initCommon(hero);

        switch (this) {
            case WARRIOR:
                initWarrior(hero);
                break;

            case MAGE:
                initMage(hero);
                break;

            case ROGUE:
                initRogue(hero);
                break;

            case HUNTRESS:
                initHuntress(hero);
                break;
        }

        hero.updateAwareness();
    }

    public Badges.Badge masteryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.MASTERY_WARRIOR;
            case MAGE:
                return Badges.Badge.MASTERY_MAGE;
            case ROGUE:
                return Badges.Badge.MASTERY_ROGUE;
            case HUNTRESS:
                return Badges.Badge.MASTERY_HUNTRESS;
        }
        return null;
    }

    public String title() {
        return Messages.get(HeroClass.class, title);
    }

    public HeroSubClass[] subClasses() {
        return subClasses;
    }

    public String spritesheet() {

        switch (this) {
            case WARRIOR:
                return Assets.WARRIOR;
            case MAGE:
                return Assets.MAGE;
            case ROGUE:
                return Assets.ROGUE;
            case HUNTRESS:
                return Assets.HUNTRESS;
        }

        return null;
    }

    public String[] perks() {

        switch (this) {
            case WARRIOR:
                return new String[]{
                        Messages.get(HeroClass.class, "warrior_perk1"),
                        Messages.get(HeroClass.class, "warrior_perk2"),
                        Messages.get(HeroClass.class, "warrior_perk3"),
                        Messages.get(HeroClass.class, "warrior_perk4"),
                        Messages.get(HeroClass.class, "warrior_perk5"),
                };
            case MAGE:
                return new String[]{
                        Messages.get(HeroClass.class, "mage_perk1"),
                        Messages.get(HeroClass.class, "mage_perk2"),
                        Messages.get(HeroClass.class, "mage_perk3"),
                        Messages.get(HeroClass.class, "mage_perk4"),
                        Messages.get(HeroClass.class, "mage_perk5"),
                };
            case ROGUE:
                return new String[]{
                        Messages.get(HeroClass.class, "rogue_perk1"),
                        Messages.get(HeroClass.class, "rogue_perk2"),
                        Messages.get(HeroClass.class, "rogue_perk3"),
                        Messages.get(HeroClass.class, "rogue_perk4"),
                        Messages.get(HeroClass.class, "rogue_perk5"),
                        Messages.get(HeroClass.class, "rogue_perk6"),
                };
            case HUNTRESS:
                return new String[]{
                        Messages.get(HeroClass.class, "huntress_perk1"),
                        Messages.get(HeroClass.class, "huntress_perk2"),
                        Messages.get(HeroClass.class, "huntress_perk3"),
                        Messages.get(HeroClass.class, "huntress_perk4"),
                        Messages.get(HeroClass.class, "huntress_perk5"),
                };
        }

        return null;
    }

    public void storeInBundle(Bundle bundle) {
        bundle.put(CLASS, toString());
    }
}
