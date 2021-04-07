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

import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.journal.Notes;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Amok;
import com.teller.pixeldungeonofteller.actors.buffs.Awareness;
import com.teller.pixeldungeonofteller.actors.buffs.Light;
import com.teller.pixeldungeonofteller.actors.buffs.MindVision;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.hero.HeroClass;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Blacksmith;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Ghost;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Imp;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Wandmaker;
import com.teller.pixeldungeonofteller.items.Ankh;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.rings.Ring;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.levels.CavesBossLevel;
import com.teller.pixeldungeonofteller.levels.CavesLevel;
import com.teller.pixeldungeonofteller.levels.CityBossLevel;
import com.teller.pixeldungeonofteller.levels.CityLevel;
import com.teller.pixeldungeonofteller.levels.DeadEndLevel;
import com.teller.pixeldungeonofteller.levels.HallsBossLevel;
import com.teller.pixeldungeonofteller.levels.HallsLevel;
import com.teller.pixeldungeonofteller.levels.LastLevel;
import com.teller.pixeldungeonofteller.levels.LastShopLevel;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.PrisonBossLevel;
import com.teller.pixeldungeonofteller.levels.PrisonLevel;
import com.teller.pixeldungeonofteller.levels.SewerBossLevel;
import com.teller.pixeldungeonofteller.levels.SewerLevel;
import com.teller.pixeldungeonofteller.levels.rooms.secret.SecretRoom;
import com.teller.pixeldungeonofteller.levels.rooms.special.SpecialRoom;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.scenes.StartScene;
import com.teller.pixeldungeonofteller.ui.QuickSlotButton;
import com.teller.pixeldungeonofteller.utils.BArray;
import com.teller.pixeldungeonofteller.utils.DungeonSeed;
import com.teller.pixeldungeonofteller.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;

public class Dungeon {

    private static final String RG_GAME_FILE = "game.dat";
    private static final String RG_DEPTH_FILE = "depth%d.dat";
    private static final String WR_GAME_FILE = "warrior.dat";
    private static final String WR_DEPTH_FILE = "warrior%d.dat";
    private static final String MG_GAME_FILE = "mage.dat";
    private static final String MG_DEPTH_FILE = "mage%d.dat";
    private static final String RN_GAME_FILE = "ranger.dat";
    private static final String RN_DEPTH_FILE = "ranger%d.dat";
    private static final String VERSION = "version";
    private static final String SEED = "seed";
    private static final String CHALLENGES = "challenges";
    private static final String HERO = "hero";
    private static final String GOLD = "gold";
    private static final String DEPTH = "depth";
    private static final String DROPPED = "dropped%d";
    private static final String LEVEL = "level";
    private static final String LIMDROPS = "limiteddrops";
    private static final String DV = "dewVial";
    private static final String WT = "transmutation";
    private static final String CHAPTERS = "chapters";
    private static final String QUESTS = "quests";
    private static final String BADGES = "badges";
    public static int challenges;
    public static Hero hero;
    public static Level level;
    public static QuickSlot quickslot = new QuickSlot();
    public static int depth;
    public static int gold;
    public static HashSet<Integer> chapters;
    // Hero's field of view
    public static boolean[] visible;
    public static SparseArray<ArrayList<Item>> droppedItems;
    public static int version;
    public static long seed;
    //we store this to avoid having to re-allocate the array with each pathfind
    private static boolean[] passable;

    public static void init() {

        version = Game.versionCode;
        challenges = PixelDungeonOfTeller.challenges();

        seed = DungeonSeed.randomSeed();

        Actor.clear();
        Actor.resetNextID();

        Random.seed(seed);

        Scroll.initLabels();
        Potion.initColors();
        Ring.initGems();
        SpecialRoom.initForRun();
        SecretRoom.initForRun();
        //transmutation = Random.IntRange(6, 14);

        Random.seed();

        Statistics.reset();
        Notes.reset();

        quickslot.reset();
        QuickSlotButton.reset();

        depth = 0;
        gold = 0;

        droppedItems = new SparseArray<ArrayList<Item>>();

        for (limitedDrops a : limitedDrops.values())
            a.count = 0;

        chapters = new HashSet<Integer>();

        Ghost.Quest.reset();
        Wandmaker.Quest.reset();
        Blacksmith.Quest.reset();
        Imp.Quest.reset();

        Generator.initArtifacts();
        hero = new Hero();
        hero.live();

        GamesInProgress.selectedClass.initHero( hero );
    }

