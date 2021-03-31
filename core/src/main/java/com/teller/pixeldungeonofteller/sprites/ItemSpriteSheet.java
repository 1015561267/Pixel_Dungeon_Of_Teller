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
package com.teller.pixeldungeonofteller.sprites;

import com.teller.pixeldungeonofteller.Assets;
import com.watabou.noosa.TextureFilm;

public class ItemSpriteSheet {

    private static final int WIDTH = 16;

    public static TextureFilm film = new TextureFilm( Assets.ITEMS, 16, 16 );

    private static final int PLACEHOLDERS = xy(1, 1);   //8 slots
    //null warning occupies space 0, should only show up if there's a bug.
    public static final int NULLWARN             = PLACEHOLDERS + 0;
    public static final int WEAPON_HOLDER        = PLACEHOLDERS + 1;
    public static final int OFFHANDWEAPON_HOLDER = PLACEHOLDERS + 2;
    public static final int ARMOR_HOLDER         = PLACEHOLDERS + 3;
    public static final int WAND_HOLDER          = PLACEHOLDERS + 4;
    public static final int RING_HOLDER          = PLACEHOLDERS + 5;
    public static final int ARTFICT_HOLDER       = PLACEHOLDERS + 6;
    public static final int POTION_HOLDER        = PLACEHOLDERS + 7;
    public static final int SCROLL_HOLDER        = PLACEHOLDERS + 8;
    public static final int SOMETHING = PLACEHOLDERS + 9;



    private static final int UNCOLLECTIBLE = xy(11, 1);   //8 slots
    public static final int GOLD = UNCOLLECTIBLE + 0;
    public static final int DEWDROP = UNCOLLECTIBLE + 1;
    public static final int PETAL = UNCOLLECTIBLE + 2;
    public static final int SANDBAG = UNCOLLECTIBLE + 3;
    public static final int DBL_BOMB = UNCOLLECTIBLE + 4;
    public static final int GUIDE_PAGE=UNCOLLECTIBLE+5;

    static{
        assignItemRect(GOLD,        1 ,1,15, 14);
        assignItemRect(DEWDROP,     3 ,3,10, 10);
        assignItemRect(PETAL,       4 ,4,8,  8);
        assignItemRect(SANDBAG,     3 ,3,10, 10);
        assignItemRect(DBL_BOMB,    1 ,1,15, 13);
        assignItemRect(GUIDE_PAGE,  3 ,2,10, 11);
    }


    private static final int CONTAINERS = xy(1, 2);   //16 slots
    public static final int BONES = CONTAINERS + 0;
    public static final int REMAINS = CONTAINERS + 1;
    public static final int TOMB = CONTAINERS + 2;
    public static final int GRAVE = CONTAINERS + 3;
    public static final int CHEST = CONTAINERS + 4;
    public static final int LOCKED_CHEST = CONTAINERS + 5;
    public static final int CRYSTAL_CHEST = CONTAINERS + 6;
    public static final int SWITCH = CONTAINERS + 7;
    static{
        assignItemRect(BONES,           0,1,15, 14);
        assignItemRect(REMAINS,         0,1,15, 14);
        assignItemRect(TOMB,            1,0,14, 15);
        assignItemRect(GRAVE,           1,0,14, 15);
        assignItemRect(CHEST,           0,1,16, 14);
        assignItemRect(LOCKED_CHEST,    0,1,16, 14);
        assignItemRect(CRYSTAL_CHEST,   0,1,16, 14);
        assignItemRect(SWITCH,          2,0,12, 15);
    }



