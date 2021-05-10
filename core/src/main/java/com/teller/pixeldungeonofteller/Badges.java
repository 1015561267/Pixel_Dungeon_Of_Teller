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
package com.teller.pixeldungeonofteller;

import com.teller.pixeldungeonofteller.actors.mobs.Acidic;
import com.teller.pixeldungeonofteller.actors.mobs.Albino;
import com.teller.pixeldungeonofteller.actors.mobs.Bandit;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.actors.mobs.Senior;
import com.teller.pixeldungeonofteller.actors.mobs.Shielded;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.artifacts.Artifact;
import com.teller.pixeldungeonofteller.items.bags.PotionBandolier;
import com.teller.pixeldungeonofteller.items.bags.ScrollHolder;
import com.teller.pixeldungeonofteller.items.bags.SeedPouch;
import com.teller.pixeldungeonofteller.items.bags.WandHolster;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Badges {

    public enum Badge {//2021/5/9 improvement of badges from SPD 0.9.0
        MONSTERS_SLAIN_1(0),
        MONSTERS_SLAIN_2(1),
        MONSTERS_SLAIN_3(2),
        MONSTERS_SLAIN_4(3),

        GOLD_COLLECTED_1(4),
        GOLD_COLLECTED_2(5),
        GOLD_COLLECTED_3(6),
        GOLD_COLLECTED_4(7),

        LEVEL_REACHED_1(8),
        LEVEL_REACHED_2(9),
        LEVEL_REACHED_3(10),
        LEVEL_REACHED_4(11),

        BOSS_SLAIN_1_WARRIOR,
        BOSS_SLAIN_1_MAGE,
        BOSS_SLAIN_1_ROGUE,
        BOSS_SLAIN_1_HUNTRESS,
        BOSS_SLAIN_1(12),
        BOSS_SLAIN_2(13),
        BOSS_SLAIN_3(14),
        BOSS_SLAIN_4(15),
        BOSS_SLAIN_1_ALL_CLASSES(60, true),
        BOSS_SLAIN_3_GLADIATOR,
        BOSS_SLAIN_3_BERSERKER,
        BOSS_SLAIN_3_WARLOCK,
        BOSS_SLAIN_3_BATTLEMAGE,
        BOSS_SLAIN_3_FREERUNNER,
        BOSS_SLAIN_3_ASSASSIN,
        BOSS_SLAIN_3_SNIPER,
        BOSS_SLAIN_3_WARDEN,
        BOSS_SLAIN_3_ALL_SUBCLASSES(61, true),

        STRENGTH_ATTAINED_1(16),
        STRENGTH_ATTAINED_2(17),
        STRENGTH_ATTAINED_3(18),
        STRENGTH_ATTAINED_4(19),

        FOOD_EATEN_1(20),
        FOOD_EATEN_2(21),
        FOOD_EATEN_3(22),
        FOOD_EATEN_4(23),

        ITEM_LEVEL_1(24),
        ITEM_LEVEL_2(25),
        ITEM_LEVEL_3(26),
        ITEM_LEVEL_4(27),

        POTIONS_COOKED_1(28),
        POTIONS_COOKED_2(29),
        POTIONS_COOKED_3(30),
        POTIONS_COOKED_4(31),

        DEATH_FROM_FIRE(32),
        DEATH_FROM_POISON(33),
        DEATH_FROM_GAS(34),
        DEATH_FROM_HUNGER(35),
        DEATH_FROM_FALLING(36),
        DEATH_FROM_GLYPH(37),
        YASD(38, true),

        RARE_ALBINO,
        RARE_BANDIT,
        RARE_SHIELDED,
        RARE_SENIOR,
        RARE_ACIDIC,
        RARE(39, true),

        NO_MONSTERS_SLAIN(40),
        GRIM_WEAPON(41),
        NIGHT_HUNTER(42),
        PIRANHAS(43),

        BAG_BOUGHT_SEED_POUCH,
        BAG_BOUGHT_SCROLL_HOLDER,
        BAG_BOUGHT_POTION_BANDOLIER,
        BAG_BOUGHT_WAND_HOLSTER,
        ALL_BAGS_BOUGHT(44),

        MASTERY_COMBO(45),

        VICTORY_WARRIOR,
        VICTORY_MAGE,
        VICTORY_ROGUE,
        VICTORY_HUNTRESS,
        VICTORY(46),
        VICTORY_ALL_CLASSES(63, true),

        HAPPY_END(47),

        ALL_POTIONS_IDENTIFIED(48),
        ALL_SCROLLS_IDENTIFIED(49),
        ALL_RINGS_IDENTIFIED(53),
        ALL_WANDS_IDENTIFIED(54),
        ALL_ITEMS_IDENTIFIED(55, true),

        GAMES_PLAYED_1(56, true),
        GAMES_PLAYED_2(57, true),
        GAMES_PLAYED_3(58, true),
        GAMES_PLAYED_4(59, true),

        MASTERY_WARRIOR,
        MASTERY_MAGE,
        MASTERY_ROGUE,
        MASTERY_HUNTRESS,
        TUTORIAL_WARRIOR,
        TUTORIAL_MAGE,

        CHAMPION(66, true),
        SUPPORTER(67, true),
        RING_OF_HAGGLER(72),
        RING_OF_THORNS(73);

        public boolean meta;

        public int image;

        Badge(int image) {
            this(image, false);
        }

        Badge(int image, boolean meta) {
            this.image = image;
            this.meta = meta;
        }

        Badge() {
            this(-1);
        }

        public String desc() {
            return Messages.get(this, name());
        }
    }

    private static final Badge[][] tierBadgeReplacements = new Badge[][]{
            {Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4},
            {Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4},
            {Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4},
            {Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4},
            {Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4},
            {Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4},
            {Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4 },
            {Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4},
            {Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4},
    };

    private static final Badge[][] metaBadgeReplacements = new Badge[][]{
            {Badge.DEATH_FROM_FIRE, Badge.YASD},
            {Badge.DEATH_FROM_GAS, Badge.YASD},
            {Badge.DEATH_FROM_HUNGER, Badge.YASD},
            {Badge.DEATH_FROM_POISON, Badge.YASD},
            {Badge.DEATH_FROM_GLYPH, Badge.YASD},
            {Badge.DEATH_FROM_FALLING, Badge.YASD },

            {Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
            {Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
            {Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
            {Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED}
    };

    public static final String BADGES_FILE = "badges.dat";
    private static final String BADGES = "badges";
    public static Callback loadingListener = null;
    private static HashSet<Badge> global;
    private static HashSet<Badge> local = new HashSet<Badges.Badge>();
    private static boolean saveNeeded = false;

    public static void reset() {
        local.clear();
        loadGlobal();
    }

    private static HashSet<Badge> restore(Bundle bundle) {
        HashSet<Badge> badges = new HashSet<Badge>();
        if (bundle == null) return badges;

        String[] names = bundle.getStringArray(BADGES);
        for (int i = 0; i < names.length; i++) {
            try {
                badges.add(Badge.valueOf(names[i]));
            } catch (Exception e) {
                PixelDungeonOfTeller.reportException(e);
            }
        }

        addReplacedBadges(badges);

        return badges;
    }

    private static void store(Bundle bundle, HashSet<Badge> badges) {
        addReplacedBadges(badges);

        int count = 0;
        String[] names = new String[badges.size()];

        for (Badge badge : badges) {
            names[count++] = badge.toString();
        }
        bundle.put(BADGES, names);
    }

    public static void loadLocal(Bundle bundle) {
        local = restore(bundle);
    }

    public static void saveLocal(Bundle bundle) {
        store(bundle, local);
    }

    public static void loadGlobal() {
        if (global == null) {
            try {
                Bundle bundle = FileUtils.bundleFromFile( BADGES_FILE );
                global = restore(bundle);

            } catch (IOException e) {
                global = new HashSet<Badge>();
            }
        }
    }

    public static void saveGlobal() {
        if (saveNeeded) {

            Bundle bundle = new Bundle();
            store(bundle, global);

            try {
                FileUtils.bundleToFile(BADGES_FILE, bundle);
                saveNeeded = false;
            } catch (IOException e) {
                PixelDungeonOfTeller.reportException(e);
            }
        }
    }

    public static void validateMonstersSlain() {
        Badge badge = null;

        if (!local.contains(Badge.MONSTERS_SLAIN_1) && Statistics.enemiesSlain >= 10) {
            badge = Badge.MONSTERS_SLAIN_1;
            local.add(badge);
        }
        if (!local.contains(Badge.MONSTERS_SLAIN_2) && Statistics.enemiesSlain >= 50) {
            badge = Badge.MONSTERS_SLAIN_2;
            local.add(badge);
        }
        if (!local.contains(Badge.MONSTERS_SLAIN_3) && Statistics.enemiesSlain >= 150) {
            badge = Badge.MONSTERS_SLAIN_3;
            local.add(badge);
        }
        if (!local.contains(Badge.MONSTERS_SLAIN_4) && Statistics.enemiesSlain >= 250) {
            badge = Badge.MONSTERS_SLAIN_4;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validateGoldCollected() {
        Badge badge = null;

        if (!local.contains(Badge.GOLD_COLLECTED_1) && Statistics.goldCollected >= 100) {
            badge = Badge.GOLD_COLLECTED_1;
            local.add(badge);
        }
        if (!local.contains(Badge.GOLD_COLLECTED_2) && Statistics.goldCollected >= 500) {
            badge = Badge.GOLD_COLLECTED_2;
            local.add(badge);
        }
        if (!local.contains(Badge.GOLD_COLLECTED_3) && Statistics.goldCollected >= 2500) {
            badge = Badge.GOLD_COLLECTED_3;
            local.add(badge);
        }
        if (!local.contains(Badge.GOLD_COLLECTED_4) && Statistics.goldCollected >= 7500) {
            badge = Badge.GOLD_COLLECTED_4;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validateLevelReached() {
        Badge badge = null;

        if (!local.contains(Badge.LEVEL_REACHED_1) && Dungeon.hero.lvl >= 6) {
            badge = Badge.LEVEL_REACHED_1;
            local.add(badge);
        }
        if (!local.contains(Badge.LEVEL_REACHED_2) && Dungeon.hero.lvl >= 12) {
            badge = Badge.LEVEL_REACHED_2;
            local.add(badge);
        }
        if (!local.contains(Badge.LEVEL_REACHED_3) && Dungeon.hero.lvl >= 18) {
            badge = Badge.LEVEL_REACHED_3;
            local.add(badge);
        }
        if (!local.contains(Badge.LEVEL_REACHED_4) && Dungeon.hero.lvl >= 24) {
            badge = Badge.LEVEL_REACHED_4;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validateStrengthAttained() {
        Badge badge = null;

        if (!local.contains(Badge.STRENGTH_ATTAINED_1) && Dungeon.hero.STR >= 13) {
            badge = Badge.STRENGTH_ATTAINED_1;
            local.add(badge);
        }
        if (!local.contains(Badge.STRENGTH_ATTAINED_2) && Dungeon.hero.STR >= 15) {
            badge = Badge.STRENGTH_ATTAINED_2;
            local.add(badge);
        }
        if (!local.contains(Badge.STRENGTH_ATTAINED_3) && Dungeon.hero.STR >= 17) {
            badge = Badge.STRENGTH_ATTAINED_3;
            local.add(badge);
        }
        if (!local.contains(Badge.STRENGTH_ATTAINED_4) && Dungeon.hero.STR >= 19) {
            badge = Badge.STRENGTH_ATTAINED_4;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validateFoodEaten() {
        Badge badge = null;

        if (!local.contains(Badge.FOOD_EATEN_1) && Statistics.foodEaten >= 10) {
            badge = Badge.FOOD_EATEN_1;
            local.add(badge);
        }
        if (!local.contains(Badge.FOOD_EATEN_2) && Statistics.foodEaten >= 20) {
            badge = Badge.FOOD_EATEN_2;
            local.add(badge);
        }
        if (!local.contains(Badge.FOOD_EATEN_3) && Statistics.foodEaten >= 30) {
            badge = Badge.FOOD_EATEN_3;
            local.add(badge);
        }
        if (!local.contains(Badge.FOOD_EATEN_4) && Statistics.foodEaten >= 40) {
            badge = Badge.FOOD_EATEN_4;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validatePotionsCooked() {
        Badge badge = null;

        if (!local.contains(Badge.POTIONS_COOKED_1) && Statistics.potionsCooked >= 3) {
            badge = Badge.POTIONS_COOKED_1;
            local.add(badge);
        }
        if (!local.contains(Badge.POTIONS_COOKED_2) && Statistics.potionsCooked >= 6) {
            badge = Badge.POTIONS_COOKED_2;
            local.add(badge);
        }
        if (!local.contains(Badge.POTIONS_COOKED_3) && Statistics.potionsCooked >= 9) {
            badge = Badge.POTIONS_COOKED_3;
            local.add(badge);
        }
        if (!local.contains(Badge.POTIONS_COOKED_4) && Statistics.potionsCooked >= 12) {
            badge = Badge.POTIONS_COOKED_4;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validatePiranhasKilled() {
        Badge badge = null;

        if (!local.contains(Badge.PIRANHAS) && Statistics.piranhasKilled >= 6) {
            badge = Badge.PIRANHAS;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validateItemLevelAquired(Item item) {

        // This method should be called:
        // 1) When an item is obtained (Item.collect)
        // 2) When an item is upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
        // 3) When an item is identified

        // Note that artifacts should never trigger this badge as they are alternatively upgraded
        if (!item.levelKnown || item instanceof Artifact) {
            return;
        }

        Badge badge = null;
        if (!local.contains(Badge.ITEM_LEVEL_1) && item.level() >= 3) {
            badge = Badge.ITEM_LEVEL_1;
            local.add(badge);
        }
        if (!local.contains(Badge.ITEM_LEVEL_2) && item.level() >= 6) {
            badge = Badge.ITEM_LEVEL_2;
            local.add(badge);
        }
        if (!local.contains(Badge.ITEM_LEVEL_3) && item.level() >= 9) {
            badge = Badge.ITEM_LEVEL_3;
            local.add(badge);
        }
        if (!local.contains(Badge.ITEM_LEVEL_4) && item.level() >= 12) {
            badge = Badge.ITEM_LEVEL_4;
            local.add(badge);
        }

        displayBadge(badge);
    }

    public static void validateAllPotionsIdentified() {
        if (Dungeon.hero != null && Dungeon.hero.isAlive() &&
                !local.contains(Badge.ALL_POTIONS_IDENTIFIED) && Potion.allKnown()) {

            Badge badge = Badge.ALL_POTIONS_IDENTIFIED;
            local.add(badge);
            displayBadge(badge);

            validateAllItemsIdentified();
        }
    }

    public static void validateAllScrollsIdentified() {
        if (Dungeon.hero != null && Dungeon.hero.isAlive() &&
                !local.contains(Badge.ALL_SCROLLS_IDENTIFIED) && Scroll.allKnown()) {

            Badge badge = Badge.ALL_SCROLLS_IDENTIFIED;
            local.add(badge);
            displayBadge(badge);

            validateAllItemsIdentified();
        }
    }

    public static void validateAllRingsIdentified() {
        if (Dungeon.hero != null && Dungeon.hero.isAlive() &&
                !local.contains(Badge.ALL_RINGS_IDENTIFIED) && Ring.allKnown()) {

            Badge badge = Badge.ALL_RINGS_IDENTIFIED;
            local.add(badge);
            displayBadge(badge);

            validateAllItemsIdentified();
        }
    }

    /**
     * public static void validateAllWandsIdentified() {
     * if (Dungeon.hero != null && Dungeon.hero.isAlive() &&
     * !local.contains( Badge.ALL_WANDS_IDENTIFIED ) && Wand.allKnown()) {
     * <p>
     * Badge badge = Badge.ALL_WANDS_IDENTIFIED;
     * local.add( badge );
     * displayBadge( badge );
     * <p>
     * validateAllItemsIdentified();
     * }
     * }
     */

    public static void validateAllBagsBought(Item bag) {

        Badge badge = null;
        if (bag instanceof SeedPouch) {
            badge = Badge.BAG_BOUGHT_SEED_POUCH;
        } else if (bag instanceof ScrollHolder) {
            badge = Badge.BAG_BOUGHT_SCROLL_HOLDER;
        } else if (bag instanceof PotionBandolier) {
            badge = Badge.BAG_BOUGHT_POTION_BANDOLIER;
        } else if (bag instanceof WandHolster) {
            badge = Badge.BAG_BOUGHT_WAND_HOLSTER;
        }

        if (badge != null) {

            local.add(badge);

            if (!local.contains(Badge.ALL_BAGS_BOUGHT) &&
                    local.contains(Badge.BAG_BOUGHT_SEED_POUCH) &&
                    local.contains(Badge.BAG_BOUGHT_SCROLL_HOLDER) &&
                    local.contains(Badge.BAG_BOUGHT_POTION_BANDOLIER) &&
                    local.contains(Badge.BAG_BOUGHT_WAND_HOLSTER)) {

                badge = Badge.ALL_BAGS_BOUGHT;
                local.add(badge);
                displayBadge(badge);
            }
        }
    }

    //TODO: no longer in use, deal with new wand related badges in the badge rework.

    public static void validateAllItemsIdentified() {
        if (!global.contains(Badge.ALL_ITEMS_IDENTIFIED) &&
                global.contains(Badge.ALL_POTIONS_IDENTIFIED) &&
                global.contains(Badge.ALL_SCROLLS_IDENTIFIED) &&
                global.contains(Badge.ALL_RINGS_IDENTIFIED)) {
            //global.contains( Badge.ALL_WANDS_IDENTIFIED )) {

            Badge badge = Badge.ALL_ITEMS_IDENTIFIED;
            displayBadge(badge);
        }
    }

    public static void validateDeathFromFire() {
        Badge badge = Badge.DEATH_FROM_FIRE;
        local.add(badge);
        displayBadge(badge);

        validateYASD();
    }

    public static void validateDeathFromPoison() {
        Badge badge = Badge.DEATH_FROM_POISON;
        local.add(badge);
        displayBadge(badge);

        validateYASD();
    }

    public static void validateDeathFromGas() {
        Badge badge = Badge.DEATH_FROM_GAS;
        local.add(badge);
        displayBadge(badge);

        validateYASD();
    }

    public static void validateDeathFromHunger() {
        Badge badge = Badge.DEATH_FROM_HUNGER;
        local.add(badge);
        displayBadge(badge);

        validateYASD();
    }

    public static void validateDeathFromGlyph() {
        Badge badge = Badge.DEATH_FROM_GLYPH;
        local.add(badge);
        displayBadge(badge);
    }

    public static void validateDeathFromFalling() {
        Badge badge = Badge.DEATH_FROM_FALLING;
        local.add(badge);
        displayBadge(badge);
    }

    private static void validateYASD() {
        if (global.contains(Badge.DEATH_FROM_FIRE) &&
                global.contains(Badge.DEATH_FROM_POISON) &&
                global.contains(Badge.DEATH_FROM_GAS) &&
                global.contains(Badge.DEATH_FROM_HUNGER)) {

            Badge badge = Badge.YASD;
            local.add(badge);
            displayBadge(badge);
        }
    }

    public static void validateBossSlain() {
        Badge badge = null;
        switch (Dungeon.depth) {
            case 5:
                badge = Badge.BOSS_SLAIN_1;
                break;
            case 10:
                badge = Badge.BOSS_SLAIN_2;
                break;
            case 15:
                badge = Badge.BOSS_SLAIN_3;
                break;
            case 20:
                badge = Badge.BOSS_SLAIN_4;
                break;
        }

        if (badge != null) {
            local.add(badge);
            displayBadge(badge);

            if (badge == Badge.BOSS_SLAIN_1) {
                switch (Dungeon.hero.heroClass) {
                    case WARRIOR:
                        badge = Badge.BOSS_SLAIN_1_WARRIOR;
                        break;
                    case MAGE:
                        badge = Badge.BOSS_SLAIN_1_MAGE;
                        break;
                    case ROGUE:
                        badge = Badge.BOSS_SLAIN_1_ROGUE;
                        break;
                    case HUNTRESS:
                        badge = Badge.BOSS_SLAIN_1_HUNTRESS;
                        break;
                }
                local.add(badge);
                if (!global.contains(badge)) {
                    global.add(badge);
                    saveNeeded = true;
                }

                if (global.contains(Badge.BOSS_SLAIN_1_WARRIOR) &&
                        global.contains(Badge.BOSS_SLAIN_1_MAGE) &&
                        global.contains(Badge.BOSS_SLAIN_1_ROGUE) &&
                        global.contains(Badge.BOSS_SLAIN_1_HUNTRESS)) {

                    badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
                    if (!global.contains(badge)) {
                        displayBadge(badge);
                        global.add(badge);
                        saveNeeded = true;
                    }
                }
            } else if (badge == Badge.BOSS_SLAIN_3) {
                switch (Dungeon.hero.subClass) {
                    case GLADIATOR:
                        badge = Badge.BOSS_SLAIN_3_GLADIATOR;
                        break;
                    case BERSERKER:
                        badge = Badge.BOSS_SLAIN_3_BERSERKER;
                        break;
                    case WARLOCK:
                        badge = Badge.BOSS_SLAIN_3_WARLOCK;
                        break;
                    case BATTLEMAGE:
                        badge = Badge.BOSS_SLAIN_3_BATTLEMAGE;
                        break;
                    case FREERUNNER:
                        badge = Badge.BOSS_SLAIN_3_FREERUNNER;
                        break;
                    case ASSASSIN:
                        badge = Badge.BOSS_SLAIN_3_ASSASSIN;
                        break;
                    case SNIPER:
                        badge = Badge.BOSS_SLAIN_3_SNIPER;
                        break;
                    case WARDEN:
                        badge = Badge.BOSS_SLAIN_3_WARDEN;
                        break;
                    default:
                        return;
                }
                local.add(badge);
                if (!global.contains(badge)) {
                    global.add(badge);
                    saveNeeded = true;
                }

                if (global.contains(Badge.BOSS_SLAIN_3_GLADIATOR) &&
                        global.contains(Badge.BOSS_SLAIN_3_BERSERKER) &&
                        global.contains(Badge.BOSS_SLAIN_3_WARLOCK) &&
                        global.contains(Badge.BOSS_SLAIN_3_BATTLEMAGE) &&
                        global.contains(Badge.BOSS_SLAIN_3_FREERUNNER) &&
                        global.contains(Badge.BOSS_SLAIN_3_ASSASSIN) &&
                        global.contains(Badge.BOSS_SLAIN_3_SNIPER) &&
                        global.contains(Badge.BOSS_SLAIN_3_WARDEN)) {

                    badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
                    if (!global.contains(badge)) {
                        displayBadge(badge);
                        global.add(badge);
                        saveNeeded = true;
                    }
                }
            }
        }
    }

    public static void validateMastery() {

        Badge badge = null;
        switch (Dungeon.hero.heroClass) {
            case WARRIOR:
                badge = Badge.MASTERY_WARRIOR;
                break;
            case MAGE:
                badge = Badge.MASTERY_MAGE;
                break;
            case ROGUE:
                badge = Badge.MASTERY_ROGUE;
                break;
            case HUNTRESS:
                badge = Badge.MASTERY_HUNTRESS;
                break;
        }

        if (!global.contains(badge)) {
            global.add(badge);
            saveNeeded = true;
        }
    }

    public static void validateMasteryCombo(int n) {
        if (!local.contains(Badge.MASTERY_COMBO) && n == 10) {
            Badge badge = Badge.MASTERY_COMBO;
            local.add(badge);
            displayBadge(badge);
        }
    }

    //TODO: Replace this badge, delayed until an eventual badge rework
    public static void validateRingOfHaggler() {
        if (!local.contains(Badge.RING_OF_HAGGLER)/* && new RingOfThorns().isKnown()*/) {
            Badge badge = Badge.RING_OF_HAGGLER;
            local.add(badge);
            displayBadge(badge);
        }
    }

    //TODO: Replace this badge, delayed until an eventual badge rework
    public static void validateRingOfThorns() {
        if (!local.contains(Badge.RING_OF_THORNS)/* && new RingOfThorns().isKnown()*/) {
            Badge badge = Badge.RING_OF_THORNS;
            local.add(badge);
            displayBadge(badge);
        }
    }

    public static void validateRare(Mob mob) {

        Badge badge = null;
        if (mob instanceof Albino) {
            badge = Badge.RARE_ALBINO;
        } else if (mob instanceof Bandit) {
            badge = Badge.RARE_BANDIT;
        } else if (mob instanceof Shielded) {
            badge = Badge.RARE_SHIELDED;
        } else if (mob instanceof Senior) {
            badge = Badge.RARE_SENIOR;
        } else if (mob instanceof Acidic) {
            badge = Badge.RARE_ACIDIC;
        }
        if (!global.contains(badge)) {
            global.add(badge);
            saveNeeded = true;
        }

        if (global.contains(Badge.RARE_ALBINO) &&
                global.contains(Badge.RARE_BANDIT) &&
                global.contains(Badge.RARE_SHIELDED) &&
                global.contains(Badge.RARE_SENIOR) &&
                global.contains(Badge.RARE_ACIDIC)) {

            badge = Badge.RARE;
            displayBadge(badge);
        }
    }

    public static void validateVictory() {

        Badge badge = Badge.VICTORY;
        displayBadge(badge);

        switch (Dungeon.hero.heroClass) {
            case WARRIOR:
                badge = Badge.VICTORY_WARRIOR;
                break;
            case MAGE:
                badge = Badge.VICTORY_MAGE;
                break;
            case ROGUE:
                badge = Badge.VICTORY_ROGUE;
                break;
            case HUNTRESS:
                badge = Badge.VICTORY_HUNTRESS;
                break;
        }
        local.add(badge);
        if (!global.contains(badge)) {
            global.add(badge);
            saveNeeded = true;
        }

        if (global.contains(Badge.VICTORY_WARRIOR) &&
                global.contains(Badge.VICTORY_MAGE) &&
                global.contains(Badge.VICTORY_ROGUE) &&
                global.contains(Badge.VICTORY_HUNTRESS)) {

            badge = Badge.VICTORY_ALL_CLASSES;
            displayBadge(badge);
        }
    }

    public static void validateTutorial() {
        Badge badge = null;
        switch (Dungeon.hero.heroClass) {
            case WARRIOR:
                badge = Badge.TUTORIAL_WARRIOR;
                break;
            case MAGE:
                badge = Badge.TUTORIAL_MAGE;
                break;
            default:
                break;
        }

        if (badge != null) {
            local.add(badge);
            if (!global.contains(badge)) {
                global.add(badge);
                saveNeeded = true;
            }
        }
    }

    public static void validateNoKilling() {
        if (!local.contains(Badge.NO_MONSTERS_SLAIN) && Statistics.completedWithNoKilling) {
            Badge badge = Badge.NO_MONSTERS_SLAIN;
            local.add(badge);
            displayBadge(badge);
        }
    }

    public static void validateGrimWeapon() {
        if (!local.contains(Badge.GRIM_WEAPON)) {
            Badge badge = Badge.GRIM_WEAPON;
            local.add(badge);
            displayBadge(badge);
        }
    }

    public static void validateNightHunter() {
        if (!local.contains(Badge.NIGHT_HUNTER) && Statistics.nightHunt >= 15) {
            Badge badge = Badge.NIGHT_HUNTER;
            local.add(badge);
            displayBadge(badge);
        }
    }

    public static void validateSupporter() {

        global.add(Badge.SUPPORTER);
        saveNeeded = true;

        PixelScene.showBadge(Badge.SUPPORTER);
    }

    public static void validateGamesPlayed() {
        Badge badge = null;
        if (Rankings.INSTANCE.totalNumber >= 10) {
            badge = Badge.GAMES_PLAYED_1;
        }
        if (Rankings.INSTANCE.totalNumber >= 100) {
            badge = Badge.GAMES_PLAYED_2;
        }
        if (Rankings.INSTANCE.totalNumber >= 500) {
            badge = Badge.GAMES_PLAYED_3;
        }
        if (Rankings.INSTANCE.totalNumber >= 2000) {
            badge = Badge.GAMES_PLAYED_4;
        }

        displayBadge(badge);
    }

    public static void validateHappyEnd() {
        displayBadge(Badge.HAPPY_END);
    }

    public static void validateChampion() {
        displayBadge(Badge.CHAMPION);
    }

    private static void displayBadge(Badge badge) {

        if (badge == null) {
            return;
        }

        if (global.contains(badge)) {

            if (!badge.meta) {
                GLog.h(Messages.get(Badges.class, "endorsed", badge.desc()));
            }

        } else {

            global.add(badge);
            saveNeeded = true;

            if (badge.meta) {
                GLog.h(Messages.get(Badges.class, "new_super", badge.desc()));
            } else {
                GLog.h(Messages.get(Badges.class, "new", badge.desc()));
            }
            PixelScene.showBadge(badge);
        }
    }

    public static boolean isUnlocked(Badge badge) {
        return global.contains(badge);
    }

    public static void disown(Badge badge) {
        loadGlobal();
        global.remove(badge);
        saveNeeded = true;
    }

    public static List<Badge> filterReplacedBadges( boolean global ) {

        ArrayList<Badge> badges = new ArrayList<>(global ? Badges.global : Badges.local);

        Iterator<Badge> iterator = badges.iterator();
        while (iterator.hasNext()) {
            Badge badge = iterator.next();
            if ((!global && badge.meta) || badge.image == -1) {
                iterator.remove();
            }
        }

        Collections.sort(badges);

        return filterReplacedBadges(badges);

    }

    public static List<Badge> filterReplacedBadges( List<Badge> badges ) {

        leaveBest( badges, Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4 );
        leaveBest( badges, Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4 );
        leaveBest( badges, Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4 );
        leaveBest( badges, Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4 );
        leaveBest( badges, Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4 );
        leaveBest( badges, Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4 );
        leaveBest( badges, Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4 );
        leaveBest( badges, Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4 );
        leaveBest( badges, Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4 );

        for (Badge[] tierReplace : tierBadgeReplacements){
            leaveBest( badges, tierReplace );
        }

        for (Badge[] metaReplace : metaBadgeReplacements){
            leaveBest( badges, metaReplace );
        }

        return badges;
    }

    public static List<Badge> filtered(boolean global) {

        HashSet<Badge> filtered = new HashSet<Badge>(global ? Badges.global : Badges.local);

        Iterator<Badge> iterator = filtered.iterator();
        while (iterator.hasNext()) {
            Badge badge = iterator.next();
            if ((!global && badge.meta) || badge.image == -1) {
                iterator.remove();
            }
        }
        leaveBest(filtered, Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4);
        leaveBest(filtered, Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4);
        leaveBest(filtered, Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4);
        leaveBest(filtered, Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4);
        leaveBest(filtered, Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4);
        leaveBest(filtered, Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4);
        leaveBest(filtered, Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4);
        leaveBest(filtered, Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4);
        leaveBest(filtered, Badge.BOSS_SLAIN_1_ALL_CLASSES, Badge.BOSS_SLAIN_3_ALL_SUBCLASSES);
        leaveBest(filtered, Badge.DEATH_FROM_FIRE, Badge.YASD);
        leaveBest(filtered, Badge.DEATH_FROM_GAS, Badge.YASD);
        leaveBest(filtered, Badge.DEATH_FROM_HUNGER, Badge.YASD);
        leaveBest(filtered, Badge.DEATH_FROM_POISON, Badge.YASD);
        leaveBest(filtered, Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED);
        leaveBest(filtered, Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED);
        leaveBest(filtered, Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED);
        leaveBest(filtered, Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED);
        leaveBest(filtered, Badge.VICTORY, Badge.VICTORY_ALL_CLASSES);
        leaveBest(filtered, Badge.VICTORY, Badge.HAPPY_END);
        leaveBest(filtered, Badge.VICTORY, Badge.CHAMPION);
        leaveBest(filtered, Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4);

        ArrayList<Badge> list = new ArrayList<Badge>(filtered);
        Collections.sort(list);

        return list;
    }
    public static List<Badge> filterHigherIncrementalBadges(List<Badges.Badge> badges ) {

        for (Badge[] tierReplace : tierBadgeReplacements){
            leaveWorst( badges, tierReplace );
        }

        Collections.sort( badges );

        return badges;
    }

    public static Collection<Badge> addReplacedBadges(Collection<Badges.Badge> badges ) {

        for (Badge[] tierReplace : tierBadgeReplacements){
            addLower( badges, tierReplace );
        }

        for (Badge[] metaReplace : metaBadgeReplacements){
            addLower( badges, metaReplace );
        }

        return badges;
    }

    private static void addLower( Collection<Badge> list, Badge...badges ) {
        for (int i=badges.length-1; i > 0; i--) {
            if (list.contains( badges[i])) {
                for (int j=0; j < i; j++) {
                    list.add( badges[j] );
                }
                break;
            }
        }
    }

    private static void leaveBest(Collection<Badge> list, Badge...badges ) {
        for (int i=badges.length-1; i > 0; i--) {
            if (list.contains( badges[i])) {
                for (int j=0; j < i; j++) {
                    list.remove( badges[j] );
                }
                break;
            }
        }
    }

    private static void leaveWorst( Collection<Badge> list, Badge...badges ) {
        for (int i=0; i < badges.length; i++) {
            if (list.contains( badges[i])) {
                for (int j=i+1; j < badges.length; j++) {
                    list.remove( badges[j] );
                }
                break;
            }
        }
    }


}