    public static boolean isChallenged(int mask) {
        return (challenges & mask) != 0;
    }

    public static Level newLevel() {

        Dungeon.level = null;
        Actor.clear();

        depth++;
        if (depth > Statistics.deepestFloor) {
            Statistics.deepestFloor = depth;

            if (Statistics.qualifiedForNoKilling) {
                Statistics.completedWithNoKilling = true;
            } else {
                Statistics.completedWithNoKilling = false;
            }
        }

        Level level;
        switch (depth) {
            case 1:
            case 2:
            case 3:
            case 4:
                //level = new PrisonLevel();
                level = new SewerLevel();
                break;
            case 5:
                level = new SewerBossLevel();
                //level = new PrisonBossLevel();
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                level = new PrisonLevel();
                break;
            case 10:
                level = new PrisonBossLevel();
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                level = new CavesLevel();
                break;
            case 15:
                level = new CavesBossLevel();
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                level = new CityLevel();
                break;
            case 20:
                level = new CityBossLevel();
                break;
            case 21:
                level = new LastShopLevel();
                break;
            case 22:
            case 23:
            case 24:
                level = new HallsLevel();
                break;
            case 25:
                level = new HallsBossLevel();
                break;
            case 26:
                level = new LastLevel();
                break;
            default:
                level = new DeadEndLevel();
                Statistics.deepestFloor--;
        }

        level.create();

        Statistics.qualifiedForNoKilling = !bossLevel();

        return level;
    }

    public static void resetLevel() {

        Actor.clear();

        level.reset();
        switchLevel(level, level.entrance);
    }

    public static long seedCurDepth() {
        return seedForDepth(depth);
    }

    public static long seedForDepth(int depth) {
        Random.seed(seed);
        for (int i = 0; i < depth; i++)
            Random.Long(); //we don't care about these values, just need to go through them
        long result = Random.Long();
        Random.seed();
        return result;
    }

    public static boolean shopOnLevel() {
        return depth == 6 || depth == 11 || depth == 16;
    }

    public static boolean bossLevel() {
        return bossLevel(depth);
    }

    public static boolean bossLevel(int depth) {
        return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25;
    }

    @SuppressWarnings("deprecation")
    public static void switchLevel(final Level level, int pos) {

        Dungeon.level = level;
        Actor.init();

        PathFinder.setMapSize(level.width(), level.height());
        visible = new boolean[level.length()];

        Actor respawner = level.respawner();
        if (respawner != null) {
            Actor.add(level.respawner());
        }

        hero.pos = pos != -1 ? pos : level.exit;
        hero.laststep=hero.pos;

        Light light = hero.buff(Light.class);
        hero.viewDistance = light == null ? level.viewDistance : Math.max(Light.DISTANCE, level.viewDistance);

        hero.curAction = hero.lastAction = null;

        observe();
        try {
            saveAll();
        } catch (IOException e) {
            PixelDungeonOfTeller.reportException(e);
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
        }
    }

    public static void dropToChasm(Item item) {
        int depth = Dungeon.depth + 1;
        ArrayList<Item> dropped = (ArrayList<Item>)Dungeon.droppedItems.get(depth);
        if (dropped == null) {
            Dungeon.droppedItems.put(depth, dropped = new ArrayList<Item>());
        }
        dropped.add(item);
    }

    public static boolean posNeeded() {
        //2 POS each floor set
        int posLeftThisSet = 2 - (limitedDrops.strengthPotions.count - (depth / 5) * 2);
        if (posLeftThisSet <= 0) return false;

        int floorThisSet = (depth % 5);

        //pos drops every two floors, (numbers 1-2, and 3-4) with a 50% chance for the earlier one each time.
        int targetPOSLeft = 2 - floorThisSet / 2;
        if (floorThisSet % 2 == 1 && Random.Int(2) == 0) targetPOSLeft--;

        if (targetPOSLeft < posLeftThisSet) return true;
        else return false;

    }