    private static final int SINGLE_USE = xy(1, 3);   //32 slots
    public static final int ANKH = SINGLE_USE + 0;
    public static final int STYLUS = SINGLE_USE + 1;
    public static final int WEIGHT = SINGLE_USE + 2;
    public static final int SEAL = SINGLE_USE + 3;
    public static final int TORCH = SINGLE_USE + 4;
    public static final int BEACON = SINGLE_USE + 5;
    public static final int BOMB = SINGLE_USE + 6;
    public static final int HONEYPOT = SINGLE_USE + 7;
    public static final int SHATTPOT = SINGLE_USE + 8;
    public static final int IRON_KEY = SINGLE_USE + 9;
    public static final int GOLDEN_KEY = SINGLE_USE + 10;
    public static final int CRYSTAL_KEY=SINGLE_USE+11;
    public static final int SKELETON_KEY = SINGLE_USE + 12;
    public static final int MASTERY = SINGLE_USE + 13;
    public static final int KIT = SINGLE_USE + 14;
    public static final int AMULET = SINGLE_USE + 15;

    static{
        assignItemRect(ANKH,            3,0,10, 16);
        assignItemRect(STYLUS,          2,2,12, 13);
        assignItemRect(WEIGHT,          1,2,14, 12);
        assignItemRect(SEAL,            3,1,9,  15);
        assignItemRect(TORCH,           2,0,12, 15);
        assignItemRect(BEACON,          0,0,16, 15);
        assignItemRect(BOMB,            3,1,10, 13);
        assignItemRect(HONEYPOT,        1,2,14, 12);
        assignItemRect(SHATTPOT,        1,2,14, 12);
        assignItemRect(IRON_KEY,        4,1,8,  14);
        assignItemRect(GOLDEN_KEY,      4,1,8,  14);
        assignItemRect(CRYSTAL_KEY,     4,1,8,  14);
        assignItemRect(SKELETON_KEY,    4,1,8,  14);
        assignItemRect(MASTERY,         2,0,13, 16);
    }

    private static final int Teller_ITEM = xy(1, 4);   //1 slots ,my new things
    public static final int SEWING_KIT = Teller_ITEM + 0;
    public static final int OVERPRICED_SEWING_KIT = Teller_ITEM + 1;
    public static final int HOLYBOMB = Teller_ITEM+2;
    static {
        assignItemRect(SEWING_KIT,           2,2,12,12);
        assignItemRect(OVERPRICED_SEWING_KIT,2,3,12,10);
        assignItemRect(HOLYBOMB,3,1,10,13);
    }

    private static final int MISSILE_WEP = xy(1, 5);  //16 slots
    //8 free slots
    public static final int DART = MISSILE_WEP + 0;
    public static final int BOOMERANG = MISSILE_WEP + 1;
    public static final int INCENDIARY_DART = MISSILE_WEP + 2;
    //public static final int SHURIKEN        = MISSILE_WEP+3;
    public static final int CURARE_DART = MISSILE_WEP + 3;
    public static final int JAVELIN = MISSILE_WEP + 4;

    private static final int WEP_MAINHAND = xy(1, 6);   //8 slots
    public static final int WORN_SHORTSWORD     =   WEP_MAINHAND + 0;
    public static final int SHORTSWORD          =   WEP_MAINHAND + 1;
    public static final int HAND_AXE            =   WEP_MAINHAND + 2;
    public static final int MAGES_STAFF         =   WEP_MAINHAND + 3;
    public static final int DIRK                =   WEP_MAINHAND + 4;
    public static final int MACE                =   WEP_MAINHAND + 5;
    public static final int SCIMITAR            =   WEP_MAINHAND + 6;
    public static final int WHIP                =   WEP_MAINHAND + 7;
    public static final int LONGSWORD           =   WEP_MAINHAND + 8;
    public static final int BATTLE_AXE          =   WEP_MAINHAND + 9;
    public static final int FLAIL               =   WEP_MAINHAND + 10;
    public static final int RUNIC_BLADE         =   WEP_MAINHAND + 11;
    public static final int ASSASSINS_BLADE     = WEP_MAINHAND + 12;

