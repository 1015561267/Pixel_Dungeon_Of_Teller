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
package com.teller.pixeldungeonofteller.levels;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Bones;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.mobs.Bestiary;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.artifacts.Artifact;
import com.teller.pixeldungeonofteller.items.journal.GuidePage;
import com.teller.pixeldungeonofteller.items.keys.GoldenKey;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.rings.RingOfWealth;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.journal.Document;
import com.teller.pixeldungeonofteller.levels.builders.Builder;
import com.teller.pixeldungeonofteller.levels.builders.LoopBuilder;
import com.teller.pixeldungeonofteller.levels.rooms.Room;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.levels.rooms.secret.SecretRoom;
import com.teller.pixeldungeonofteller.levels.rooms.special.PitRoom;
import com.teller.pixeldungeonofteller.levels.rooms.special.ShopRoom;
import com.teller.pixeldungeonofteller.levels.rooms.special.SpecialRoom;
import com.teller.pixeldungeonofteller.levels.rooms.standard.EntranceRoom;
import com.teller.pixeldungeonofteller.levels.rooms.standard.ExitRoom;
import com.teller.pixeldungeonofteller.levels.rooms.standard.StandardRoom;
import com.teller.pixeldungeonofteller.levels.traps.BlazingTrap;
import com.teller.pixeldungeonofteller.levels.traps.ChillingTrap;
import com.teller.pixeldungeonofteller.levels.traps.DisintegrationTrap;
import com.teller.pixeldungeonofteller.levels.traps.ExplosiveTrap;
import com.teller.pixeldungeonofteller.levels.traps.FireTrap;
import com.teller.pixeldungeonofteller.levels.traps.FrostTrap;
import com.teller.pixeldungeonofteller.levels.traps.Trap;
import com.teller.pixeldungeonofteller.levels.traps.WornTrap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class RegularLevel extends Level {

    protected ArrayList<Room> rooms;

    protected Builder builder;

    protected Room roomEntrance;
    protected Room roomExit;

    public int secretDoors;

    @Override
    protected boolean build() {

        builder = builder();

        ArrayList<Room> initRooms = initRooms();
        Random.shuffle(initRooms);

        do {
            for (Room r : initRooms){
                r.neigbours.clear();
                r.connected.clear();
            }
            rooms = builder.build((ArrayList<Room>)initRooms.clone());
        } while (rooms == null);

        if (painter().paint(this, rooms)){
            placeSign();
            return true;
        } else {
            return false;
        }
    }

    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();
        initRooms.add ( roomEntrance = new EntranceRoom());
        initRooms.add( roomExit = new ExitRoom());

        int standards = standardRooms();
        for (int i = 0; i < standards; i++) {
            StandardRoom s;
            do {
                s = StandardRoom.createRoom();
            } while (!s.setSizeCat( standards-i ));
            i += s.sizeCat.roomValue-1;
            initRooms.add(s);
        }

        if (Dungeon.shopOnLevel())
            initRooms.add(new ShopRoom());

        int specials = specialRooms();
        SpecialRoom.initForFloor();
        for (int i = 0; i < specials; i++)
            initRooms.add(SpecialRoom.createRoom());

        int secrets = SecretRoom.secretsForFloor(Dungeon.depth);
        for (int i = 0; i < secrets; i++)
            initRooms.add(SecretRoom.createRoom());

        return initRooms;
    }

    protected int standardRooms(){
        return 0;
    }

    protected int specialRooms(){
        return 0;
    }

    protected Builder builder(){
        return new LoopBuilder()
                .setLoopShape( 2 ,
                        Random.Float(0.55f, 0.85f),
                        Random.Float(0f, 0.5f));
    }

    protected abstract Painter painter();

    protected void placeSign(){
        while (true) {
            int pos = pointToCell(roomEntrance.random());
            if (pos != entrance && traps.get(pos) == null && findMob(pos) == null) {
                map[pos] = Terrain.SIGN;
                break;
            }
        }

        //teaches new players about secret doors
        if (Dungeon.depth == 2 && !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1)) {
            for (Room r : roomEntrance.connected.keySet()) {
                Room.Door d = roomEntrance.connected.get(r);
                if (d.type == Room.Door.Type.REGULAR)
                    map[d.x + d.y * width()] = Terrain.SECRET_DOOR;
            }
        }
    }

    protected float waterFill(){
        return 0;
    }

    protected int waterSmoothing(){
        return 0;
    }

    protected float grassFill(){
        return 0;
    }

    protected int grassSmoothing(){
        return 0;
    }

    protected int nTraps() {
        return Random.NormalIntRange( 1, 3+(Dungeon.depth/3) );
    }

    protected Class<?>[] trapClasses(){
        return new Class<?>[]{WornTrap.class};
    }

    protected float[] trapChances() {
        return new float[]{1};
    }

    private ArrayList<Class<?extends Mob>> mobsToSpawn = new ArrayList<>();

    @Override
    public int nMobs() {
        switch(Dungeon.depth) {
            case 1:
                //mobs are not randomly spawned on floor 1.
                return 0;
            default:
                return 2 + Dungeon.depth % 5 + Random.Int(5);
        }
    }

    @Override
    public Mob createMob() {
        if (mobsToSpawn == null || mobsToSpawn.isEmpty())
            mobsToSpawn = Bestiary.getMobRotation(Dungeon.depth);

        try {
            return mobsToSpawn.remove(0).newInstance();
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    @Override
    protected void createMobs() {
        //on floor 1, 10 rats are created so the player can get level 2.
        int mobsToSpawn = Dungeon.depth == 1 ? 10 : nMobs();

        ArrayList<Room> stdRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room instanceof StandardRoom && room != roomEntrance) {
                for (int i = 0; i < ((StandardRoom) room).sizeCat.roomValue; i++) {
                    stdRooms.add(room);
                }
                //pre-0.6.0 save compatibility
            } else if (room.legacyType.equals("STANDARD")){
                stdRooms.add(room);
            }
        }
        Random.shuffle(stdRooms);
        Iterator<Room> stdRoomIter = stdRooms.iterator();

        while (mobsToSpawn > 0) {
            if (!stdRoomIter.hasNext())
                stdRoomIter = stdRooms.iterator();
            Room roomToSpawn = stdRoomIter.next();

            Mob mob = Bestiary.mob( Dungeon.depth );
            mob.pos = pointToCell(roomToSpawn.random());

            if (findMob(mob.pos) == null && passable[mob.pos]) {
                mobsToSpawn--;
                mobs.add(mob);
                //TODO: perhaps externalize this logic into a method. Do I want to make mobs more likely to clump deeper down?
                if (mobsToSpawn > 0 && Random.Int(4) == 0){
                    mob = Bestiary.mob( Dungeon.depth );
                    mob.pos = pointToCell(roomToSpawn.random());

                    if (findMob(mob.pos)  == null && passable[mob.pos]) {
                        mobsToSpawn--;
                        mobs.add(mob);
                    }
                }
            }
        }

        for (Mob m : mobs){
            if (map[m.pos] == Terrain.HIGH_GRASS) {
                map[m.pos] = Terrain.GRASS;
                losBlocking[m.pos] = false;
            }

        }

    }

    @Override
    public int randomRespawnCell() {
        int count = 0;
        int cell = -1;

        while (true) {

            if (++count > 30) {
                return -1;
            }

            Room room = randomRoom( StandardRoom.class );
            if (room == null || room == roomEntrance) {
                continue;
            }

            cell = pointToCell(room.random());
            if (!Dungeon.visible[cell]
                    && Actor.findChar( cell ) == null
                    && passable[cell]
                    && cell != exit) {
                return cell;
            }

        }
    }

    @Override
    public int randomDestination() {

        int cell = -1;

        while (true) {

            Room room = Random.element( rooms );
            if (room == null) {
                continue;
            }

            cell = pointToCell(room.random());
            if (passable[cell]) {
                return cell;
            }

        }
    }

    @Override
    protected void createItems() {

        int nItems = 3 + Random.chances(new float[]{6, 3, 1});
        //int bonus = RingOfWealth.getBonus(Dungeon.hero, RingOfWealth.Wealth.class);

        //just incase someone gets a ridiculous ring, cap this at 80%
        //bonus = Math.min(bonus, 10);
        //while (Random.Float() < (0.3f + bonus*0.05f)) {
            nItems++;
        //}

        for (int i=0; i < nItems; i++) {
            Heap.Type type = null;
            switch (Random.Int( 20 )) {
                case 0:
                    type = Heap.Type.SKELETON;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    type = Heap.Type.CHEST;
                    break;
                case 5:
                    type = Dungeon.depth > 1 ? Heap.Type.MIMIC : Heap.Type.CHEST;
                    break;
                default:
                    type = Heap.Type.HEAP;
            }
            int cell = randomDropCell();
            if (map[cell] == Terrain.HIGH_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }

            Item toDrop = Generator.random();
            if ((toDrop instanceof Artifact && Random.Int(2) == 0) ||
                    (toDrop.isUpgradable() && Random.Int(4 - toDrop.level()) == 0)){
                Heap dropped = drop( toDrop, cell );
                if (heaps.get(cell) == dropped) {
                    dropped.type = Heap.Type.LOCKED_CHEST;
                    addItemToSpawn(new GoldenKey(Dungeon.depth));
                }
            } else {
                drop( toDrop, cell ).type = type;
            }
        }

        for (Item item : itemsToSpawn) {
            int cell = randomDropCell();
            drop( item, cell ).type = Heap.Type.HEAP;
            if (map[cell] == Terrain.HIGH_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
        }

        Item item = Bones.get();
        if (item != null) {
            int cell = randomDropCell();
            if (map[cell] == Terrain.HIGH_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
            drop( item, cell ).type = Heap.Type.REMAINS;
        }

        Collection<String> allPages = Document.ADVENTURERS_GUIDE.pages();
        ArrayList<String> missingPages = new ArrayList<>();
        for ( String page : allPages){
            if (!Document.ADVENTURERS_GUIDE.hasPage(page)){
                missingPages.add(page);
            }
        }

        //these are dropped specially
        missingPages.remove(Document.GUIDE_INTRO_PAGE);
        missingPages.remove(Document.GUIDE_SEARCH_PAGE);

        int foundPages = allPages.size() - (missingPages.size() + 2);

        //chance to find a page scales with pages missing and depth
        if (missingPages.size() > 0 && Random.Float() < (Dungeon.depth/(float)(foundPages + 1))){
            GuidePage p = new GuidePage();
            p.page(missingPages.get(0));
            int cell = randomDropCell();
            if (map[cell] == Terrain.HIGH_GRASS) {
                map[cell] = Terrain.GRASS;
                losBlocking[cell] = false;
            }
            drop( p, cell );
        }
    }

    protected Room randomRoom( Class<?extends Room> type ) {
        Random.shuffle( rooms );
        for (Room r : rooms) {
            if (type.isInstance(r)
                    //compatibility with pre-0.6.0 saves
                    || (type == StandardRoom.class && r.legacyType.equals("STANDARD"))) {
                return r;
            }
        }
        return null;
    }

    public Room room( int pos ) {
        for (Room room : rooms) {
            if (room.inside( cellToPoint(pos) )) {
                return room;
            }
        }

        return null;
    }

    protected int randomDropCell() {
        while (true) {
            Room room = randomRoom( StandardRoom.class );
            if (room != null && room != roomEntrance) {
                int pos = pointToCell(room.random());
                if (passable[pos]
                        && pos != exit
                        && heaps.get(pos) == null) {

                    Trap t = traps.get(pos);

                    //items cannot spawn on traps which destroy items
                    if (t == null ||
                            ! (t instanceof FireTrap || t instanceof BlazingTrap
                                    || t instanceof ChillingTrap || t instanceof FrostTrap
                                    || t instanceof ExplosiveTrap || t instanceof DisintegrationTrap)) {

                        return pos;
                    }
                }
            }
        }
    }

    @Override
    public int fallCell( boolean fallIntoPit ) {
        if (fallIntoPit) {
            for (Room room : rooms) {
                if (room instanceof PitRoom || room.legacyType.equals("PIT")) {
                    int result;
                    do {
                        result = pointToCell(room.random());
                    } while (traps.get(result) != null
                            || findMob(result) != null
                            || heaps.get(result) != null);
                    return result;
                }
            }
        }
        return super.fallCell( false );
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( "rooms", rooms );
        bundle.put( "mobs_to_spawn", mobsToSpawn.toArray(new Class[0]));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        rooms = new ArrayList<>( (Collection<Room>) ((Collection<?>) bundle.getCollection( "rooms" )) );
        for (Room r : rooms) {
            r.onLevelLoad( this );
            if (r instanceof EntranceRoom || r.legacyType.equals("ENTRANCE")){
                roomEntrance = r;
            } else if (r instanceof ExitRoom  || r.legacyType.equals("EXIT")){
                roomExit = r;
            }
        }

        if (bundle.contains( "mobs_to_spawn" )) {
            for (Class<? extends Mob> mob : bundle.getClassArray("mobs_to_spawn")) {
                if (mob != null) mobsToSpawn.add(mob);
            }
        }
    }
}