    public static boolean souNeeded() {
        //3 SOU each floor set
        int souLeftThisSet = 3 - (limitedDrops.upgradeScrolls.count - (depth / 5) * 3);
        if (souLeftThisSet <= 0) return false;

        int floorThisSet = (depth % 5);
        //chance is floors left / scrolls left
        return Random.Int(5 - floorThisSet) < souLeftThisSet;
    }

    public static boolean asNeeded() {
        //1 AS each floor set
        int asLeftThisSet = 1 - (limitedDrops.arcaneStyli.count - (depth / 5));
        if (asLeftThisSet <= 0) return false;

        int floorThisSet = (depth % 5);
        //chance is floors left / scrolls left
        return Random.Int(5 - floorThisSet) < asLeftThisSet;
    }

    public static String gameFile(HeroClass cl) {
        switch (cl) {
            case WARRIOR:
                return WR_GAME_FILE;
            case MAGE:
                return MG_GAME_FILE;
            case HUNTRESS:
                return RN_GAME_FILE;
            default:
                return RG_GAME_FILE;
        }
    }

    private static String depthFile(HeroClass cl) {
        switch (cl) {
            case WARRIOR:
                return WR_DEPTH_FILE;
            case MAGE:
                return MG_DEPTH_FILE;
            case HUNTRESS:
                return RN_DEPTH_FILE;
            default:
                return RG_DEPTH_FILE;
        }
    }

    public static void saveGame( int save) throws IOException {
        try {
            Bundle bundle = new Bundle();

            version = Game.versionCode;
            bundle.put(VERSION, version);
            bundle.put(SEED, seed);
            bundle.put(CHALLENGES, challenges);
            bundle.put(HERO, hero);
            bundle.put(GOLD, gold);
            bundle.put(DEPTH, depth);

            for (int d : droppedItems.keyArray()) {
                bundle.put(Messages.format(DROPPED, d), droppedItems.get(d));
            }

            quickslot.storePlaceholders(bundle);

            int[] dropValues = new int[limitedDrops.values().length];
            for (limitedDrops value : limitedDrops.values())
                dropValues[value.ordinal()] = value.count;
            bundle.put(LIMDROPS, dropValues);

            int count = 0;
            int[] ids = new int[chapters.size()];
            for (Integer id : chapters) {
                ids[count++] = id;
            }
            bundle.put(CHAPTERS, ids);

            Bundle quests = new Bundle();
            Ghost.Quest.storeInBundle(quests);
            Wandmaker.Quest.storeInBundle(quests);
            Blacksmith.Quest.storeInBundle(quests);
            Imp.Quest.storeInBundle(quests);
            bundle.put(QUESTS, quests);

            SpecialRoom.storeRoomsInBundle( bundle );
            SecretRoom.storeRoomsInBundle( bundle );

            Statistics.storeInBundle(bundle);
            Notes.storeInBundle( bundle );
            Generator.storeInBundle(bundle);

            Scroll.save(bundle);
            Potion.save(bundle);
            Ring.save(bundle);

            Actor.storeNextID(bundle);

            Bundle badges = new Bundle();
            Badges.saveLocal(badges);
            bundle.put(BADGES, badges);

            FileUtils.bundleToFile( GamesInProgress.gameFile(save), bundle);

        } catch (IOException e) {
            GamesInProgress.setUnknown( save );
            PixelDungeonOfTeller.reportException(e);
        }
    }

    public static void saveLevel(int save) throws IOException {
        Bundle bundle = new Bundle();
        bundle.put(LEVEL, level);

        FileUtils.bundleToFile(GamesInProgress.depthFile( save, depth), bundle);
    }

    public static void saveAll() throws IOException {
        if (hero.isAlive()) {

            Actor.fixTime();
            saveGame( GamesInProgress.curSlot );
            saveLevel( GamesInProgress.curSlot );

            GamesInProgress.set( GamesInProgress.curSlot, depth, challenges, hero );

        } else if (WndResurrect.instance != null) {

            WndResurrect.instance.hide();
            Hero.reallyDie(WndResurrect.causeOfDeath);

        }
    }