    static{
        assignItemRect(WORN_SHORTSWORD, 1,2,13, 13);
        assignItemRect(SHORTSWORD,      1,2,13, 13);
        assignItemRect(HAND_AXE,        2,1,12, 14);
        assignItemRect(MAGES_STAFF,     0,2,15, 14);
        assignItemRect(DIRK,            1,2,13, 14);
    }

    private static final int WEP_OFFHAND = xy(1, 7);   //8 slots
    //public static final int CUDGEL          = WEP_TIER1+1;
    //public static final int RAPIER          = WEP_TIER1+3;
    public static final int WAKIZASHI           = WEP_OFFHAND + 0;
    public static final int RAPIER              = WEP_OFFHAND + 1;
    //public static final int SPIKED_CLUB       = WEP_OFFHAND+2;
    public static final int ROUND_SHIELD        = WEP_OFFHAND + 3;
    public static final int JAVELINBARREL       = WEP_OFFHAND + 4;
    public static final int HAMMERWITHTHORNS    = WEP_OFFHAND + 5;

    public static final int  SAWTOOTHFRISBEE    = WEP_OFFHAND + 7;

    private static final int WEP_TWOHANDED = xy(1, 8);   //8 slots
    public static final int SPEAR = WEP_TWOHANDED + 0;
    public static final int QUARTERSTAFF = WEP_TWOHANDED + 1;
    public static final int GREATSWORD = WEP_TWOHANDED + 2;
    public static final int WAR_HAMMER = WEP_TWOHANDED + 3;
    public static final int GLAIVE = WEP_TWOHANDED + 4;
    public static final int GREATAXE = WEP_TWOHANDED + 5;
    public static final int GREATSHIELD = WEP_TWOHANDED + 6;
    public static final int NUNCHAKU = WEP_TWOHANDED + 7;

    private static final int WEP_DUALWIELD = xy(1, 9);   //8 slots
    public static final int KNUCKLEDUSTER       = WEP_DUALWIELD + 0;
    public static final int DAGGER              = WEP_DUALWIELD + 1;
    public static final int SAI                 = WEP_DUALWIELD + 2;
    public static final int SWORD               = WEP_DUALWIELD + 3;
    public static final int TOMAHAWK            = WEP_DUALWIELD + 4;
    public static final int TONFA               = WEP_DUALWIELD + 5;

    static {
        assignItemRect(KNUCKLEDUSTER,       1,3,15,10);
        assignItemRect(DAGGER,              1,2,12,13);
        assignItemRect(TOMAHAWK,            2,2,13,13);
    }

    private static final int WEP_ATTACHED = xy(1, 10);
    public static final int HIDDENBLADE = WEP_ATTACHED + 0;
    public static final int NINJAPROSTHESIS = WEP_ATTACHED + 1;
    public static final int SHURIKEN = WEP_ATTACHED + 2;
    public static final int KUNAI = WEP_ATTACHED + 3;
    public static final int GAUNTLET = WEP_ATTACHED + 4;
    static {
        assignItemRect(HIDDENBLADE,         1,2,13,13);
        assignItemRect(GAUNTLET,            1,1,15,15);
    }


    private static final int WEP_GUN = xy(1,11);
    public static final int BULLET = WEP_GUN+0;
    public static final int DOUBLESHOOT = WEP_GUN+1;
    public static final int FLINTLOCK= WEP_GUN + 2;
    public static final int SUBMACHINEGUN= WEP_GUN + 3;
    public static final int HANDCANNON=WEP_GUN+4;

    static {
        assignItemRect(FLINTLOCK,         3,1,11,14);
        assignItemRect(SUBMACHINEGUN,     1,0,13,16);
    }

    private static final int WEP_MAGICBOOK = xy(1,12);
    public static final int OLDBOOK = WEP_MAGICBOOK+0;
    public static final int BOOKOFFLAME = WEP_MAGICBOOK+1;
    public static final int BOOKOFLIGHT = WEP_MAGICBOOK+4;

    static {
        for (int i = WEP_MAGICBOOK; i < WEP_MAGICBOOK+8; i++)
            assignItemRect(i, 2,1,12, 15);
    }

