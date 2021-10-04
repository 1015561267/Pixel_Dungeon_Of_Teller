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
package com.teller.pixeldungeonofteller.items;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Ghost;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.armor.ClothArmor;
import com.teller.pixeldungeonofteller.items.armor.LeatherArmor;
import com.teller.pixeldungeonofteller.items.armor.MailArmor;
import com.teller.pixeldungeonofteller.items.armor.PlateArmor;
import com.teller.pixeldungeonofteller.items.armor.ScaleArmor;
import com.teller.pixeldungeonofteller.items.artifacts.AlchemistsToolkit;
import com.teller.pixeldungeonofteller.items.artifacts.Artifact;
import com.teller.pixeldungeonofteller.items.artifacts.CapeOfThorns;
import com.teller.pixeldungeonofteller.items.artifacts.ChaliceOfBlood;
import com.teller.pixeldungeonofteller.items.artifacts.CloakOfShadows;
import com.teller.pixeldungeonofteller.items.artifacts.DriedRose;
import com.teller.pixeldungeonofteller.items.artifacts.EtherealChains;
import com.teller.pixeldungeonofteller.items.artifacts.HornOfPlenty;
import com.teller.pixeldungeonofteller.items.artifacts.LloydsBeacon;
import com.teller.pixeldungeonofteller.items.artifacts.MasterThievesArmband;
import com.teller.pixeldungeonofteller.items.artifacts.SandalsOfNature;
import com.teller.pixeldungeonofteller.items.artifacts.TalismanOfForesight;
import com.teller.pixeldungeonofteller.items.artifacts.TimekeepersHourglass;
import com.teller.pixeldungeonofteller.items.artifacts.UnstableSpellbook;
import com.teller.pixeldungeonofteller.items.bags.Bag;
import com.teller.pixeldungeonofteller.items.food.Food;
import com.teller.pixeldungeonofteller.items.food.MysteryMeat;
import com.teller.pixeldungeonofteller.items.food.Pasty;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.potions.PotionOfExperience;
import com.teller.pixeldungeonofteller.items.potions.PotionOfFrost;
import com.teller.pixeldungeonofteller.items.potions.PotionOfHealing;
import com.teller.pixeldungeonofteller.items.potions.PotionOfInvisibility;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLevitation;
import com.teller.pixeldungeonofteller.items.potions.PotionOfLiquidFlame;
import com.teller.pixeldungeonofteller.items.potions.PotionOfMight;
import com.teller.pixeldungeonofteller.items.potions.PotionOfMindVision;
import com.teller.pixeldungeonofteller.items.potions.PotionOfParalyticGas;
import com.teller.pixeldungeonofteller.items.potions.PotionOfPurity;
import com.teller.pixeldungeonofteller.items.potions.PotionOfStrength;
import com.teller.pixeldungeonofteller.items.potions.PotionOfToxicGas;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.rings.RingOfAccuracy;
import com.teller.pixeldungeonofteller.items.rings.RingOfElements;
import com.teller.pixeldungeonofteller.items.rings.RingOfEvasion;
import com.teller.pixeldungeonofteller.items.rings.RingOfForce;
import com.teller.pixeldungeonofteller.items.rings.RingOfFuror;
import com.teller.pixeldungeonofteller.items.rings.RingOfHaste;
import com.teller.pixeldungeonofteller.items.rings.RingOfMagic;
import com.teller.pixeldungeonofteller.items.rings.RingOfMight;
import com.teller.pixeldungeonofteller.items.rings.RingOfSharpshooting;
import com.teller.pixeldungeonofteller.items.rings.RingOfTenacity;
import com.teller.pixeldungeonofteller.items.rings.RingOfWealth;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfIdentify;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfLullaby;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicMapping;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicalInfusion;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMirrorImage;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfPsionicBlast;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRage;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRecharging;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRemoveCurse;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfTeleportation;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfTerror;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfUpgrade;
import com.teller.pixeldungeonofteller.items.wands.Wand;
import com.teller.pixeldungeonofteller.items.wands.WandOfBlastWave;
import com.teller.pixeldungeonofteller.items.wands.WandOfCorruption;
import com.teller.pixeldungeonofteller.items.wands.WandOfDisintegration;
import com.teller.pixeldungeonofteller.items.wands.WandOfFireblast;
import com.teller.pixeldungeonofteller.items.wands.WandOfFrost;
import com.teller.pixeldungeonofteller.items.wands.WandOfLightning;
import com.teller.pixeldungeonofteller.items.wands.WandOfMagicMissile;
import com.teller.pixeldungeonofteller.items.wands.WandOfPrismaticLight;
import com.teller.pixeldungeonofteller.items.wands.WandOfRegrowth;
import com.teller.pixeldungeonofteller.items.wands.WandOfTransfusion;
import com.teller.pixeldungeonofteller.items.wands.WandOfVenom;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.missiles.Boomerang;
import com.teller.pixeldungeonofteller.items.weapon.missiles.CurareDart;
import com.teller.pixeldungeonofteller.items.weapon.missiles.Dart;
import com.teller.pixeldungeonofteller.items.weapon.missiles.IncendiaryDart;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.Gauntlet;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Dagger;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Knuckles;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Sai;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Sword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tamahawk;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tonfa;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.BattleAxe;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Dirk;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Flail;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.HandAxe;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Longsword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Mace;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.MagesStaff;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.NewShortsword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Scimitar;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.WarHammer;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Whip;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.WornShortsword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.HammerWithThorns;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.JavelinBarrel;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Rapier;
import com.teller.pixeldungeonofteller.items.weapon.weapons.Shield.RoundShield;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.Wakizashi;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Glaive;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Greataxe;
import com.teller.pixeldungeonofteller.items.weapon.weapons.Shield.Greatshield;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Greatsword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Nunchaku;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Quarterstaff;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Spear;
import com.teller.pixeldungeonofteller.plants.BlandfruitBush;
import com.teller.pixeldungeonofteller.plants.Blindweed;
import com.teller.pixeldungeonofteller.plants.Dreamfoil;
import com.teller.pixeldungeonofteller.plants.Earthroot;
import com.teller.pixeldungeonofteller.plants.Fadeleaf;
import com.teller.pixeldungeonofteller.plants.Firebloom;
import com.teller.pixeldungeonofteller.plants.Icecap;
import com.teller.pixeldungeonofteller.plants.Plant;
import com.teller.pixeldungeonofteller.plants.Rotberry;
import com.teller.pixeldungeonofteller.plants.Sorrowmoss;
import com.teller.pixeldungeonofteller.plants.Starflower;
import com.teller.pixeldungeonofteller.plants.Stormvine;
import com.teller.pixeldungeonofteller.plants.Sungrass;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Generator {

    public static final Category[] wepTiers = new Category[]{
            Category.WEP_T1,
            Category.WEP_T2,
            Category.WEP_T3,
            Category.WEP_T4,
            Category.WEP_T5
    };
    private static final float[][] floorSetTierProbs = new float[][]{
            {0, 70, 20, 8, 2},
            {0, 25, 50, 20, 5},
            {0, 10, 40, 40, 10},
            {0, 5, 20, 50, 25},
            {0, 2, 8, 20, 70}
    };
    private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1};
    private static final String ARTIFACTS = "artifacts";
    private static HashMap<Category, Float> categoryProbs = new HashMap<Generator.Category, Float>();
    private static ArrayList<String> spawnedArtifacts = new ArrayList<String>();

    static {

        Category.GOLD.classes = new Class<?>[]{
                Gold.class};
        Category.GOLD.probs = new float[]{1};

        Category.SCROLL.classes = new Class<?>[]{
                ScrollOfIdentify.class,
                ScrollOfTeleportation.class,
                ScrollOfRemoveCurse.class,
                ScrollOfUpgrade.class,
                ScrollOfRecharging.class,
                ScrollOfMagicMapping.class,
                ScrollOfRage.class,
                ScrollOfTerror.class,
                ScrollOfLullaby.class,
                ScrollOfMagicalInfusion.class,
                ScrollOfPsionicBlast.class,
                ScrollOfMirrorImage.class};
        Category.SCROLL.probs = new float[]{30, 10, 20, 0, 15, 15, 12, 8, 8, 0, 4, 10};

        Category.POTION.classes = new Class<?>[]{
                PotionOfHealing.class,
                PotionOfExperience.class,
                PotionOfToxicGas.class,
                PotionOfParalyticGas.class,
                PotionOfLiquidFlame.class,
                PotionOfLevitation.class,
                PotionOfStrength.class,
                PotionOfMindVision.class,
                PotionOfPurity.class,
                PotionOfInvisibility.class,
                PotionOfMight.class,
                PotionOfFrost.class};
        Category.POTION.probs = new float[]{45, 4, 15, 10, 15, 10, 0, 20, 12, 10, 0, 10};

        //TODO: add last ones when implemented
        Category.WAND.classes = new Class<?>[]{
                WandOfMagicMissile.class,
                WandOfLightning.class,
                WandOfDisintegration.class,
                WandOfFireblast.class,
                WandOfVenom.class,
                WandOfBlastWave.class,
                //WandOfLivingEarth.class,
                WandOfFrost.class,
                WandOfPrismaticLight.class,
                //WandOfWarding.class,
                WandOfTransfusion.class,
                WandOfCorruption.class,
                WandOfRegrowth.class};
        Category.WAND.probs = new float[]{5, 4, 4, 4, 4, 3, /*3,*/ 3, 3, /*3,*/ 3, 3, 3};

        //see generator.randomWeapon
        Category.WEAPON.classes = new Class<?>[]{};
        Category.WEAPON.probs = new float[]{};

        Category.WEP_T1.classes = new Class<?>[]{
                WornShortsword.class,
                Knuckles.class,
                Dagger.class,
                MagesStaff.class,
                Boomerang.class,
                Dart.class
        };
        Category.WEP_T1.probs = new float[]{1, 1, 1, 0, 0, 1};

        Category.WEP_T2.classes = new Class<?>[]{
                NewShortsword.class,
                HandAxe.class,
                Spear.class,
                Quarterstaff.class,
                Dirk.class,
                IncendiaryDart.class,
                HammerWithThorns.class,
                Gauntlet.class
        };
        Category.WEP_T2.probs = new float[]{6, 5, 5, 4, 4, 6, 5, 4};

        Category.WEP_T3.classes = new Class<?>[]{
                Sword.class,
                Mace.class,
                Scimitar.class,
                RoundShield.class,
                Sai.class,
                Whip.class,
                CurareDart.class,
                Rapier.class,
                Wakizashi.class,
                Nunchaku.class
        };
        Category.WEP_T3.probs = new float[]{6, 5, 5, 4, 4, 4, 6, 4, 4, 4};

        Category.WEP_T4.classes = new Class<?>[]{
                Longsword.class,
                BattleAxe.class,
                Flail.class,
                //RunicBlade.class,
                //AssassinsBlade.class,
                Tonfa.class,
                NinjaProsthesis.class,
                JavelinBarrel.class
                //Javelin.class
        };
        Category.WEP_T4.probs = new float[]{6, 5, 5, 5 , 4, 6};

        Category.WEP_T5.classes = new Class<?>[]{
                Greatsword.class,
                WarHammer.class,
                Glaive.class,
                Greataxe.class,
                Greatshield.class,
                Tamahawk.class
        };
        Category.WEP_T5.probs = new float[]{6, 5, 5, 4, 4, 6};
        //Category.WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4 };

        //see Generator.randomArmor
        Category.ARMOR.classes = new Class<?>[]{
                ClothArmor.class,
                LeatherArmor.class,
                MailArmor.class,
                ScaleArmor.class,
                PlateArmor.class};
        Category.ARMOR.probs = new float[]{0, 0, 0, 0, 0};

        Category.FOOD.classes = new Class<?>[]{
                Food.class,
                Pasty.class,
                MysteryMeat.class};
        Category.FOOD.probs = new float[]{4, 1, 0};

        Category.RING.classes = new Class<?>[]{
                RingOfAccuracy.class,
                RingOfEvasion.class,
                RingOfElements.class,
                RingOfForce.class,
                RingOfFuror.class,
                RingOfHaste.class,
                RingOfMagic.class, //currently removed from drop tables, pending rework
                RingOfMight.class,
                RingOfSharpshooting.class,
                RingOfTenacity.class,
                RingOfWealth.class};
        Category.RING.probs = new float[]{1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1};

        Category.ARTIFACT.classes = new Class<?>[]{
                CapeOfThorns.class,
                ChaliceOfBlood.class,
                CloakOfShadows.class,
                HornOfPlenty.class,
                MasterThievesArmband.class,
                SandalsOfNature.class,
                TalismanOfForesight.class,
                TimekeepersHourglass.class,
                UnstableSpellbook.class,
                AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
                DriedRose.class, //starts with no chance of spawning, chance is set directly after beating ghost quest.
                LloydsBeacon.class,
                EtherealChains.class
        };
        Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();

        Category.SEED.classes = new Class<?>[]{
                Firebloom.Seed.class,
                Icecap.Seed.class,
                Sorrowmoss.Seed.class,
                Blindweed.Seed.class,
                Sungrass.Seed.class,
                Earthroot.Seed.class,
                Fadeleaf.Seed.class,
                Rotberry.Seed.class,
                BlandfruitBush.Seed.class,
                Dreamfoil.Seed.class,
                Stormvine.Seed.class,
                Starflower.Seed.class};
        Category.SEED.probs = new float[]{12, 12, 12, 12, 12, 12, 12, 0, 4, 12, 12, 1};
    }

    public static void reset() {
        for (Category cat : Category.values()) {
            categoryProbs.put(cat, cat.prob);
        }
    }

    public static Item random() {
        return random(Random.chances(categoryProbs));
    }

    public static Item random(Category cat) {
        try {

            categoryProbs.put(cat, categoryProbs.get(cat) / 2);

            switch (cat) {
                case ARMOR:
                    return randomArmor();
                case WEAPON:
                    return randomWeapon();
                case ARTIFACT:
                    Item item = randomArtifact();
                    //if we're out of artifacts, return a ring instead.
                    return item != null ? item : random(Category.RING);
                default:
                    return ((Item) cat.classes[Random.chances(cat.probs)].newInstance()).random();
            }

        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    public static Item random(Class<? extends Item> cl) {
        try {

            return cl.newInstance().random();

        } catch (Exception e) {

            PixelDungeonOfTeller.reportException(e);
            return null;

        }
    }

    public static Armor randomArmor() {
        return randomArmor(Dungeon.depth / 5);
    }

    public static Armor randomArmor(int floorSet) {

        floorSet = (int) GameMath.gate(0, floorSet, floorSetTierProbs.length - 1);

        try {
            Armor a = (Armor) Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])].newInstance();
            a.random();
            return a;
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    public static Weapon randomWeapon() {
        return randomWeapon(Dungeon.depth / 5);
    }

    public static Weapon randomWeapon(int floorSet) {
        floorSet = (int) GameMath.gate(0, floorSet, floorSetTierProbs.length - 1);
        try {
            Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
            Weapon w = (Weapon) c.classes[Random.chances(c.probs)].newInstance();
            w.random();
            return w;
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    public static Weapon statictierrandomWeapon(int tier) {
        try {
            Category c = wepTiers[tier];
            Weapon w = (Weapon) c.classes[Random.chances(c.probs)].newInstance();
            w.random();
            return w;
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    //enforces uniqueness of artifacts throughout a run.
    public static Artifact randomArtifact() {

        try {
            Category cat = Category.ARTIFACT;
            int i = Random.chances(cat.probs);

            //if no artifacts are left, return null
            if (i == -1) {
                return null;
            }

            Artifact artifact = (Artifact) cat.classes[i].newInstance();

            //remove the chance of spawning this artifact.
            cat.probs[i] = 0;
            spawnedArtifacts.add(cat.classes[i].getSimpleName());

            artifact.random();

            return artifact;

        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    public static boolean removeArtifact(Artifact artifact) {
        if (spawnedArtifacts.contains(artifact.getClass().getSimpleName()))
            return false;

        Category cat = Category.ARTIFACT;
        for (int i = 0; i < cat.classes.length; i++)
            if (cat.classes[i].equals(artifact.getClass())) {
                if (cat.probs[i] == 1) {
                    cat.probs[i] = 0;
                    spawnedArtifacts.add(artifact.getClass().getSimpleName());
                    return true;
                } else
                    return false;
            }

        return false;
    }

    //resets artifact probabilities, for new dungeons
    public static void initArtifacts() {
        Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();

        //checks for dried rose quest completion, adds the rose in accordingly.
        if (Ghost.Quest.completed()) Category.ARTIFACT.probs[10] = 1;

        spawnedArtifacts = new ArrayList<String>();
    }

    //used to store information on which artifacts have been spawned.
    public static void storeInBundle(Bundle bundle) {
        bundle.put(ARTIFACTS, spawnedArtifacts.toArray(new String[spawnedArtifacts.size()]));
    }

    public static void restoreFromBundle(Bundle bundle) {
        initArtifacts();

        if (bundle.contains(ARTIFACTS)) {
            Collections.addAll(spawnedArtifacts, bundle.getStringArray(ARTIFACTS));
            Category cat = Category.ARTIFACT;

            for (String artifact : spawnedArtifacts)
                for (int i = 0; i < cat.classes.length; i++)
                    if (cat.classes[i].getSimpleName().equals(artifact))
                        cat.probs[i] = 0;
        }
    }

    public enum Category {
        WEAPON(100, Weapon.class),
        WEP_T1(0, Weapon.class),
        WEP_T2(0, Weapon.class),
        WEP_T3(0, Weapon.class),
        WEP_T4(0, Weapon.class),
        WEP_T5(0, Weapon.class),
        ARMOR(60, Armor.class),
        POTION(500, Potion.class),
        SCROLL(400, Scroll.class),
        WAND(40, Wand.class),
        RING(15, Ring.class),
        ARTIFACT(15, Artifact.class),
        SEED(50, Plant.Seed.class),
        FOOD(0, Food.class),
        GOLD(500, Gold.class);

        public Class<?>[] classes;
        public float[] probs;

        public float prob;
        public Class<? extends Item> superClass;

        Category(float prob, Class<? extends Item> superClass) {
            this.prob = prob;
            this.superClass = superClass;
        }

        public static int order(Item item) {
            for (int i = 0; i < values().length; i++) {
                if (values()[i].superClass.isInstance(item)) {
                    return i;
                }
            }

            return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
        }
    }
}