    public static void loadGame( int save ) throws IOException {
        loadGame( save, true );
    }

    public static void loadGame(int save, boolean fullLoad) throws IOException {

        Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.gameFile( save ) );

        version = bundle.getInt(VERSION);

        seed = bundle.contains(SEED) ? bundle.getLong(SEED) : DungeonSeed.randomSeed();

        Actor.restoreNextID(bundle);

        quickslot.reset();
        QuickSlotButton.reset();

        Dungeon.challenges = bundle.getInt(CHALLENGES);

        Dungeon.level = null;
        Dungeon.depth = -1;

        Scroll.restore(bundle);
        Potion.restore(bundle);
        Ring.restore(bundle);

        quickslot.restorePlaceholders(bundle);

        if (fullLoad) {
            int[] dropValues = bundle.getIntArray(LIMDROPS);
            for (limitedDrops value : limitedDrops.values())
                value.count = value.ordinal() < dropValues.length ?
                        dropValues[value.ordinal()] : 0;

            chapters = new HashSet<Integer>();
            int[] ids = bundle.getIntArray(CHAPTERS);
            if (ids != null) {
                for (int id : ids) {
                    chapters.add(id);
                }
            }

            Bundle quests = bundle.getBundle(QUESTS);
            if (!quests.isNull()) {
                Ghost.Quest.restoreFromBundle(quests);
                Wandmaker.Quest.restoreFromBundle(quests);
                Blacksmith.Quest.restoreFromBundle(quests);
                Imp.Quest.restoreFromBundle(quests);
            } else {
                Ghost.Quest.reset();
                Wandmaker.Quest.reset();
                Blacksmith.Quest.reset();
                Imp.Quest.reset();
            }

            SpecialRoom.restoreRoomsFromBundle(bundle);
            SecretRoom.restoreRoomsFromBundle(bundle);
        }

        Bundle badges = bundle.getBundle(BADGES);
        if (!badges.isNull()) {
            Badges.loadLocal(badges);
        } else {
            Badges.reset();
        }
        Notes.restoreFromBundle( bundle );

        hero = null;
        hero = (Hero) bundle.get(HERO);

        gold = bundle.getInt(GOLD);
        depth = bundle.getInt(DEPTH);

        Statistics.restoreFromBundle(bundle);
        Generator.restoreFromBundle(bundle);