    private static final int ARMOR = xy(1, 13);  //16 slots
    public static final int ARMOR_CLOTH = ARMOR + 0;
    public static final int ARMOR_LEATHER = ARMOR + 1;
    public static final int ARMOR_MAIL = ARMOR + 2;
    public static final int ARMOR_SCALE = ARMOR + 3;
    public static final int ARMOR_PLATE = ARMOR + 4;
    public static final int ARMOR_WARRIOR = ARMOR + 5;
    public static final int ARMOR_MAGE = ARMOR + 6;
    public static final int ARMOR_ROGUE = ARMOR + 7;
    public static final int ARMOR_HUNTRESS = ARMOR + 8;
    static{
        assignItemRect(ARMOR_CLOTH,     1,2,15, 12);
        assignItemRect(ARMOR_LEATHER,   1,2,14, 13);
        assignItemRect(ARMOR_MAIL,      1,2,14, 12);
        assignItemRect(ARMOR_SCALE,     1,2,14, 11);
        assignItemRect(ARMOR_PLATE,     2,2,12, 12);
        assignItemRect(ARMOR_WARRIOR,   2,2,12, 12);
        assignItemRect(ARMOR_MAGE,      0,0,15, 15);
        assignItemRect(ARMOR_ROGUE,     1,2,14, 12);
        assignItemRect(ARMOR_HUNTRESS,  1,0,13, 15);
    }

    private static final int WANDS = xy(1, 14);  //16 slots
    public static final int WAND_MAGIC_MISSILE = WANDS + 0;
    public static final int WAND_FIREBOLT = WANDS + 1;
    public static final int WAND_FROST = WANDS + 2;
    public static final int WAND_LIGHTNING = WANDS + 3;
    public static final int WAND_DISINTEGRATION = WANDS + 4;
    public static final int WAND_PRISMATIC_LIGHT = WANDS + 5;
    public static final int WAND_VENOM = WANDS + 6;
    public static final int WAND_LIVING_EARTH = WANDS + 7;
    public static final int WAND_BLAST_WAVE = WANDS + 8;
    public static final int WAND_CORRUPTION = WANDS + 9;
    public static final int WAND_WARDING = WANDS + 10;
    public static final int WAND_REGROWTH = WANDS + 11;
    public static final int WAND_TRANSFUSION = WANDS + 12;

    static {
        for (int i = WANDS; i < WANDS+16; i++)
            assignItemRect(i, 1,1,14, 14);
    }

    private static final int RINGS = xy(1, 15);  //16 slots
    public static final int RING_GARNET = RINGS + 0;
    public static final int RING_RUBY = RINGS + 1;
    public static final int RING_TOPAZ = RINGS + 2;
    public static final int RING_EMERALD = RINGS + 3;
    public static final int RING_ONYX = RINGS + 4;
    public static final int RING_OPAL = RINGS + 5;
    public static final int RING_TOURMALINE = RINGS + 6;
    public static final int RING_SAPPHIRE = RINGS + 7;
    public static final int RING_AMETHYST = RINGS + 8;
    public static final int RING_QUARTZ = RINGS + 9;
    public static final int RING_AGATE = RINGS + 10;
    public static final int RING_DIAMOND = RINGS + 11;

    static {
        for (int i = RINGS; i < RINGS+16; i++)
            assignItemRect(i, 4,2,8, 10);
    }

