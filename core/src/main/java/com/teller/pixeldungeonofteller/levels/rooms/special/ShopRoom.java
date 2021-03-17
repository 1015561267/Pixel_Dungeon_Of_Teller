package com.teller.pixeldungeonofteller.levels.rooms.special;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.hero.Belongings;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.Shopkeeper;
import com.teller.pixeldungeonofteller.items.Ankh;
import com.teller.pixeldungeonofteller.items.Bomb;
import com.teller.pixeldungeonofteller.items.Generator;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Honeypot;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.MerchantsBeacon;
import com.teller.pixeldungeonofteller.items.OverpricedSewingKit;
import com.teller.pixeldungeonofteller.items.Stylus;
import com.teller.pixeldungeonofteller.items.Torch;
import com.teller.pixeldungeonofteller.items.Weightstone;
import com.teller.pixeldungeonofteller.items.armor.LeatherArmor;
import com.teller.pixeldungeonofteller.items.armor.MailArmor;
import com.teller.pixeldungeonofteller.items.armor.PlateArmor;
import com.teller.pixeldungeonofteller.items.armor.ScaleArmor;
import com.teller.pixeldungeonofteller.items.artifacts.TimekeepersHourglass;
import com.teller.pixeldungeonofteller.items.bags.Bag;
import com.teller.pixeldungeonofteller.items.bags.PotionBandolier;
import com.teller.pixeldungeonofteller.items.bags.ScrollHolder;
import com.teller.pixeldungeonofteller.items.bags.SeedPouch;
import com.teller.pixeldungeonofteller.items.bags.WandHolster;
import com.teller.pixeldungeonofteller.items.food.OverpricedRation;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.potions.PotionOfHealing;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfIdentify;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicMapping;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfRemoveCurse;
import com.teller.pixeldungeonofteller.items.wands.Wand;
import com.teller.pixeldungeonofteller.items.weapon.missiles.CurareDart;
import com.teller.pixeldungeonofteller.items.weapon.missiles.IncendiaryDart;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Sword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tamahawk;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.BattleAxe;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.HandAxe;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Longsword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Mace;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.NewShortsword;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.WarHammer;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.Greatsword;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.painters.Painter;
import com.teller.pixeldungeonofteller.plants.Plant;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShopRoom extends SpecialRoom {

    private ArrayList<Item> itemsToSpawn;

    @Override
    public int minWidth() {
        if (itemsToSpawn == null) itemsToSpawn = generateItems();
        return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
    }

    @Override
    public int minHeight() {
        if (itemsToSpawn == null) itemsToSpawn = generateItems();
        return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
    }

    public void paint( Level level ) {

        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        placeShopkeeper( level );

        placeItems( level );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }

    }

    protected void placeShopkeeper( Level level ) {

        int pos = level.pointToCell(center());

        Mob shopkeeper = new Shopkeeper();
        shopkeeper.pos = pos;
        level.mobs.add( shopkeeper );

    }

    protected void placeItems( Level level ){

        if (itemsToSpawn == null)
            itemsToSpawn = generateItems();

        Point itemPlacement = new Point(entrance());
        if (itemPlacement.y == top){
            itemPlacement.y++;
        } else if (itemPlacement.y == bottom) {
            itemPlacement.y--;
        } else if (itemPlacement.x == left){
            itemPlacement.x++;
        } else {
            itemPlacement.x--;
        }

        for (Item item : itemsToSpawn) {

            if (itemPlacement.x == left+1 && itemPlacement.y != top+1){
                itemPlacement.y--;
            } else if (itemPlacement.y == top+1 && itemPlacement.x != right-1){
                itemPlacement.x++;
            } else if (itemPlacement.x == right-1 && itemPlacement.y != bottom-1){
                itemPlacement.y++;
            } else {
                itemPlacement.x--;
            }

            int cell = level.pointToCell(itemPlacement);

            if (level.heaps.get( cell ) != null) {
                do {
                    cell = level.pointToCell(random());
                } while (level.heaps.get( cell ) != null || level.findMob( cell ) != null);
            }

            level.drop( item, cell ).type = Heap.Type.FOR_SALE;
        }

    }

    protected static ArrayList<Item> generateItems() {

        ArrayList<Item> itemsToSpawn = new ArrayList<>();

        switch (Dungeon.depth) {
            case 6:
                itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new NewShortsword().identify() : new HandAxe()).identify() );
                itemsToSpawn.add( Random.Int( 2 ) == 0 ?
                        new IncendiaryDart().quantity(Random.NormalIntRange(2, 4)) :
                        new CurareDart().quantity(Random.NormalIntRange(1, 3)));
                itemsToSpawn.add( new LeatherArmor().identify() );
                break;

            case 11:
                itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Sword().identify() : new Mace()).identify() );
                itemsToSpawn.add( Random.Int( 2 ) == 0 ?
                        new CurareDart().quantity(Random.NormalIntRange(2, 5)) :
                        new NinjaProsthesis.Shuriken().quantity(Random.NormalIntRange(3, 6)));
                itemsToSpawn.add( new MailArmor().identify() );
                break;

            case 16:
                itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Longsword().identify() : new BattleAxe()).identify() );
                itemsToSpawn.add( new ScaleArmor().identify() );
                break;

            case 21:
                itemsToSpawn.add( Random.Int( 2 ) == 0 ? new Greatsword().identify() : new WarHammer().identify() );
                itemsToSpawn.add(
                        new Tamahawk().quantity(Random.NormalIntRange(1, 2)));
                itemsToSpawn.add( new PlateArmor().identify() );
                itemsToSpawn.add( new Torch() );
                itemsToSpawn.add( new Torch() );
                itemsToSpawn.add( new Torch() );
                break;
        }

        itemsToSpawn.add( new MerchantsBeacon() );


        itemsToSpawn.add(ChooseBag(Dungeon.hero.belongings));


        itemsToSpawn.add( new PotionOfHealing() );
        for (int i=0; i < 3; i++)
            itemsToSpawn.add( Generator.random( Generator.Category.POTION ) );

        itemsToSpawn.add( new ScrollOfIdentify() );
        itemsToSpawn.add( new ScrollOfRemoveCurse() );
        itemsToSpawn.add( new ScrollOfMagicMapping() );
        itemsToSpawn.add( Generator.random( Generator.Category.SCROLL ) );

        for (int i=0; i < 2; i++)
            itemsToSpawn.add( Random.Int(2) == 0 ?
                    Generator.random( Generator.Category.POTION ) :
                    Generator.random( Generator.Category.SCROLL ) );


        itemsToSpawn.add( new OverpricedRation() );
        itemsToSpawn.add( new OverpricedSewingKit() );

        itemsToSpawn.add( new Bomb().random() );
        switch (Random.Int(5)){
            case 1:
                itemsToSpawn.add( new Bomb() );
                break;
            case 2:
                itemsToSpawn.add( new Bomb().random() );
                break;
            case 3:
            case 4:
                itemsToSpawn.add( new Honeypot() );
                break;
        }


        if (Dungeon.depth == 6) {
            itemsToSpawn.add( new Ankh() );
            itemsToSpawn.add( new Weightstone() );
        } else {
            itemsToSpawn.add(Random.Int(2) == 0 ? new Ankh() : new Weightstone());
        }


        TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
        if (hourglass != null){
            int bags = 0;
            //creates the given float percent of the remaining bags to be dropped.
            //this way players who get the hourglass late can still max it, usually.
            switch (Dungeon.depth) {
                case 6:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f ); break;
                case 11:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
                case 16:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f ); break;
                case 21:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f ); break;
            }

            for(int i = 1; i <= bags; i++){
                itemsToSpawn.add( new TimekeepersHourglass.sandBag());
                hourglass.sandBags ++;
            }
        }

        Item rare;
        switch (Random.Int(10)){
            case 0:
                rare = Generator.random( Generator.Category.WAND );
                rare.level( 0 );
                break;
            case 1:
                rare = Generator.random(Generator.Category.RING);
                rare.level( 1 );
                break;
            case 2:
                rare = Generator.random( Generator.Category.ARTIFACT ).identify();
                break;
            default:
                rare = new Stylus();
        }
        rare.cursed = rare.cursedKnown = false;
        itemsToSpawn.add( rare );

        //hard limit is 63 items + 1 shopkeeper, as shops can't be bigger than 8x8=64 internally
        if (itemsToSpawn.size() > 63)
            throw new RuntimeException("Shop attempted to carry more than 63 items!");

        Random.shuffle(itemsToSpawn);
        return itemsToSpawn;
    }

    protected static Bag ChooseBag(Belongings pack){

        int seeds = 0, scrolls = 0, potions = 0, wands = 0;

        //count up items in the main bag, for bags which haven't yet been dropped.
        for (Item item : pack.backpack.items) {
            if (!Dungeon.limitedDrops.seedBag.dropped() && item instanceof Plant.Seed)
                seeds++;
            else if (!Dungeon.limitedDrops.scrollBag.dropped() && item instanceof Scroll)
                scrolls++;
            else if (!Dungeon.limitedDrops.potionBag.dropped() && item instanceof Potion)
                potions++;
            else if (!Dungeon.limitedDrops.wandBag.dropped() && item instanceof Wand)
                wands++;
        }

        //then pick whichever valid bag has the most items available to put into it.
        //note that the order here gives a perference if counts are otherwise equal
        if (seeds >= scrolls && seeds >= potions && seeds >= wands && !Dungeon.limitedDrops.seedBag.dropped()) {
            Dungeon.limitedDrops.seedBag.drop();
            return new SeedPouch();

        } else if (scrolls >= potions && scrolls >= wands && !Dungeon.limitedDrops.scrollBag.dropped()) {
            Dungeon.limitedDrops.scrollBag.drop();
            return new ScrollHolder();

        } else if (potions >= wands && !Dungeon.limitedDrops.potionBag.dropped()) {
            Dungeon.limitedDrops.potionBag.drop();
            return new PotionBandolier();

        } else if (!Dungeon.limitedDrops.wandBag.dropped()) {
            Dungeon.limitedDrops.wandBag.drop();
            return new WandHolster();
        }

        return null;
    }

}
