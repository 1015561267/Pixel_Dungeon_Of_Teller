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
import com.teller.pixeldungeonofteller.Bones;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.GamesInProgress;
import com.teller.pixeldungeonofteller.Statistics;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Awareness;
import com.teller.pixeldungeonofteller.actors.buffs.Barkskin;
import com.teller.pixeldungeonofteller.actors.buffs.Berserk;
import com.teller.pixeldungeonofteller.actors.buffs.Bless;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.CombinationCoolDown;
import com.teller.pixeldungeonofteller.actors.buffs.CombinationReady;
import com.teller.pixeldungeonofteller.actors.buffs.Combo;
import com.teller.pixeldungeonofteller.actors.buffs.Drowsy;
import com.teller.pixeldungeonofteller.actors.buffs.EarthImbue;
import com.teller.pixeldungeonofteller.actors.buffs.FireImbue;
import com.teller.pixeldungeonofteller.actors.buffs.FlavourBuff;
import com.teller.pixeldungeonofteller.actors.buffs.Fury;
import com.teller.pixeldungeonofteller.actors.buffs.Guard;
import com.teller.pixeldungeonofteller.actors.buffs.Hunger;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.buffs.Levitation;
import com.teller.pixeldungeonofteller.actors.buffs.ManaRegeneration;
import com.teller.pixeldungeonofteller.actors.buffs.MindVision;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.actors.buffs.Regeneration;
import com.teller.pixeldungeonofteller.actors.buffs.ShieldRecharging;
import com.teller.pixeldungeonofteller.actors.buffs.SnipersMark;
import com.teller.pixeldungeonofteller.actors.buffs.Vertigo;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.actors.mobs.npcs.NPC;
import com.teller.pixeldungeonofteller.effects.CellEmitter;
import com.teller.pixeldungeonofteller.effects.CheckedCell;
import com.teller.pixeldungeonofteller.effects.Flare;
import com.teller.pixeldungeonofteller.effects.Pushing;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.Amulet;
import com.teller.pixeldungeonofteller.items.Ankh;
import com.teller.pixeldungeonofteller.items.Dewdrop;
import com.teller.pixeldungeonofteller.items.EquipableItem;
import com.teller.pixeldungeonofteller.items.Heap;
import com.teller.pixeldungeonofteller.items.Heap.Type;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.items.armor.glyphs.AntiMagic;
import com.teller.pixeldungeonofteller.items.armor.glyphs.Flow;
import com.teller.pixeldungeonofteller.items.armor.glyphs.Stone;
import com.teller.pixeldungeonofteller.items.armor.glyphs.Swiftness;
import com.teller.pixeldungeonofteller.items.armor.glyphs.Viscosity;
import com.teller.pixeldungeonofteller.items.artifacts.CapeOfThorns;
import com.teller.pixeldungeonofteller.items.artifacts.DriedRose;
import com.teller.pixeldungeonofteller.items.artifacts.EtherealChains;
import com.teller.pixeldungeonofteller.items.artifacts.HornOfPlenty;
import com.teller.pixeldungeonofteller.items.artifacts.TalismanOfForesight;
import com.teller.pixeldungeonofteller.items.artifacts.TimekeepersHourglass;
import com.teller.pixeldungeonofteller.items.keys.CrystalKey;
import com.teller.pixeldungeonofteller.items.keys.GoldenKey;
import com.teller.pixeldungeonofteller.items.keys.IronKey;
import com.teller.pixeldungeonofteller.items.keys.Key;
import com.teller.pixeldungeonofteller.items.keys.SkeletonKey;
import com.teller.pixeldungeonofteller.items.potions.Potion;
import com.teller.pixeldungeonofteller.items.potions.PotionOfMight;
import com.teller.pixeldungeonofteller.items.potions.PotionOfStrength;
import com.teller.pixeldungeonofteller.items.rings.RingOfElements;
import com.teller.pixeldungeonofteller.items.rings.RingOfEvasion;
import com.teller.pixeldungeonofteller.items.rings.RingOfFuror;
import com.teller.pixeldungeonofteller.items.rings.RingOfHaste;
import com.teller.pixeldungeonofteller.items.rings.RingOfMight;
import com.teller.pixeldungeonofteller.items.rings.RingOfTenacity;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicMapping;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfMagicalInfusion;
import com.teller.pixeldungeonofteller.items.scrolls.ScrollOfUpgrade;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.Gauntlet;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tamahawk;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.Flintlock;
import com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm.SubmachineGun;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.Flail;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.TwoHandedWeapon;
import com.teller.pixeldungeonofteller.journal.Notes;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.features.AlchemyPot;
import com.teller.pixeldungeonofteller.levels.features.Chasm;
import com.teller.pixeldungeonofteller.levels.features.Sign;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.plants.Earthroot;
import com.teller.pixeldungeonofteller.plants.Sungrass;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.scenes.InterlevelScene;
import com.teller.pixeldungeonofteller.scenes.SurfaceScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.HeroSprite;
import com.teller.pixeldungeonofteller.ui.AttackIndicator;
import com.teller.pixeldungeonofteller.ui.BuffIndicator;
import com.teller.pixeldungeonofteller.ui.DangerIndicator;
import com.teller.pixeldungeonofteller.ui.MainHandIndicator;
import com.teller.pixeldungeonofteller.ui.OffHandIndicator;
import com.teller.pixeldungeonofteller.ui.QuickSlotButton;
import com.teller.pixeldungeonofteller.utils.BArray;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndMessage;
import com.teller.pixeldungeonofteller.windows.WndResurrect;
import com.teller.pixeldungeonofteller.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class Hero extends Char {

    public static final int MAX_LEVEL = 30;
    public static final int STARTING_STR = 1;
    public static final int STARTING_DEX = 1;
    public static final int STARTING_INT = 1;

    //public static final int MAX_NOISE=100;

    private static final float TIME_TO_PICK_UP = 1.0f;
    private static final float TIME_TO_REST = 1f;
    private static final float TIME_TO_SEARCH = 2f;
    private static final String ATTACK = "attackSkill";
    private static final String DEFENSE = "defenseSkill";
    private static final String STRENGTH = "STR";
    private static final String DEXTERITY = "DEX";
    private static final String INTELLIGENCE = "INT";
    private static final String MANAPOINT = "MANA";
    private static final String MANAPOINTCAP = "MANACAP";
    private static final int NOISE_REST=5;
    private static final int NOISE_MOVE_ONGRASS=-1;
    private static final int NOISE_MOVE_ONGROUND=-2;
    private static final int NOISE_MOVE_ONWATER=-3;
    private static final int NOISE_MOVE_OPENDOOR=-5;
    private static final int NOISE_PICK_UP=-2;
    private static final int NOISE_MOVE_FLY=-1;
    private static final int NOISE_ATTACK=-5;
    private static final int NOISE_KILL=3;
    private static final int NOISE_THROW=-3;
    private static final int NOISE_USEITEM=-2;
    private static final String LEVEL = "lvl";
    private static final String EXPERIENCE = "exp";
    public HeroClass heroClass = HeroClass.ROGUE;
    public HeroSubClass subClass = HeroSubClass.NONE;
    public boolean ready = false;
    public HeroAction curAction = null;
    public HeroAction lastAction = null;
    public boolean resting = false;
    public MissileWeapon rangedWeapon = null;
    public Tamahawk tamahawk = null;
    public Belongings belongings;
    public int STR;
    public int DEX;
    public int INT;
    public int MANA;
    public int MANACAP;
    public float particalmana;
    public boolean weakened = false;
    public float awareness;
    public int lvl = 1;
    public int exp = 0;
    public int laststep = pos;
    //This list is maintained so that some logic checks can be skipped
    // for enemies we know we aren't seeing normally, resultign in better performance
    public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();
    private int attackSkill = 10;
    private int defenseSkill = 5;
    private boolean damageInterrupt = true;
    private Char enemy;
    private Item theKey;
    private ArrayList<Mob> visibleEnemies;
    //effectively cache this buff to prevent having to call buff(Berserk.class) a bunch.
    //This is relevant because we call isAlive during drawing, which has both performance
    //and concurrent modification implications if that method calls buff(Berserk.class)
    private Berserk berserk;

    {
        actPriority = 0; //acts at priority 0, baseline for the rest of behaviour.
    }
    public Hero() {
        super();
        name = Messages.get(this, "name");

        HP = HT = 20;
        STR = STARTING_STR;
        DEX = STARTING_DEX;
        INT = STARTING_INT;

        MANA = MANACAP =21;
        awareness = 0.1f;
        SHLD = 0;
        ARMOR = 0;
        MAXSHLD = 0;

        belongings = new Belongings(this);

        visibleEnemies = new ArrayList<Mob>();
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle) {
        info.level = bundle.getInt( LEVEL );
        info.str = bundle.getInt( STRENGTH );
        info.exp = bundle.getInt( EXPERIENCE );
        info.hp = bundle.getInt( Char.TAG_HP );
        info.ht = bundle.getInt( Char.TAG_HT );
        info.shld = bundle.getInt( Char.TAG_SHLD );
        info.heroClass = HeroClass.restoreInBundle( bundle );
        info.subClass = HeroSubClass.restoreInBundle( bundle );
        Belongings.preview( info, bundle );
    }

    public static void reallyDie(Object cause) {

        int length = Dungeon.level.length();
        int[] map = Dungeon.level.map;
        boolean[] visited = Dungeon.level.visited;
        boolean[] discoverable =  Dungeon.level.discoverable;

        for (int i = 0; i < length; i++) {

            int terr = map[i];

            if (discoverable[i]) {

                visited[i] = true;
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
                    Dungeon.level.discover(i);
                }
            }
        }

        Bones.leave();

        Dungeon.observe();
        GameScene.updateFog();

        Dungeon.hero.belongings.identify();

        int pos = Dungeon.hero.pos;

        ArrayList<Integer> passable = new ArrayList<Integer>();
        for (Integer ofs : PathFinder.NEIGHBOURS8) {
            int cell = pos + ofs;
            if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Dungeon.level.heaps.get(cell) == null) {
                passable.add(cell);
            }
        }
        Collections.shuffle(passable);

        ArrayList<Item> items = new ArrayList<Item>(Dungeon.hero.belongings.backpack.items);
        for (Integer cell : passable) {
            if (items.isEmpty()) {
                break;
            }

            Item item = Random.element(items);
            Dungeon.level.drop(item, cell).sprite.drop(pos);
            items.remove(item);
        }

        GameScene.gameOver();

        if (cause instanceof Hero.Doom) {
            ((Hero.Doom) cause).onDeath();
        }

        Dungeon.deleteGame(GamesInProgress.curSlot, true);
    }

    public int STR() {
        int STR = this.STR;

        STR += RingOfMight.getBonus(this, RingOfMight.Might.class);

        return weakened ? STR - 2 : STR;
    }

    public int DEX() {
        int DEX = this.DEX;

        return weakened ? DEX - 2 : DEX;
    }

    public int INT() {
        int INT = this.INT;

        return weakened ? INT - 2 : INT;
    }

    //public int NOISE()
    //{
    //    return buff(Stealth.class).Stealth();
    //}

    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        heroClass.storeInBundle(bundle);
        subClass.storeInBundle(bundle);

        bundle.put(ATTACK, attackSkill);
        bundle.put(DEFENSE, defenseSkill);

        bundle.put(MANAPOINT,MANA);
        bundle.put(MANAPOINTCAP,MANACAP);

        bundle.put(STRENGTH, STR);
        bundle.put(DEXTERITY, DEX);
        bundle.put(INTELLIGENCE, INT);

        bundle.put(LEVEL, lvl);
        bundle.put(EXPERIENCE, exp);

        belongings.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        heroClass = HeroClass.restoreInBundle(bundle);
        subClass = HeroSubClass.restoreInBundle(bundle);

        attackSkill = bundle.getInt(ATTACK);
        defenseSkill = bundle.getInt(DEFENSE);

        MANA = bundle.getInt(MANAPOINT);
        MANACAP = bundle.getInt(MANAPOINTCAP);

        STR = bundle.getInt(STRENGTH);
        DEX = bundle.getInt(DEXTERITY);
        INT = bundle.getInt(INTELLIGENCE);

        updateAwareness();

        lvl = bundle.getInt(LEVEL);
        exp = bundle.getInt(EXPERIENCE);

        belongings.restoreFromBundle(bundle);
    }

    //inorder to give a clear way of consulting damage of dualwielding weapon,I have no choice but to overwrite attack,it sounds really stupid,but I have no choice...I would be glad to take your advice if you can help me fix it.
    @Override
    public boolean attack(Char enemy) {
        if (enemy == null || !enemy.isAlive()) return false;
        boolean visibleFight = Dungeon.visible[pos] || Dungeon.visible[enemy.pos];
        boolean hit1 = false;
        boolean hit2 = false;
        if (buff(CombinationCoolDown.class) != null) {
            buff(CombinationCoolDown.class).recharge(belongings.mainhandweapon != null ? (belongings.mainhandweapon.speedFactor(this)) : 1);
        }

        if (mainhandhit(this, enemy, false)) {

            int dr = this instanceof Hero && this.rangedWeapon != null && this.subClass ==
                    HeroSubClass.SNIPER ? 0 : enemy.drRoll();

            Damage dmg1 = damageRoll();
            dmg1 = attackProc(enemy, dmg1);
            dmg1 = enemy.defenseProc(this, dmg1);

            if (visibleFight) {
                Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
            }

            enemy.before_damage(dmg1,this);
            enemy.damage(dmg1, this);
            enemy.after_damage(dmg1,this);

            int effectiveDamage = Math.max(dmg1.effictivehpdamage - dr, 0);

            if (buff(FireImbue.class) != null)
                buff(FireImbue.class).proc(enemy);
            if (buff(EarthImbue.class) != null)
                buff(EarthImbue.class).proc(enemy);
            enemy.sprite.bloodBurstA(sprite.center(), effectiveDamage);
            enemy.sprite.flash();
            //TODO: consider revisiting this and shaking in more cases.
            float shake = 0f;
            if (enemy == Dungeon.hero)
                shake = effectiveDamage / (enemy.HT / 4);
            if (shake > 1f)
                Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
            if (!enemy.isAlive() && visibleFight) {
                if (enemy == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name)));
                } else if (this == Dungeon.hero) {
                    GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                    hero.buff(Noise.class).killNoise();
                }
            }
            hit1 = true;
        } else {
            String defense = enemy.defenseVerb();
            enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
            Sample.INSTANCE.play(Assets.SND_MISS);
        }

        if (buff(CombinationReady.class) != null && belongings.offhandweapon.attackable() && canoffhandAttack(enemy)) {
            {
                Buff.detach(this, CombinationReady.class);
                Buff.affect(this, CombinationCoolDown.class);
                buff(CombinationCoolDown.class).set(belongings.offhandweapon.cooldown());
                if (offhandhit(this, enemy, false)) {
                    int dr = this instanceof Hero && this.rangedWeapon != null && this.subClass ==
                            HeroSubClass.SNIPER ? 0 : enemy.drRoll();
                    Damage dmg2 = offhanddamageRoll();
                    dmg2 = offhandattackProc(enemy, dmg2);
                    dmg2 = enemy.defenseProc(this, dmg2);

                    enemy.before_damage(dmg2,this);
                    enemy.damage(dmg2, this);
                    enemy.after_damage(dmg2,this);

                    if (visibleFight) {
                        Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
                    }
                    int effectiveDamage = Math.max(dmg2.effictivehpdamage - dr, 0);
                    if (!enemy.isAlive()) {
                        return true;
                    }
                    if (buff(FireImbue.class) != null)
                        buff(FireImbue.class).proc(enemy);
                    if (buff(EarthImbue.class) != null)
                        buff(EarthImbue.class).proc(enemy);
                    enemy.sprite.bloodBurstA(sprite.center(), effectiveDamage);
                    enemy.sprite.flash();
                    //TODO: consider revisiting this and shaking in more cases.
                    float shake = 0f;
                    if (enemy == Dungeon.hero)
                        shake = effectiveDamage / (enemy.HT / 4);
                    if (shake > 1f)
                        Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
                    if (!enemy.isAlive() && visibleFight) {
                        if (enemy == Dungeon.hero) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name)));
                        } else if (this == Dungeon.hero) {
                            GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                            hero.buff(Noise.class).killNoise();
                        }
                    }
                    hit2 = true;
                } else {
                    String defense = enemy.defenseVerb();
                    enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                    Sample.INSTANCE.play(Assets.SND_MISS);
                }
            }
        }
        return hit1 || hit2;
    }

    public boolean mainhandhit(Char attacker, Char defender, boolean magic) {
        return hit(attacker, defender, magic);
    }

    public boolean offhandhit(Char attacker, Char defender, boolean magic) {
        if (this.belongings.offhandweapon == null) return false;
        else {
            float accuracy = 1;
            if ((rangedWeapon != null || tamahawk != null) && Dungeon.level.distance(pos, defender.pos) == 1) {
                accuracy *= 0.5f;
            }
            KindOfWeapon kindOfWeapon;
            if (tamahawk != null) {
                kindOfWeapon = tamahawk;
            }
            kindOfWeapon = rangedWeapon != null ? rangedWeapon : belongings.offhandweapon;
            if (kindOfWeapon != null) {
                accuracy = (int) (attackSkill * accuracy * kindOfWeapon.accuracyFactor(this));
            } else {
                accuracy = (int) (attackSkill * accuracy);
            }
            float acuRoll = Random.Float(accuracy);
            float defRoll = Random.Float(defender.defenseSkill(attacker));
            if (attacker.buff(Bless.class) != null) acuRoll *= 1.20f;
            if (defender.buff(Bless.class) != null) defRoll *= 1.20f;
            return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
        }
    }

    public String className() {
        return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
    }

    public String givenName() {
        return name.equals(Messages.get(this, "name")) ? className() : name;
    }

    public void live() {
        Buff.affect(this, Regeneration.class);
        Buff.affect(this, Hunger.class);
        Buff.affect(this, ManaRegeneration.class);
        Buff.affect(this, ShieldRecharging.class);
        Buff.affect(this, Noise.class);
    }

    public int tier() {
        return belongings.armor == null ? 0 : belongings.armor.tier;
    }

    public boolean shoot(Char enemy, MissileWeapon wep) {
        rangedWeapon = wep;
        boolean result = attack(enemy);
        Invisibility.dispel();
        rangedWeapon = null;
        return result;
    }

    public boolean shoot(Char enemy, Tamahawk tamahawk) {
        this.tamahawk = tamahawk;
        boolean result = attack(enemy);
        Invisibility.dispel();
        this.tamahawk = null;
        return result;
    }

    @Override
    public int attackSkill(Char target) {
        float accuracy = 1;
        if ((rangedWeapon != null || tamahawk != null) && Dungeon.level.distance(pos, target.pos) == 1) {
            accuracy *= 0.5f;
        }
        KindOfWeapon kindOfWeapon;
        if (tamahawk != null) {
            kindOfWeapon = tamahawk;
        }
        //KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.mainhandweapon;
        kindOfWeapon = rangedWeapon != null ? rangedWeapon : belongings.mainhandweapon;
        //if (wep != null) {
        //	return (int)(attackSkill * accuracy * wep.accuracyFactor( this ));
        //} else {
        //	return (int)(attackSkill * accuracy);
        //}
        if (kindOfWeapon != null) {
            return (int) (attackSkill * accuracy * kindOfWeapon.accuracyFactor(this));
        } else {
            return (int) (attackSkill * accuracy);
        }
    }

    @Override
    public int defenseSkill(Char enemy) {

        int bonus = RingOfEvasion.getBonus(this, RingOfEvasion.Evasion.class);

        float evasion = (float) Math.pow(1.125, bonus);
        if (paralysed > 0) {
            evasion /= 2;
        }

        int aEnc = belongings.armor != null ? belongings.armor.STRReq() - STR() : 10 - STR();

        if (aEnc > 0) {
            return (int) (defenseSkill * evasion / Math.pow(1.5, aEnc));
        } else {

            bonus = 0;
            if (heroClass == HeroClass.ROGUE) bonus += -aEnc;

            if (belongings.armor != null && belongings.armor.hasGlyph(Swiftness.class))
                bonus += 5 + belongings.armor.level() * 1.5f;

            return Math.round((defenseSkill + bonus) * evasion);
        }
    }

    @Override
    public int drRoll() {
        int dr = 0;
        Barkskin bark = buff(Barkskin.class);

        if (belongings.armor != null) {
            dr += Random.NormalIntRange(belongings.armor.DRMin(), belongings.armor.DRMax());
            if (STR() < belongings.armor.STRReq()) {
                dr -= 2 * (belongings.armor.STRReq() - STR());
                dr = Math.max(dr, 0);
            }
        }
        if (belongings.mainhandweapon != null)
            dr += Random.NormalIntRange(0, belongings.mainhandweapon.defenseFactor(this));
        if (bark != null) dr += Random.NormalIntRange(0, bark.level());
        return dr;
    }

    @Override
    public PhysicalDamage damageRoll() {
        KindOfWeapon wep1 = rangedWeapon != null ? rangedWeapon : belongings.mainhandweapon;

        if(tamahawk!=null)
        {
            wep1=tamahawk;
        }

        int dmg = 0;
        //int bonus = RingOfForce.getBonus(this, RingOfForce.Force.class);
        PhysicalDamage damage = new PhysicalDamage();
        if (wep1 != null) {
            damage = wep1.damageRoll(this);
            //damage.AddImpact(bonus);
            //dmg = wep.damageRoll( this ) + bonus;
        } else {
            dmg = Random.NormalIntRange(1, Math.max(STR() - 8, 1));
            damage.AddImpact(dmg);//if (bonus != 0){//	dmg = RingOfForce.damageRoll(this);//	damage.AddImpact(dmg);//}else {//}
        }

        if (dmg < 0) dmg = 0;
        if (subClass == HeroSubClass.BERSERKER) {
            berserk = Buff.affect(this, Berserk.class);
            dmg = berserk.damageFactor(dmg);
        }
        damage.multiplie(buff(Fury.class) != null ? 1.5f : 1f);
        //dmg=buff( Fury.class ) != null ? (int)(dmg * 1.5f) : dmg;
        //PhysicalDamage damage=new PhysicalDamage();
        //damage=wep.damageRoll();
        //damage.multiplie(dmg);
        return damage;
    }

    public PhysicalDamage offhanddamageRoll() {
        KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.offhandweapon;
        int dmg = 0;
        //int bonus = RingOfForce.getBonus(this, RingOfForce.Force.class);
        PhysicalDamage damage = new PhysicalDamage();
        if (wep != null) {
            damage = wep.damageRoll(this);
            //damage.AddImpact(bonus);
            //dmg = wep.damageRoll( this ) + bonus;
        } else {
            dmg = Random.NormalIntRange(1, Math.max(STR() - 8, 1));
            damage.AddImpact(dmg);//if (bonus != 0){//	dmg = RingOfForce.damageRoll(this);//	damage.AddImpact(dmg);//}else {//}
        }

        if (dmg < 0) dmg = 0;
        if (subClass == HeroSubClass.BERSERKER) {
            berserk = Buff.affect(this, Berserk.class);
            dmg = berserk.damageFactor(dmg);
        }
        damage.multiplie(buff(Fury.class) != null ? 1.5f : 1f);
        //dmg=buff( Fury.class ) != null ? (int)(dmg * 1.5f) : dmg;
        //PhysicalDamage damage=new PhysicalDamage();
        //damage=wep.damageRoll();
        //damage.multiplie(dmg);
        return damage;
    }

    @Override
    public float speed() {

        float speed = super.speed();

        int hasteLevel = RingOfHaste.getBonus(this, RingOfHaste.Haste.class);

        if (hasteLevel != 0)
            speed *= Math.pow(1.2, hasteLevel);

        Armor armor = belongings.armor;

        if (armor != null) {

            if (armor.hasGlyph(Swiftness.class)) {
                speed *= (1.1f + 0.01f * belongings.armor.level());
            } else if (armor.hasGlyph(Flow.class) && Dungeon.level.water[pos]) {
                speed *= (1.5f + 0.05f * belongings.armor.level());
            }
        }

        int aEnc = armor != null ? armor.STRReq() - STR() : 0;
        if (aEnc > 0) {

            return (float) (speed / Math.pow(1.2, aEnc));

        } else {

            return ((HeroSprite) sprite).sprint(subClass == HeroSubClass.FREERUNNER && !isStarving()) ?
                    invisible > 0 ?
                            2f * speed :
                            1.5f * speed :
                    speed;

        }
    }

    public boolean canSurpriseAttack() {
        if (belongings.mainhandweapon == null || !(belongings.mainhandweapon instanceof Weapon))
            return true;

        if (STR() < ((Weapon) belongings.mainhandweapon).STRReq())
            return false;

        return !(belongings.mainhandweapon instanceof Flail) || rangedWeapon != null;
    }

    public boolean offhandcanSurpriseAttack() {
        if (belongings.offhandweapon == null || !(belongings.offhandweapon instanceof Weapon))
            return true;

        return DEX() >= ((Weapon) belongings.offhandweapon).DEXReq();

        //if (belongings.mainhandweapon instanceof Flail && rangedWeapon == null)
        //	return false;
    }

    public boolean canAttack(Char enemy) {
        if (enemy == null || pos == enemy.pos)
            return false;

        //can always attack adjacent enemies
        if (Dungeon.level.adjacent(pos, enemy.pos))
            return true;

        KindOfWeapon wep = Dungeon.hero.belongings.mainhandweapon;

        if (wep != null && Dungeon.level.distance(pos, enemy.pos) <= wep.reachFactor(this)) {

            boolean[] passable = BArray.not(Dungeon.level.solid, null);
            for (Mob m : Dungeon.level.mobs)
                passable[m.pos] = false;

            PathFinder.buildDistanceMap(enemy.pos, passable, wep.reachFactor(this));

            return PathFinder.distance[pos] <= wep.reachFactor(this);

        } else {
            return false;
        }
    }

    public boolean canoffhandAttack(Char enemy) {
        if (enemy == null || pos == enemy.pos)
            return false;

        if(tamahawk!=null||rangedWeapon!=null)
            return false;

        //can always attack adjacent enemies
        if (Dungeon.level.adjacent(pos, enemy.pos))
            return true;

        KindOfWeapon wep = Dungeon.hero.belongings.offhandweapon;

        if (wep != null && Dungeon.level.distance(pos, enemy.pos) <= wep.reachFactor(this)) {

            boolean[] passable = BArray.not(Dungeon.level.solid, null);
            for (Mob m : Dungeon.level.mobs)
                passable[m.pos] = false;

            PathFinder.buildDistanceMap(enemy.pos, passable, wep.reachFactor(this));

            return PathFinder.distance[pos] <= wep.reachFactor(this);

        } else {
            return false;
        }
    }

    public float attackDelay() {
        KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.mainhandweapon;
        if (wep != null) {
            return wep.speedFactor(this);
        } else {
            //Normally putting furor speed on unarmed attacks would be unnecessary
            //But there's going to be that one guy who gets a furor+force ring combo
            //This is for that one guy, you shall get your fists of fury!
            int bonus = RingOfFuror.getBonus(this, RingOfFuror.Furor.class);
            return (float) (0.2 + (1 - 0.2) * Math.pow(0.85, bonus));
        }
    }

    @Override
    public void spend(float time) {
        justMoved = false;
        TimekeepersHourglass.timeFreeze buff = buff(TimekeepersHourglass.timeFreeze.class);
        if (!(buff != null && buff.processTime(time)))
            super.spend(time);
        reloadfirearm(time);
    }

    public void spendAndNext(float time) {
        busy();
        spend(time);
        next();
    }

    @Override
    public boolean act() {

        super.act();

        fieldOfView = Dungeon.hero.fieldOfView;

        if (!ready) {
            //do a full observe (including fog update) if not resting.
            if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null) {
                Dungeon.observe();
            } else {
                //otherwise just directly re-calculate FOV
                Dungeon.level.updateFieldOfView(this,fieldOfView);
            }
        }
        checkVisibleMobs();
        if (buff(FlavourBuff.class) != null) {
            BuffIndicator.refreshHero();
        }
        if (paralysed > 0) {
            curAction = null;
            spendAndNext(TICK);
            return false;
        }
        //checkVisibleMobs();
        if (curAction == null) {
            if (resting) {
                buff(Noise.class).fade();
                spend(TIME_TO_REST);
                next();
                return false;
            }
            ready();
            return false;
        } else {

            resting = false;

            ready = false;

            if (curAction instanceof HeroAction.Move) {

                buff(Noise.class).moveNoise();

                return actMove((HeroAction.Move) curAction);

            } else if (curAction instanceof HeroAction.Interact) {


                return actInteract((HeroAction.Interact) curAction);

            } else if (curAction instanceof HeroAction.Buy) {

                return actBuy((HeroAction.Buy) curAction);

            } else if (curAction instanceof HeroAction.PickUp) {

                return actPickUp((HeroAction.PickUp) curAction);

            } else if (curAction instanceof HeroAction.OpenChest) {

                return actOpenChest((HeroAction.OpenChest) curAction);

            } else if (curAction instanceof HeroAction.Unlock) {

                return actUnlock((HeroAction.Unlock) curAction);

            } else if (curAction instanceof HeroAction.Descend) {

                return actDescend((HeroAction.Descend) curAction);

            } else if (curAction instanceof HeroAction.Ascend) {

                return actAscend((HeroAction.Ascend) curAction);

            } else if (curAction instanceof HeroAction.Attack) {

                return actAttack((HeroAction.Attack) curAction);

            } else if (curAction instanceof HeroAction.Cook) {

                return actCook((HeroAction.Cook) curAction);

            }
        }
        return false;
    }

    private void reloadfirearm(float time)
    {
        if(this.belongings.mainhandweapon instanceof Flintlock)
        {
            ((Flintlock) this.belongings.mainhandweapon).reload(time);
        }

        if(this.belongings.offhandweapon instanceof Flintlock)
        {
            ((Flintlock) this.belongings.offhandweapon).reload(time);
        }
        else if(this.belongings.offhandweapon instanceof SubmachineGun)
        {
            ((SubmachineGun) this.belongings.offhandweapon).reload(time);
        }
    }

    public void busy() {
        ready = false;
    }

    private void ready() {
        if (sprite.looping()) sprite.idle();
        curAction = null;
        damageInterrupt = true;
        ready = true;
        AttackIndicator.updateState();
        GameScene.ready();
    }

    public void getready()
    {
        ready();
    }


    public void interrupt() {
        if (isAlive() && curAction != null && curAction instanceof HeroAction.Move && curAction.dst != pos) {
            lastAction = curAction;
        }
        curAction = null;
    }

    public void resume() {
        curAction = lastAction;
        lastAction = null;
        damageInterrupt = false;
        next();
    }

    public boolean justMoved = false;

    private boolean actMove(HeroAction.Move action) {
        if(this.buffs().contains(Levitation.class))
            buff(Noise.class).levitationNoise();
        //else
        if (Dungeon.level.map[pos] == Terrain.WATER)
            buff(Noise.class).moveTowaterNoise();
            //buff(Stealth.class).AlterStealth(NOISE_MOVE_ONWATER);
        //else if(Dungeon.level.map[pos] == Terrain.DOOR)
            //buff(Stealth.class).AlterStealth(NOISE_MOVE_OPENDOOR);
        else if(Dungeon.level.map[pos] == Terrain.HIGH_GRASS||Dungeon.level.map[pos]==Terrain.GRASS)
            //buff(Stealth.class).AlterStealth(Random.Int(NOISE_MOVE_ONGRASS,NOISE_MOVE_ONGROUND));
            buff(Noise.class).moveTograssNoise();
        else  buff(Noise.class).moveNoise();

        if(Dungeon.level.map[pos] == Terrain.HIGH_GRASS)
        Dungeon.hero.buff(Noise.class).pressNoise();

        if (getCloser(action.dst)) {
            justMoved = true;
            return true;
        } else {
            if (Dungeon.level.map[pos] == Terrain.SIGN) {
                Sign.read(pos);
            }
            ready();
            return false;
        }
    }

    private boolean actInteract(HeroAction.Interact action) {
        NPC npc = action.npc;
        if (Dungeon.level.adjacent(pos, npc.pos)) {
            ready();
            sprite.turnTo(pos, npc.pos);
            return npc.interact();
        } else {
            if (fieldOfView[npc.pos] && getCloser(npc.pos)) {
                return true;
            } else {
                ready();
                return false;
            }
        }
    }

    private boolean actBuy(HeroAction.Buy action) {

        buff(Noise.class).fade();

        int dst = action.dst;
        if (pos == dst || Dungeon.level.adjacent(pos, dst)) {
            ready();
            Heap heap = Dungeon.level.heaps.get(dst);
            if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
                GameScene.show(new WndTradeItem(heap, true));
            }
            return false;
        } else if (getCloser(dst)) {
            return true;
        } else {
            ready();
            return false;
        }
    }

    private boolean actCook(HeroAction.Cook action) {
        int dst = action.dst;
        if (Dungeon.visible[dst]) {
            ready();
            AlchemyPot.operate(this, dst);
            return false;
        } else if (getCloser(dst)) {
            return true;
        } else {
            ready();
            return false;
        }
    }

    private boolean actPickUp(HeroAction.PickUp action) {
        int dst = action.dst;
        if (pos == dst) {
            Heap heap = Dungeon.level.heaps.get(pos);
            if (heap != null) {
                Item item = heap.peek();
                if (item.doPickUp(this)) {
                    heap.pickUp();
                    if (item instanceof Dewdrop
                            || item instanceof TimekeepersHourglass.sandBag
                            || item instanceof DriedRose.Petal
                            || item instanceof Key) {
                        //Do Nothing
                    } else {
                        boolean important =
                                ((item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion) && ((Scroll) item).isKnown()) ||
                                        ((item instanceof PotionOfStrength || item instanceof PotionOfMight) && ((Potion) item).isKnown());
                        if (important) {
                            GLog.p(Messages.get(this, "you_now_have", item.name()));
                        } else {
                            GLog.i(Messages.get(this, "you_now_have", item.name()));
                        }
                    }
                    if (!heap.isEmpty()) {
                        GLog.i(Messages.get(this, "something_else"));
                    }
                    curAction = null;
                    buff(Noise.class).pickNoise();
                    spendAndNext( TIME_TO_PICK_UP );
                } else {
                    heap.sprite.drop();
                    ready();
                }
            } else {
                ready();
            }
            return false;
        } else if (getCloser(dst)) {
            return true;
        } else {
            ready();
            return false;
        }
    }

    private boolean actOpenChest(HeroAction.OpenChest action) {
        int dst = action.dst;
        if (Dungeon.level.adjacent(pos, dst) || pos == dst) {
            Heap heap = Dungeon.level.heaps.get(dst);
            if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {
                if ((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.depth)) < 1)
                        || (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.depth)) < 1)){
                    GLog.w(Messages.get(this, "locked_chest"));
                    ready();
                    return false;
                }
                switch (heap.type) {
                    case TOMB:
                        Sample.INSTANCE.play(Assets.SND_TOMB);
                        Camera.main.shake(1, 0.5f);
                        break;
                    case SKELETON:
                    case REMAINS:
                        break;
                    default:
                        Sample.INSTANCE.play(Assets.SND_UNLOCK);
                }
                buff(Noise.class).openChestNoise();
                spend(Key.TIME_TO_UNLOCK);
                sprite.operate(dst);
            } else {
                ready();
            }
            return false;
        } else if (getCloser(dst)) {
            return true;
        } else {
            ready();
            return false;
        }
    }

    private boolean actUnlock(HeroAction.Unlock action) {
        int doorCell = action.dst;
        if (Dungeon.level.adjacent(pos, doorCell)) {
            boolean hasKey = false;
            int door = Dungeon.level.map[doorCell];
            if (door == Terrain.LOCKED_DOOR
                    && Notes.keyCount(new IronKey(Dungeon.depth)) > 0) {
                hasKey = true;
            } else if (door == Terrain.LOCKED_EXIT
                    && Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0) {
                hasKey = true;
            }
            if (hasKey) {
                spend(Key.TIME_TO_UNLOCK);
                sprite.operate(doorCell);
                Sample.INSTANCE.play(Assets.SND_UNLOCK);
                buff(Noise.class).fade();
            } else {
                GLog.w(Messages.get(this, "locked_door"));
                ready();
            }
            return false;
        } else if (getCloser(doorCell)) {
            return true;
        } else {
            ready();
            return false;
        }
    }

    private boolean actDescend(HeroAction.Descend action) {
        int stairs = action.dst;
        if (pos == stairs && pos == Dungeon.level.exit) {

            curAction = null;

            Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                if (mob instanceof DriedRose.GhostHero) mob.destroy();

            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene(InterlevelScene.class);

            return false;

        } else if (getCloser(stairs)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAscend(HeroAction.Ascend action) {
        int stairs = action.dst;
        if (pos == stairs && pos == Dungeon.level.entrance) {

            if (Dungeon.depth == 1) {

                if (belongings.getItem(Amulet.class) == null) {
                    GameScene.show(new WndMessage(Messages.get(this, "leave")));
                    ready();
                } else {
                    Dungeon.win(Amulet.class);
                    Dungeon.deleteGame(GamesInProgress.curSlot, true);
                    Game.switchScene(SurfaceScene.class);
                }

            } else {

                curAction = null;

                Hunger hunger = buff(Hunger.class);
                if (hunger != null && !hunger.isStarving()) {
                    hunger.reduceHunger(-Hunger.STARVING / 10);
                }

                Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
                if (buff != null) buff.detach();

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob instanceof DriedRose.GhostHero) mob.destroy();

                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene(InterlevelScene.class);
            }

            return false;

        } else if (getCloser(stairs)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAttack(HeroAction.Attack action) {
        enemy = action.target;
        if (enemy.isAlive() && canAttack(enemy) && !isCharmedBy(enemy)) {
            Invisibility.dispel();
            spend(attackDelay());
            sprite.attack(enemy.pos);
            return false;
        } else {
            if (fieldOfView[enemy.pos] && getCloser(enemy.pos)) {
                buff(Noise.class).attackNoise();
                return true;
            } else {
                ready();
                return false;
            }
        }
    }

    public Char enemy() {
        return enemy;
    }

    public void rest(boolean fullRest) {
        buff(Noise.class).fade();
        spendAndNext(TIME_TO_REST);
        if (!fullRest) {
            sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
        }
        resting = fullRest;
    }

    @Override
    public Damage attackProc(Char enemy, Damage damage) {
        KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.mainhandweapon;

        if(tamahawk!=null)
        {
            wep=tamahawk;
        }

        if (wep != null)
            damage = wep.proc(this, enemy, damage);

        switch (subClass) {
            case SNIPER:
                if (rangedWeapon != null) {
                    Buff.prolong(this, SnipersMark.class, attackDelay() * 1.1f).object = enemy.id();
                }
                break;
            default:
        }
        return damage;
    }

    public Damage mainhandattackproc(Char enemy,Damage damage)
    {
        return damage;
    }

    public Damage offhandattackProc(Char enemy, Damage damage) {

        KindOfWeapon wep = belongings.offhandweapon;

        if (wep != null)
            damage = wep.proc(this, enemy, damage);

        switch (subClass) {
            case SNIPER:
                if (rangedWeapon != null) {
                    Buff.prolong(this, SnipersMark.class, attackDelay() * 1.1f).object = enemy.id();
                }
                break;
            default:
        }
        return damage;
    }

    @Override
    public Damage defenseProc(Char enemy, Damage damage) {

        buff(Noise.class).defenseNoise();

        Earthroot.Armor armor = buff(Earthroot.Armor.class);
        if (armor != null) {
            damage = armor.absorb(damage);
        }

        Sungrass.Health health = buff(Sungrass.Health.class);
        if (health != null) {
            health.absorb(damage);
        }

        if (this.buff(Guard.class) != null && damage instanceof PhysicalDamage) {
            damage = buff(Guard.class).getblocked((PhysicalDamage) damage);
            this.sprite.showStatus(CharSprite.NEUTRAL, "格挡!");
        }

        if (this.belongings.offhandweapon instanceof Gauntlet && damage instanceof PhysicalDamage) {
            if (Random.Int(100) < ((Gauntlet) this.belongings.offhandweapon).BlockChance()) {
                damage = ((Gauntlet) this.belongings.offhandweapon).damageproc((PhysicalDamage) damage);
                this.sprite.showStatus(CharSprite.NEUTRAL, "招架!");
            }
        }


        if (belongings.armor != null) {
            damage = belongings.armor.proc(enemy, this, damage);
        }

        return damage;
    }

    @Override
    public void damage(Damage dmg, Object src) {
        if (buff(TimekeepersHourglass.timeStasis.class) != null)
            return;

        if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
            interrupt();
            resting = false;
        }

        if (this.buff(Drowsy.class) != null) {
            Buff.detach(this, Drowsy.class);
            GLog.w(Messages.get(this, "pain_resist"));
        }

        CapeOfThorns.Thorns thorns = buff(CapeOfThorns.Thorns.class);
        if (thorns != null) {
			/*
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null),  this);
			*///I haven't think up an idea for that,so capeofthorns has no use now
        }

        int tenacity = RingOfTenacity.getBonus(this, RingOfTenacity.Tenacity.class);
        if (tenacity != 0) //(HT - HP)/HT = heroes current % missing health.
            //dmg = (int)Math.ceil((float)dmg * Math.pow(0.85, tenacity*((float)(HT - HP)/HT)));//Alright,now ring of tenacity is useless too

            //TODO improve this when I have proper damage source logic
            if (belongings.armor != null && belongings.armor.hasGlyph(AntiMagic.class)
                    && RingOfElements.FULL.contains(src.getClass())) {
                //dmg -= Random.NormalIntRange(belongings.armor.DRMin(), belongings.armor.DRMax())/2;//Armor now provide armor instead of aromr...sounds strange
            }

        if (subClass == HeroSubClass.BERSERKER && berserk == null) {
            berserk = Buff.affect(this, Berserk.class);
        }

        super.damage(dmg, src);
    }

    public void checkVisibleMobs() {
        ArrayList<Mob> visible = new ArrayList<>();

        boolean newMob = false;

        Mob target = null;
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (fieldOfView[m.pos] && m.hostile) {
                visible.add(m);
                if (!visibleEnemies.contains(m)) {
                    newMob = true;
                }

                if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1) {
                    if (target == null) {
                        target = m;
                    } else if (distance(target) > distance(m)) {
                        target = m;
                    }
                }
            }
        }

        if (target != null && (QuickSlotButton.lastTarget == null ||
                !QuickSlotButton.lastTarget.isAlive() ||
                !fieldOfView[QuickSlotButton.lastTarget.pos] ||
                QuickSlotButton.lastTarget.pos > fieldOfView.length
                )) {
            QuickSlotButton.target(target);
        }

        if (target != null && (MainHandIndicator.lastTarget == null ||
                !MainHandIndicator.lastTarget.isAlive() ||
                !fieldOfView[MainHandIndicator.lastTarget.pos] ||
                MainHandIndicator.lastTarget.pos > fieldOfView.length
        )) {
            MainHandIndicator.target(target);
        }
        if (target != null && (OffHandIndicator.lastTarget == null ||
                !OffHandIndicator.lastTarget.isAlive() ||
                !fieldOfView[OffHandIndicator.lastTarget.pos] ||
                MainHandIndicator.lastTarget.pos > fieldOfView.length
                )) {
            OffHandIndicator.target(target);
        }
        if (newMob) {
            interrupt();
            resting = false;
        }

        visibleEnemies = visible;
    }

    public int visibleEnemies() {
        return visibleEnemies.size();
    }

    public Mob visibleEnemy(int index) {
        return visibleEnemies.get(index % visibleEnemies.size());
    }

    private boolean getCloser(final int target) {

        if (target == pos)
            return false;

        if (rooted) {
            Camera.main.shake(1, 1f);
            return false;
        }

        int step = -1;

        if (Dungeon.level.adjacent(pos, target)) {

            path = null;
            if (Actor.findChar(target) == null) {
                if (Dungeon.level.pit[target] && !flying && !Dungeon.level.solid[target]) {
                    if (!Chasm.jumpConfirmed) {
                        Chasm.heroJump(this);
                        interrupt();
                    } else {
                        Chasm.heroFall(target);
                    }
                    return false;
                }
                if (Dungeon.level.passable[target] || Dungeon.level.avoid[target]) {
                    step = target;
                }
            }

        } else {

            boolean newPath = false;
            if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
                newPath = true;
            else if (path.getLast() != target)
                newPath = true;
            else {
                //looks ahead for path validity, up to length-1 or 2.
                //Note that this is shorter than for mobs, so that mobs usually yield to the hero
                int lookAhead = (int) GameMath.gate(0, path.size() - 1, 2);
                for (int i = 0; i < lookAhead; i++) {
                    int cell = path.get(i);
                    if (!Dungeon.level.passable[cell] || (Dungeon.visible[cell] && Actor.findChar(cell) != null)) {
                        newPath = true;
                        break;
                    }
                }
            }

            if (newPath) {
                int len = Dungeon.level.length();
                boolean[] p = Dungeon.level.passable;
                boolean[] v = Dungeon.level.visited;
                boolean[] m = Dungeon.level.mapped;
                boolean[] passable = new boolean[len];
                for (int i = 0; i < len; i++) {
                    passable[i] = p[i] && (v[i] || m[i]);
                }
                path = Dungeon.findPath(this, pos, target, passable, fieldOfView);
            }
            if (path == null) return false;
            step = path.removeFirst();
        }

        if (step != -1) {
            int moveTime = 1;
            if (belongings.armor != null && belongings.armor.hasGlyph(Stone.class) &&
                    (Dungeon.level.map[pos] == Terrain.DOOR
                            || Dungeon.level.map[pos] == Terrain.OPEN_DOOR
                            || Dungeon.level.map[step] == Terrain.DOOR
                            || Dungeon.level.map[step] == Terrain.OPEN_DOOR)) {
                moveTime *= 2;
            }
            sprite.move(pos, step);
            move(step);
            spend(moveTime / speed());
            return true;
        } else {
            return false;
        }

    }

    public boolean handle(int cell) {

        if (cell == -1) {
            return false;
        }

        Char ch;
        Heap heap;

        if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {

            curAction = new HeroAction.Cook(cell);

        } else if (fieldOfView[cell] && (ch = Actor.findChar(cell)) instanceof Mob) {

            if (ch instanceof NPC) {
                curAction = new HeroAction.Interact((NPC) ch);
            } else {
                curAction = new HeroAction.Attack(ch);
            }

        } else if ((heap = Dungeon.level.heaps.get(cell)) != null
                //moving to an item doesn't auto-pickup when enemies are near...
                && (visibleEnemies.size() == 0 || cell == pos ||
                //...but only for standard heaps, chests and similar open as normal.
                (heap.type != Type.HEAP && heap.type != Type.FOR_SALE))) {

            switch (heap.type) {
                case HEAP:
                    curAction = new HeroAction.PickUp(cell);
                    break;
                case FOR_SALE:
                    curAction = heap.size() == 1 && heap.peek().price() > 0 ?
                            new HeroAction.Buy(cell) :
                            new HeroAction.PickUp(cell);
                    break;
                default:
                    curAction = new HeroAction.OpenChest(cell);
            }

        } else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {

            curAction = new HeroAction.Unlock(cell);

        } else if (cell == Dungeon.level.exit && Dungeon.depth < 26) {

            curAction = new HeroAction.Descend(cell);

        } else if (cell == Dungeon.level.entrance) {

            curAction = new HeroAction.Ascend(cell);

        } else {
            curAction = new HeroAction.Move(cell);

            lastAction = null;
        }

        return true;
    }

    public void earnExp(int exp) {

        this.exp += exp;
        float percent = exp / (float) maxExp();

        EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
        if (chains != null) chains.gainExp(percent);

        HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
        if (horn != null) horn.gainCharge(percent);

        NinjaProsthesis.passivebuff passivebuff = buff(NinjaProsthesis.passivebuff.class);
        if (passivebuff != null) passivebuff.gainCharge(percent);

        if (subClass == HeroSubClass.BERSERKER) {
            berserk = Buff.affect(this, Berserk.class);
            berserk.recover(percent);
        }

        boolean levelUp = false;
        while (this.exp >= maxExp()) {
            this.exp -= maxExp();
            if (lvl < MAX_LEVEL) {
                lvl++;
                levelUp = true;

                HT += 5;
                HP += 5;
                attackSkill++;
                defenseSkill++;

            } else {
                Buff.prolong(this, Bless.class, 30f);
                this.exp = 0;

                GLog.p(Messages.get(this, "level_cap"));
                Sample.INSTANCE.play(Assets.SND_LEVELUP);
            }

            if (lvl < 10) {
                updateAwareness();
            }
        }

        if (levelUp) {

            GLog.p(Messages.get(this, "new_level"), lvl);
            sprite.showStatus(CharSprite.POSITIVE, Messages.get(Hero.class, "level_up"));
            Sample.INSTANCE.play(Assets.SND_LEVELUP);

            Badges.validateLevelReached();
        }
    }

    public static int maxExp(int lvl) {
        return 5 + lvl * 5;
    }

    public int maxExp() {
        return maxExp( lvl );
    }

    void updateAwareness() {
        awareness = (float) (1 - Math.pow(
                (heroClass == HeroClass.ROGUE ? 0.85 : 0.90),
                (1 + Math.min(lvl, 9)) * 0.5
        ));
    }

    public boolean isStarving() {
        return buff(Hunger.class) != null && buff(Hunger.class).isStarving();
    }

    @Override
    public void add(Buff buff) {

        if (buff(TimekeepersHourglass.timeStasis.class) != null)
            return;

        super.add(buff);

        if (sprite != null) {
            String msg = buff.heroMessage();
            if (msg != null) {
                GLog.w(msg);
            }

            if (buff instanceof Paralysis || buff instanceof Vertigo) {
                interrupt();
            }

        }

        BuffIndicator.refreshHero();
    }

    @Override
    public void remove(Buff buff) {
        super.remove(buff);

        BuffIndicator.refreshHero();
    }



    @Override
    public int stealth() {
        //int stealth = super.stealth();
        //stealth += RingOfEvasion.getBonus(this, RingOfEvasion.Evasion.class);
        //if (belongings.armor != null && belongings.armor.hasGlyph(Obfuscation.class)) {
        //    stealth += belongings.armor.level();
        //}
        return 0;
    }

    @Override
    public void die(Object cause) {

        curAction = null;

        Ankh ankh = null;

        //look for ankhs in player inventory, prioritize ones which are blessed.
        for (Item item : belongings) {
            if (item instanceof Ankh) {
                if (ankh == null || ((Ankh) item).isBlessed()) {
                    ankh = (Ankh) item;
                }
            }
        }

        if (ankh != null && ankh.isBlessed()) {
            this.HP = HT / 4;

            //ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
            Buff.detach(this, Paralysis.class);
            spend(-cooldown());

            new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
            CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

            ankh.detach(belongings.backpack);

            Sample.INSTANCE.play(Assets.SND_TELEPORT);
            GLog.w(Messages.get(this, "revive"));
            Statistics.ankhsUsed++;

            return;
        }

        Actor.fixTime();
        super.die(cause);

        if (ankh == null) {

            reallyDie(cause);

        } else {

            Dungeon.deleteGame(GamesInProgress.curSlot, false);
            GameScene.show(new WndResurrect(ankh, cause));

        }
    }

    @Override
    public boolean isAlive() {
        if (subClass == HeroSubClass.BERSERKER
                && berserk != null
                && berserk.berserking()) {
            return true;
        }
        return super.isAlive();
    }

    @Override
    public void move(final int step) {
        int moveTime = 1;
        if (belongings.armor != null && belongings.armor.hasGlyph(Stone.class) &&
                (Dungeon.level.map[pos] == Terrain.DOOR
                        || Dungeon.level.map[pos] == Terrain.OPEN_DOOR
                        || Dungeon.level.map[step] == Terrain.DOOR
                        || Dungeon.level.map[step] == Terrain.OPEN_DOOR)) {
            moveTime *= 2;
        }
        if (belongings.offhandweapon instanceof SubmachineGun)
        {
            if(((SubmachineGun) belongings.offhandweapon).check()) {
                Actor.addDelayed(new SubmachineGun.VirtualActor(this,step,step-pos+step), -1);
            }
            else
            { reloadfirearm(-moveTime / speed()); }
        }
        else
        {
            //FIXME:It will conduct much more unnecessary code if I try to paste it in all other action,so I try to just let firearm gain sometime to balance it's consume in next scentence
            reloadfirearm(-moveTime / speed());
        }
        super.move(step);
        if (!flying) {
            if (Dungeon.level.water[pos]) {
                Sample.INSTANCE.play(Assets.SND_WATER, 1, 1, Random.Float(0.8f, 1.25f));
            } else {
                Sample.INSTANCE.play(Assets.SND_STEP);
            }
        }

        Heap heap = Dungeon.level.heaps.get( step );
        if(
                heap != null && heap.type == Type.HEAP && heap.peek() != null
                        && heap.peek() instanceof Tamahawk
        )
        {
            Item item = heap.peek();
            if(item instanceof Tamahawk)
            {
                if(hero.belongings.mainhandweapon==null)
                {
                    item= heap.pickUp();
                    item.detachAll(hero.belongings.backpack);
                    hero.belongings.mainhandweapon = (Tamahawk)item;
                    ((Tamahawk) item).activate(hero);
                    item.updateQuickslot();
                    GameScene.scene.updateweaponindicator((Weapon)item,true);
                }
                else if(hero.belongings.offhandweapon==null && !(hero.belongings.mainhandweapon instanceof TwoHandedWeapon))
                {
                    item= heap.pickUp();
                    item.detachAll(hero.belongings.backpack);
                    hero.belongings.offhandweapon = (Tamahawk)item;
                    Buff.affect(Dungeon.hero, CombinationCoolDown.class);
                    Dungeon.hero.buff(CombinationCoolDown.class).set(hero.belongings.offhandweapon.cooldown());
                    item.updateQuickslot();
                    GameScene.scene.updateweaponindicator((Weapon)item,false);
                }
                else if (item.doPickUp(this)) {
                   heap.pickUp();
                 }
            }
        }
    }
    @Override
    public void onMotionComplete() {
        //Dungeon.observe();
        //search(false);
    }

    @Override
    public void onAttackComplete() {
        AttackIndicator.target(enemy);
        boolean hit = attack(enemy);
        if (subClass == HeroSubClass.GLADIATOR) {
            if (hit) {
                Buff.affect(this, Combo.class).hit();
            } else {
                Combo combo = buff(Combo.class);
                if (combo != null) combo.miss();
            }
        }
        curAction = null;
        super.onAttackComplete();
    }

    @Override
    public void onOperateComplete() {

        if (curAction instanceof HeroAction.Unlock) {

            int doorCell = ((HeroAction.Unlock) curAction).dst;
            int door = Dungeon.level.map[doorCell];

            if (door == Terrain.LOCKED_DOOR) {
                Notes.remove(new IronKey(Dungeon.depth));
                Level.set(doorCell, Terrain.DOOR);
            } else {
                Notes.remove(new SkeletonKey(Dungeon.depth));
                Level.set(doorCell, Terrain.UNLOCKED_EXIT);
            }
            GameScene.updateKeyDisplay();

            Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
            GameScene.updateMap(doorCell);

        } else if (curAction instanceof HeroAction.OpenChest) {

            Heap heap = Dungeon.level.heaps.get(((HeroAction.OpenChest) curAction).dst);
            if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
                Sample.INSTANCE.play( Assets.SND_BONES );
            } else if (heap.type == Type.LOCKED_CHEST){
                Notes.remove(new GoldenKey(Dungeon.depth));
            } else if (heap.type == Type.CRYSTAL_CHEST){
                Notes.remove(new CrystalKey(Dungeon.depth));
            }
            GameScene.updateKeyDisplay();
            heap.open(this);
        }
        curAction = null;

        super.onOperateComplete();
    }

    public boolean search(boolean intentional) {

        boolean smthFound = false;

        int positive = 0;
        int negative = 0;

        int distance = 1 + positive + negative;

        float level = intentional ? (2 * awareness - awareness * awareness) : awareness;
        if (distance <= 0) {
            level /= 2 - distance;
            distance = 1;
        }

        int cx = pos % Dungeon.level.width();
        int cy = pos / Dungeon.level.width();
        int ax = cx - distance;
        if (ax < 0) {
            ax = 0;
        }
        int bx = cx + distance;
        if (bx >= Dungeon.level.width()) {
            bx = Dungeon.level.width() - 1;
        }
        int ay = cy - distance;
        if (ay < 0) {
            ay = 0;
        }
        int by = cy + distance;
        if (by >= Dungeon.level.height()) {
            by = Dungeon.level.height() - 1;
        }

        TalismanOfForesight.Foresight foresight = buff(TalismanOfForesight.Foresight.class);

        //cursed talisman of foresight makes unintentionally finding things impossible.
        if (foresight != null && foresight.isCursed()) {
            level = -1;
        }

        for (int y = ay; y <= by; y++) {
            for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {

                if (Dungeon.visible[p]) {

                    if (intentional) {
                        sprite.parent.addToBack(new CheckedCell(p));
                    }

                    if (Dungeon.level.secret[p] && (intentional || Random.Float() < level)) {

                        int oldValue = Dungeon.level.map[p];

                        GameScene.discoverTile(p, oldValue);

                        Dungeon.level.discover(p);

                        ScrollOfMagicMapping.discover(p);

                        smthFound = true;

                        if (foresight != null && !foresight.isCursed())
                            foresight.charge();
                    }
                }
            }
        }


        if (intentional) {
            sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "search"));
            sprite.operate(pos);
            if (foresight != null && foresight.isCursed()) {
                GLog.n(Messages.get(this, "search_distracted"));
                spendAndNext(TIME_TO_SEARCH * 3);
            } else {
                spendAndNext(TIME_TO_SEARCH);
            }

        }

        if (smthFound) {
            GLog.w(Messages.get(this, "noticed_smth"));
            Sample.INSTANCE.play(Assets.SND_SECRET);
            interrupt();
        }

        return smthFound;
    }

    public void resurrect(int resetLevel) {

        HP = HT;
        Dungeon.gold = 0;
        exp = 0;

        belongings.resurrect(resetLevel);

        live();
    }

    @Override
    public HashSet<Class<?>> resistances() {
        RingOfElements.Resistance r = buff(RingOfElements.Resistance.class);
        return r == null ? super.resistances() : r.resistances();
    }

    @Override
    public HashSet<Class<?>> immunities() {
        HashSet<Class<?>> immunities = new HashSet<Class<?>>();
        for (Buff buff : buffs()) {
            for (Class<?> immunity : buff.immunities)
                immunities.add(immunity);
        }
        return immunities;
    }

    @Override
    public void next() {
        if (isAlive())
            super.next();
    }

    public interface Doom {
        void onDeath();
    }

    public boolean checkvisible(Char ch)
    {
        if(visibleEnemies.contains(ch))
            return true;
        else return false;
    }

    public int stealthLevel()
    {
        int stealth = (weaponStealth()+armorStealth())*(this.DEX+4)-15;

        if(stealth<5)
            return 1;
        else if(stealth<20)
            return 2;
        else if(stealth<35)
            return 3;
        else if(stealth<50)
            return 4;
        else
            return 5;
    }

    public int weaponStealth()
    {
        if(this.belongings.mainhandweapon!=null)
        {
            return this.belongings.mainhandweapon.stealth();
        }
        else return 3;
    }

    public int armorStealth()
    {
        if(this.belongings.armor!=null)
        {
            return this.belongings.armor.stealth();
        }
        else return 3;
    }


}