    private static final int ARTIFACTS = xy(1, 16);  //32 slots
    public static final int ARTIFACT_CLOAK = ARTIFACTS + 0;
    public static final int ARTIFACT_ARMBAND = ARTIFACTS + 1;
    public static final int ARTIFACT_CAPE = ARTIFACTS + 2;
    public static final int ARTIFACT_TALISMAN = ARTIFACTS + 3;
    public static final int ARTIFACT_HOURGLASS = ARTIFACTS + 4;
    public static final int ARTIFACT_TOOLKIT = ARTIFACTS + 5;
    public static final int ARTIFACT_SPELLBOOK = ARTIFACTS + 6;
    public static final int ARTIFACT_BEACON = ARTIFACTS + 7;
    public static final int ARTIFACT_CHAINS = ARTIFACTS + 8;
    public static final int ARTIFACT_HORN1 = ARTIFACTS + 9;
    public static final int ARTIFACT_HORN2 = ARTIFACTS + 10;
    public static final int ARTIFACT_HORN3 = ARTIFACTS + 11;
    public static final int ARTIFACT_HORN4 = ARTIFACTS + 12;
    public static final int ARTIFACT_CHALICE1 = ARTIFACTS + 13;
    public static final int ARTIFACT_CHALICE2 = ARTIFACTS + 14;
    public static final int ARTIFACT_CHALICE3 = ARTIFACTS + 15;
    public static final int ARTIFACT_SANDALS = ARTIFACTS + 16;
    public static final int ARTIFACT_SHOES = ARTIFACTS + 17;
    public static final int ARTIFACT_BOOTS = ARTIFACTS + 18;
    public static final int ARTIFACT_GREAVES = ARTIFACTS + 19;
    public static final int ARTIFACT_ROSE1 = ARTIFACTS + 20;
    public static final int ARTIFACT_ROSE2 = ARTIFACTS + 21;
    public static final int ARTIFACT_ROSE3 = ARTIFACTS + 22;

    static {
        assignItemRect(ARTIFACT_CLOAK,      3,0,9,  15);
        assignItemRect(ARTIFACT_ARMBAND,    0,2,16,  13);
        assignItemRect(ARTIFACT_SANDALS,    0,7,16, 5 );
        assignItemRect(ARTIFACT_SHOES,      0,7,16, 6 );
        assignItemRect(ARTIFACT_BOOTS,      0,5,16, 9);
        assignItemRect(ARTIFACT_GREAVES,    0,1,16, 14);
        assignItemRect(ARTIFACT_ROSE1,      2,2,14, 14);
        assignItemRect(ARTIFACT_ROSE2,      2,2,14, 14);
        assignItemRect(ARTIFACT_ROSE3,      2,2,14, 14);
    }


    private static final int SCROLLS = xy(1, 20);  //16 slots

    //32 free slots
    public static final int SCROLL_KAUNAN = SCROLLS + 0;
    public static final int SCROLL_SOWILO = SCROLLS + 1;
    public static final int SCROLL_LAGUZ = SCROLLS + 2;
    public static final int SCROLL_YNGVI = SCROLLS + 3;
    public static final int SCROLL_GYFU = SCROLLS + 4;
    public static final int SCROLL_RAIDO = SCROLLS + 5;
    public static final int SCROLL_ISAZ = SCROLLS + 6;
    public static final int SCROLL_MANNAZ = SCROLLS + 7;
    public static final int SCROLL_NAUDIZ = SCROLLS + 8;
    public static final int SCROLL_BERKANAN = SCROLLS + 9;
    public static final int SCROLL_ODAL = SCROLLS + 10;
    public static final int SCROLL_TIWAZ = SCROLLS + 11;

    static {
        for (int i = SCROLLS; i < SCROLLS+16; i++)
            assignItemRect(i, 0,1,15, 14);
    }

    private static final int POTIONS = xy(1, 21);  //16 slots
    public static final int POTION_CRIMSON = POTIONS + 0;
    public static final int POTION_AMBER = POTIONS + 1;
    public static final int POTION_GOLDEN = POTIONS + 2;
    public static final int POTION_JADE = POTIONS + 3;
    public static final int POTION_TURQUOISE = POTIONS + 4;
    public static final int POTION_AZURE = POTIONS + 5;
    public static final int POTION_INDIGO = POTIONS + 6;
    public static final int POTION_MAGENTA = POTIONS + 7;
    public static final int POTION_BISTRE = POTIONS + 8;
    public static final int POTION_CHARCOAL = POTIONS + 9;
    public static final int POTION_SILVER = POTIONS + 10;
    public static final int POTION_IVORY = POTIONS + 11;