        droppedItems = new SparseArray<ArrayList<Item>>();
        for (int i = 2; i <= Statistics.deepestFloor + 1; i++) {
            ArrayList<Item> dropped = new ArrayList<Item>();
            if (bundle.contains(Messages.format(DROPPED, i)))
                for (Bundlable b : bundle.getCollection(Messages.format(DROPPED, i))) {
                    dropped.add((Item) b);
                }
            if (!dropped.isEmpty()) {
                droppedItems.put(i, dropped);
            }
        }
    }

    public static Level loadLevel(int save) throws IOException {

        Dungeon.level = null;
        Actor.clear();

        Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.depthFile( save, depth)) ;

        Level level = (Level)bundle.get( LEVEL );

        if (level == null){
            throw new IOException();
        } else {
            return level;
        }
    }

    public static void deleteGame(int save, boolean deleteLevels) {

        FileUtils.deleteFile(GamesInProgress.gameFile(save));

        if (deleteLevels) {
            FileUtils.deleteDir(GamesInProgress.gameFolder(save));
        }

        GamesInProgress.delete( save );
    }

    public static Bundle gameBundle(String fileName) throws IOException {

        InputStream input = Game.instance.openFileInput(fileName);
        Bundle bundle = Bundle.read(input);
        input.close();

        return bundle;
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle) {
        info.depth = bundle.getInt( DEPTH );
        info.version = bundle.getInt( VERSION );
        info.challenges = bundle.getInt( CHALLENGES );
        Hero.preview( info, bundle.getBundle( HERO ) );
        Statistics.preview( info, bundle );
    }

    public static void fail(Class cause) {
        if (hero.belongings.getItem(Ankh.class) == null) {
            Rankings.INSTANCE.submit(false, cause);
        }
    }

    public static void win(Class cause) {

        hero.belongings.identify();

        if (challenges != 0) {
            Badges.validateChampion();
        }

        Rankings.INSTANCE.submit(true, cause);
    }

    public static void observe() {
        observe(hero.viewDistance + 1);
    }

    public static void observe(int dist) {

        if (level == null) {
            return;
        }

        level.updateFieldOfView(hero, visible);

        int x = hero.pos % level.width();
        int y = hero.pos / level.width();

        //left, right, top, bottom
        int l = Math.max( 0, x - dist );
        int r = Math.min( x + dist, level.width() - 1 );
        int t = Math.max( 0, y - dist );
        int b = Math.min( y + dist, level.height() - 1 );

        int width = r - l + 1;
        int height = b - t + 1;

        int pos = l + t * level.width();

        for (int i = t; i <= b; i++) {
            BArray.or( level.visited, visible, pos, width, level.visited );
            pos+=level.width();
        }

        GameScene.updateFog(l, t, width, height);

        if (hero.buff(MindVision.class) != null || hero.buff(Awareness.class) != null) {
            for (Mob m : level.mobs.toArray(new Mob[0])){
                BArray.or( level.visited, visible, m.pos - 1 - level.width(), 3, level.visited );
                BArray.or( level.visited, visible, m.pos, 3, level.visited );
                BArray.or( level.visited, visible, m.pos - 1 + level.width(), 3, level.visited );
                //updates adjacent cells too
                GameScene.updateFog(m.pos, 2);
            }
        }
        GameScene.afterObserve();

    }

    private static void setupPassable() {
        if (passable == null || passable.length != Dungeon.level.length())
            passable = new boolean[Dungeon.level.length()];
        else
            BArray.setFalse(passable);
    }

    public static PathFinder.Path findPath(Char ch, int from, int to, boolean[] pass, boolean[] visible) {

        setupPassable();
        if (ch.flying || ch.buff(Amok.class) != null) {
            BArray.or(pass, Dungeon.level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Dungeon.level.length());
        }

        for (Char c : Actor.chars()) {
            if (visible[c.pos]) {
                passable[c.pos] = false;
            }
        }

        return PathFinder.find(from, to, passable);

    }

    public static int findStep(Char ch, int from, int to, boolean[] pass, boolean[] visible) {

        if (level.adjacent(from, to)) {
            return Actor.findChar(to) == null && (pass[to] || Dungeon.level.avoid[to]) ? to : -1;
        }

        setupPassable();
        if (ch.flying || ch.buff(Amok.class) != null) {
            BArray.or(pass, Dungeon.level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Dungeon.level.length());
        }

        for (Char c : Actor.chars()) {
            if (visible[c.pos]) {
                passable[c.pos] = false;
            }
        }

        return PathFinder.getStep(from, to, passable);

    }

    public static int flee(Char ch, int cur, int from, boolean[] pass, boolean[] visible) {

        setupPassable();
        if (ch.flying) {
            BArray.or(pass,Dungeon.level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Dungeon.level.length());
        }

        for (Char c : Actor.chars()) {
            if (visible[c.pos]) {
                passable[c.pos] = false;
            }
        }
        passable[cur] = true;

        return PathFinder.getStepBack(cur, from, passable);

    }

    //enum of items which have limited spawns, records how many have spawned
    //could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
    //TODO: this is fairly brittle when it comes to bundling, should look into a more flexible solution.
    public enum limitedDrops {
        //limited world drops
        strengthPotions,
        upgradeScrolls,
        arcaneStyli,

        //all unlimited health potion sources (except guards, which are at the bottom.
        swarmHP,
        batHP,
        warlockHP,
        scorpioHP,
        cookingHP,
        //blandfruit, which can technically be an unlimited health potion source
        blandfruitSeed,

        //doesn't use Generator, so we have to enforce one armband drop here
        armband,

        //containers
        dewVial,
        seedBag,
        scrollBag,
        potionBag,
        wandBag,

        guardHP;

        public int count = 0;

        //for items which can only be dropped once, should directly access count otherwise.
        public boolean dropped() {
            return count != 0;
        }

        public void drop() {
            count = 1;
        }
    }

}
