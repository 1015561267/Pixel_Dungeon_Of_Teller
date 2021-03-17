package com.teller.pixeldungeonofteller.items;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.Flare;
import com.teller.pixeldungeonofteller.effects.particles.BlastParticle;
import com.teller.pixeldungeonofteller.effects.particles.ShadowParticle;
import com.teller.pixeldungeonofteller.effects.particles.SmokeParticle;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.HolyBomb;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Holybomb extends Bomb {
    {
        image = ItemSpriteSheet.HOLYBOMB;
        usesTargeting = true;
        stackable = false;
    }

    @Override
    public boolean doPickUp(Hero hero) {
        GLog.w(Messages.get(this, "nopickup"));
        return false;
    }

    @Override
    public void explode(int cell) {
        this.fuse = null;
        Sample.INSTANCE.play(Assets.SND_BLAST);
        if (Dungeon.visible[cell]) {
            CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            new Flare(10, 64).show(Dungeon.hero.sprite.parent, DungeonTilemap.tileCenterToWorld(cell), 2f);
        }
        boolean terrainAffected = false;

        Heap heap = Dungeon.level.heaps.get(cell);
        for (Item item : heap.items.toArray(new Item[0])) {
            {
                if (item instanceof HolyBomb) {
                    heap.items.remove(item);
                }
            }
        }
        if (heap.isEmpty()) {
            heap.destroy();
        } else if (heap.sprite != null) {
            heap.sprite.view(heap.items.peek());
        }

        for (int n : PathFinder.NEIGHBOURS9) {
            int c = cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.visible[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                }
                Char ch = Actor.findChar(c);
                if (ch != null) {
                    if (ch != Dungeon.hero) {
                        //those not at the center of the blast take damage less consistently.
                        int minDamage = 4 + 2 * Dungeon.hero.INT();
                        int maxDamage = 22 + 8 * Dungeon.hero.INT();
                        int dmg = Random.NormalIntRange(minDamage, maxDamage); //- ch.drRoll();
                        if (dmg > 0) {
                            MagicalDamage damage = new MagicalDamage();
                            damage.AddHoly(dmg);
                            ch.damage(damage, this);
                        }
                        if (ch == Dungeon.hero && !ch.isAlive())
                            Dungeon.fail(getClass());
                        if (ch.properties().contains(Char.Property.UNDEAD) || ch.properties().contains(Char.Property.DEMONIC)) {
                            ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                        }
                    }
                }


            }
        }
        if (terrainAffected) {
            Dungeon.observe();
        }
        Sample.INSTANCE.play( Assets.SND_READ );
    }

    @Override
    public void onThrow(int cell) {
        if (!Dungeon.level.pit[cell]) {
            Actor.addDelayed(fuse = new Fuse().ignite(this), 2);
            super.onThrow(cell);
        }
        else {
            if (Dungeon.visible[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
                new Flare(10, 64).show(Dungeon.hero.sprite.parent, DungeonTilemap.tileCenterToWorld(cell), 2f);
            }

            for (int n : PathFinder.NEIGHBOURS9) {
                int c = cell + n;
                if (c >= 0 && c < Dungeon.level.length()) {
                    if (Dungeon.visible[c]) {
                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    }
                    Char ch = Actor.findChar(c);
                    if (ch != null) {
                        if (ch != Dungeon.hero) {
                            //those not at the center of the blast take damage less consistently.
                            int minDamage = 4 + 2 * Dungeon.hero.INT();
                            int maxDamage = 22 + 8 * Dungeon.hero.INT();
                            int dmg = Random.NormalIntRange(minDamage, maxDamage); //- ch.drRoll();
                            if (dmg > 0) {
                                MagicalDamage damage = new MagicalDamage();
                                damage.AddHoly(dmg);
                                ch.damage(damage, this);
                            }
                            if (ch == Dungeon.hero && !ch.isAlive())
                                Dungeon.fail(getClass());
                            if (ch.properties().contains(Char.Property.UNDEAD) || ch.properties().contains(Char.Property.DEMONIC)) {
                                ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc_burning");
    }
}