    static {
        for (int i = POTIONS; i < POTIONS+16; i++)
            assignItemRect(i, 3,1,10, 14);
    }

    private static final int SEEDS = xy(1, 22);  //16 slots
    public static final int SEED_ROTBERRY = SEEDS + 0;
    public static final int SEED_FIREBLOOM = SEEDS + 1;
    public static final int SEED_STARFLOWER = SEEDS + 2;
    public static final int SEED_BLINDWEED = SEEDS + 3;
    public static final int SEED_SUNGRASS = SEEDS + 4;
    public static final int SEED_ICECAP = SEEDS + 5;
    public static final int SEED_STORMVINE = SEEDS + 6;
    public static final int SEED_SORROWMOSS = SEEDS + 7;
    public static final int SEED_DREAMFOIL = SEEDS + 8;
    public static final int SEED_EARTHROOT = SEEDS + 9;
    public static final int SEED_FADELEAF = SEEDS + 10;
    public static final int SEED_BLANDFRUIT = SEEDS + 11;

    static{
        for (int i = SEEDS; i < SEEDS+16; i++)
            assignItemRect(i, 3,3,10, 10);
    }

    private static final int PAGES = xy(1, 23);  //16 slots
    public static final int PAGE_MAGICMISSILE = PAGES + 0;
    public static final int PAGE_LIGHTUP = PAGES + 1;
    public static final int PAGE_FLASH = PAGES + 2;
    public static final int PAGE_HEALING = PAGES + 3;
    public static final int PAGE_HOLYBOMB = PAGES + 4;

    static {
        for (int i = PAGES; i < PAGES+16; i++)
            assignItemRect(i, 0,1,15, 14);
    }

    private static final int FOOD = xy(1, 25);  //16 slots

    //32 free slots
    public static final int MEAT = FOOD + 0;
    public static final int STEAK = FOOD + 1;
    public static final int OVERPRICED = FOOD + 2;
    public static final int CARPACCIO = FOOD + 3;
    public static final int BLANDFRUIT = FOOD + 4;
    public static final int RATION = FOOD + 5;
    public static final int PASTY = FOOD + 6;
    public static final int PUMPKIN_PIE = FOOD + 7;
    public static final int CANDY_CANE = FOOD + 8;
    public static final int HAW_FLAKES = FOOD + 9;

    static{
        assignItemRect(MEAT,        0,2,15, 11);
        assignItemRect(STEAK,       0,2,15, 11);
        assignItemRect(OVERPRICED,  1,2,14, 11);
        assignItemRect(CARPACCIO,   0,2,15, 11);
        assignItemRect(BLANDFRUIT,  3,2,9,  12);
        assignItemRect(RATION,      0,2,16, 12);
        assignItemRect(PASTY,       0,3,16, 11);
        assignItemRect(PUMPKIN_PIE, 0,2,16, 12);
        assignItemRect(CANDY_CANE,  2,0,13, 16);
        assignItemRect(HAW_FLAKES,  1,3,15, 12);
    }

    private static final int QUEST = xy(1, 26);  //32 slots
    public static final int SKULL = QUEST + 0;
    public static final int DUST = QUEST + 1;
    public static final int CANDLE = QUEST + 2;
    public static final int EMBER = QUEST + 3;
    public static final int PICKAXE = QUEST + 4;
    public static final int ORE = QUEST + 5;
    public static final int TOKEN = QUEST + 6;

    static{
        assignItemRect(SKULL,   0,2,16, 11);
        assignItemRect(DUST,    2,3,12, 11);
        assignItemRect(CANDLE,  2,3,12, 12);
        assignItemRect(EMBER,   2,3,12, 11);
        assignItemRect(PICKAXE, 1,1,14, 14);
        assignItemRect(ORE,     1,1,15, 15);
        assignItemRect(TOKEN,   2,2,12, 12);

    }

    private static final int BAGS = xy(1, 28);  //16 slots
    public static final int VIAL = BAGS + 0;
    public static final int POUCH = BAGS + 1;
    public static final int HOLDER = BAGS + 2;
    public static final int BANDOLIER = BAGS + 3;
    public static final int HOLSTER = BAGS + 4;

    static{
        assignItemRect(VIAL,        2,2,12, 12);
        assignItemRect(POUCH,       1,0,14, 15);
    }


    //private static final int SPELLBOOK = xy(1,29);
    public  static final int EMPTY     = Teller_ITEM+2;

    private static final int FIRESPELL = xy(1,29);

    public static final int IGNITE = FIRESPELL+0;
    public static final int FIREBALL = FIRESPELL+1;
    public static final int BURNINGBLADE = FIRESPELL+2;
    public static final int FIREBLAST = FIRESPELL+3;
    public static final int SUMMONFIREELEMENTAL= FIRESPELL+4;


    private static final int ICESPELL = xy(9,29);

    public static final int COLDAIR     =   ICESPELL+0;
    public static final int DRIFTICE    =   ICESPELL+1;
    public static final int ICEIMBUE    =   ICESPELL+2;
    public static final int ICELANCE    =   ICESPELL+3;
    public static final int FROSTCRYSTAL=   ICESPELL+4;

    private static final int HOLYSPELL = xy(1,30);

    public static final int LIGHTUP    =    HOLYSPELL+0;
    public static final int HOLYHEAL   =    HOLYSPELL+1;
    public static final int FLASHLIGHT =    HOLYSPELL+2;
    //public static final int HOLYBOMB   =    HOLYSPELL+3;
    public static final int DIVINEGRACE=    HOLYSPELL+4;

    private static final int SHADOWSPELL = xy(9,30);
    public static final int DISAPPER   =    SHADOWSPELL+0;
    public static final int SHADOWARROW=    SHADOWSPELL+1;
    public static final int CORRUPTBREATH=  SHADOWSPELL+2;
    public static final int MENTALCONTROL=  SHADOWSPELL+3;
    public static final int SOULEXPLOSION=  SHADOWSPELL+4;

    private static final int LIGHTNINGSPELL = xy(1,31);
    public static final int SHOCK         = LIGHTNINGSPELL+0;
    public static final int CHAINLIGHTNING= LIGHTNINGSPELL+1;
    public static final int CHARGE        = LIGHTNINGSPELL+2;
    public static final int THUNDERSTORM  = LIGHTNINGSPELL+3;
    public static final int FURYOFTHUNDER = LIGHTNINGSPELL+4;

    private static final int NATURESPELL  = xy(9,31);
    public static final int RECOVERY      = NATURESPELL+0;
    public static final int TWINE         = NATURESPELL+1;
    public static final int TAILWIND      = NATURESPELL+2;
    public static final int STONEARMOR    = NATURESPELL+3;
    public static final int SUMMONROCK    = NATURESPELL+4;

    private static final int ARCANESPELL    = xy(1,32);
    public static final int POWERHUNGRY     = ARCANESPELL+0;
    public static final int MAGICALMISSILE  = ARCANESPELL+1;
    public static final int BLASTWAVE       = ARCANESPELL+2;
    public static final int ARCANESHELL     = ARCANESPELL+3;
    public static final int DISINTEGRATEBEAM= ARCANESPELL+4;

    private static int xy(int x, int y) {
        x -= 1;
        y -= 1;
        return x + WIDTH * y;
    }

    //64 free slots
    private static void assignItemRect( int item, int x1,int y1,int width, int height){
        int x = (item % WIDTH) * WIDTH;
        int y = (item / WIDTH) * WIDTH;
        x+=x1;
        y+=y1;
        film.add( item, x, y, x+width, y+height);
    }
}
