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
package com.teller.pixeldungeonofteller.actors;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Bless;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Charm;
import com.teller.pixeldungeonofteller.actors.buffs.Chill;
import com.teller.pixeldungeonofteller.actors.buffs.Cripple;
import com.teller.pixeldungeonofteller.actors.buffs.CursedFlame;
import com.teller.pixeldungeonofteller.actors.buffs.EarthImbue;
import com.teller.pixeldungeonofteller.actors.buffs.FireImbue;
import com.teller.pixeldungeonofteller.actors.buffs.Frost;
import com.teller.pixeldungeonofteller.actors.buffs.MagicalSleep;
import com.teller.pixeldungeonofteller.actors.buffs.Ooze;
import com.teller.pixeldungeonofteller.actors.buffs.Paralysis;
import com.teller.pixeldungeonofteller.actors.buffs.ShieldRecharging;
import com.teller.pixeldungeonofteller.actors.buffs.Slow;
import com.teller.pixeldungeonofteller.actors.buffs.Speed;
import com.teller.pixeldungeonofteller.actors.buffs.Vertigo;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.hero.HeroSubClass;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.HiddenBlade;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.levels.features.Door;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public abstract class Char extends Actor {

    protected static final String POS = "pos";
    protected static final String TAG_HP = "HP";
    protected static final String TAG_HT = "HT";
    protected static final String TAG_SHLD = "SHLD";
    protected static final String TAG_MAXSHLD = "MAXSHLD";
    protected static final String TAG_ARMOR = "ARMOR";
    protected static final String BUFFS = "buffs";
    private static final HashSet<Class<?>> EMPTY = new HashSet<>();
    public int pos = 0;
    public CharSprite sprite;
    public String name = "mob";
    public int HT;
    public int HP;
    public int ARMOR;
    public int MAXARMOR;
    public int SlashThreshold = 0;
    public int SHLD;
    public int MAXSHLD;
    public int TOPSHLD;
    public int paralysed = 0;
    public boolean rooted = false;
    public boolean flying = false;
    public int invisible = 0;
    public int viewDistance = 8;
    protected float baseSpeed = 1;
    protected PathFinder.Path path;
    protected HashSet<Property> properties = new HashSet<>();
    private HashSet<Buff> buffs = new HashSet<>();

    public boolean[] fieldOfView = null;

    public static boolean hit(Char attacker, Char defender, boolean magic) {
        float acuRoll = Random.Float(attacker.attackSkill(defender));
        float defRoll = Random.Float(defender.defenseSkill(attacker));
        if (attacker.buff(Bless.class) != null) acuRoll *= 1.20f;
        if (defender.buff(Bless.class) != null) defRoll *= 1.20f;
        return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
    }

    @Override
    protected boolean act() {
        Buff.affect(this, ShieldRecharging.class);
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
            fieldOfView = new boolean[Dungeon.level.length()];
        }
        Dungeon.level.updateFieldOfView( this, fieldOfView );
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        bundle.put(POS, pos);
        bundle.put(TAG_HP, HP);
        bundle.put(TAG_HT, HT);
        bundle.put(TAG_SHLD, SHLD);
        bundle.put(TAG_MAXSHLD, MAXSHLD);
        bundle.put(TAG_ARMOR, ARMOR);
        bundle.put(BUFFS, buffs);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

        super.restoreFromBundle(bundle);

        pos = bundle.getInt(POS);
        HP = bundle.getInt(TAG_HP);
        HT = bundle.getInt(TAG_HT);
        ARMOR = bundle.getInt(TAG_ARMOR);
        SHLD = bundle.getInt(TAG_SHLD);
        MAXSHLD = bundle.getInt(TAG_MAXSHLD);
        if (this instanceof Hero) {
            if (((Hero) this).belongings.armor != null) {
                ((Hero) this).belongings.armor.armor = ARMOR;
                ((Hero) this).belongings.armor.shld = SHLD;
            }
        }

        for (Bundlable b : bundle.getCollection(BUFFS)) {
            if (b != null) {
                ((Buff) b).attachTo(this);
            }
        }
    }

    public boolean attack(Char enemy) {
        if (enemy == null || !enemy.isAlive()) return false;
        boolean visibleFight = Dungeon.visible[pos] || Dungeon.visible[enemy.pos];

        if (enemy instanceof Hero) {
            if (((Hero) enemy).belongings.offhandweapon instanceof HiddenBlade) {
                if (Random.Int(100) < 12 + ((Hero) enemy).DEX() * 3) {
                    this.damage(((Hero) enemy).belongings.offhandweapon.damageRoll(hero), enemy);
                    enemy.sprite.showStatus(CharSprite.NEUTRAL, "袖剑反击");
                }
            }
        }

        if (!this.isAlive()) {
            return false;
        }

        if (hit(this, enemy, false)) {
            // FIXME
            int dr = this instanceof Hero && ((Hero) this).rangedWeapon != null && ((Hero) this).subClass ==
                    HeroSubClass.SNIPER ? 0 : enemy.drRoll();

            Damage dmg = damageRoll();
            dmg = attackProc(enemy, dmg);
            dmg = enemy.defenseProc(this, dmg);

            if (visibleFight) {
                Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
            }
            // If the enemy is already dead, interrupt the attack.
            // This matters as defence procs can sometimes inflict self-damage, such as armor glyphs.

            if (!enemy.isAlive()) {
                return true;
            }

            enemy.before_damage(dmg,this);
            enemy.damage(dmg, this);
            enemy.after_damage(dmg,this);

            int effectiveDamage = Math.max(dmg.effictivehpdamage - dr, 0);
            if (buff(FireImbue.class) != null)
                buff(FireImbue.class).proc(enemy);
            if (buff(EarthImbue.class) != null)
                buff(EarthImbue.class).proc(enemy);
            enemy.sprite.bloodBurstA(sprite.center(), effectiveDamage);
            enemy.sprite.flash();
            //TODO: consider revisiting this and shaking in more cases.
            float shake = 0f;
            if (enemy == hero)
                shake = effectiveDamage / (enemy.HT / 4);
            if (shake > 1f)
                Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
            if (!enemy.isAlive() && visibleFight) {
                if (enemy == hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name)));
                } else if (this == hero) {
                    GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                }
            }
            return true;
        } else {
            if (visibleFight) {
                String defense = enemy.defenseVerb();
                enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                Sample.INSTANCE.play(Assets.SND_MISS);
            }
            return false;
        }
    }

    public void before_damage(Damage damage,Char enemy)
    {
        //not to much thing to do for now,but make the damage process a little bit easier to understand and use is a necessary thing need to be done,not now although
    }

    public void after_damage(Damage damage,Char enemy)
    {
        if(this.isAlive()) {
            if (damage.tamahaawk) {
                GLog.w(String.valueOf(damage.effictivehpdamage));
                if (damage.effictivehpdamage > 0) {
                    Buff.affect(this, Bleeding.class).set(damage.effictivehpdamage);
                }
                damage.tamahaawk=false;
            }
        }
    }


    public int attackSkill(Char target) {
        return 0;
    }

    public int defenseSkill(Char enemy) {
        return 0;
    }

    public String defenseVerb() {
        return Messages.get(this, "def_verb");
    }

    public int drRoll() {
        return 0;
    }

    public Damage damageRoll() {
        return new Damage();
    }

    public Damage attackProc(Char enemy, Damage damage) {
        return damage;
    }

    public Damage defenseProc(Char enemy, Damage damage) {
        return damage;
    }

    public float speed() {
        return buff(Cripple.class) == null ? baseSpeed : baseSpeed * 0.5f;
    }



    public void damage(Damage dmg, Object src) {
        if (!isAlive() || !dmg.useful()) {
            return;
        }
        if (this.buff(Frost.class) != null) {
            Buff.detach(this, Frost.class);
        }
        if (this.buff(MagicalSleep.class) != null) {
            Buff.detach(this, MagicalSleep.class);
        }

        Class<?> srcClass = src.getClass();
        if (immunities().contains(srcClass)) {
            //dmg = 0;
        } else if (resistances().contains(srcClass)) {
            //dmg = Random.IntRange( 0, dmg );
        }


        //FIXME: when I add proper damage properties, should add an IGNORES_SHIELDS property to use here.
        //if (src instanceof Hunger || SHLD == 0){
        //	HP -= dmg;
        //} else if (SHLD >= dmg){
        //	SHLD -= dmg;
        //} else if (SHLD > 0) {
        //	HP -= (dmg - SHLD);
        //	SHLD = 0;
        //}

        if (dmg instanceof AbsoluteDamage)//deals absolutedamage first
        {
            if(this instanceof Hero)
            {
                if (HP > 0) {
                    int BHP = HP;
                    HP -= ((AbsoluteDamage) dmg).damage;
                    if (HP < 0) HP = 0;
                    dmg.effictivehpdamage += BHP - HP;
                }
            }
            else
            {
                if (SHLD> 0) {
                    int BSHLD = SHLD;
                    SHLD -= ((AbsoluteDamage) dmg).damage;
                    if (SHLD < 0) SHLD = 0;
                    dmg.effictiveslddamage += BSHLD - SHLD;
                }
                if (((AbsoluteDamage) dmg).damage > 0) {
                    int BHP = HP;
                    HP -= ((AbsoluteDamage) dmg).damage;
                    if (HP < 0) HP = 0;
                    dmg.effictivehpdamage += BHP - HP;
                } else ((AbsoluteDamage) dmg).damage = 0;
            }
            damageconsult(dmg, this);
            if (!isAlive()) {
                die(src);
                return;
            }
        } else if (dmg instanceof PhysicalDamage) {
            //int indicator=Random.Int(6);//I want to let 3 types of damage do in random,but it looks too foolish,but a solid style may have a bad reflect
            //whatever,now physical damage will settle in the order of:impact---puncture---slash(which will be deadly in most times)
            //impact against shield first
            if(HT>0) {
                if (SHLD > 0) {
                    int BSHLD = SHLD;
                    if (((PhysicalDamage) dmg).IMPACTDAMAGE > 0) {
                        int BESHLD = SHLD;
                        SHLD -= Math.ceil(((PhysicalDamage) dmg).IMPACTDAMAGE * 1.5f);
                        if (SHLD < 0) {
                            SHLD = 0;
                        }
                        ((PhysicalDamage) dmg).IMPACTDAMAGE -= (BESHLD - SHLD) / 1.5f;
                    }//impact can damage shield in a high rate
                    if (SHLD < 0) {
                        SHLD = 0;
                    } else {
                        if (((PhysicalDamage) dmg).PUNCTUREDAMAGE > 0) {
                            int BESHLD = SHLD;
                            SHLD -= Math.floor(((PhysicalDamage) dmg).PUNCTUREDAMAGE) * 0.6f;
                            if (SHLD < 0) {
                                SHLD = 0;
                            }
                            ((PhysicalDamage) dmg).PUNCTUREDAMAGE -= (BESHLD - SHLD) / 0.6f;
                        }//shield is very effective when handling this,in compensate for the order,may be changed later.
                        if (SHLD < 0) {
                            SHLD = 0;
                        } else {
                            if (((PhysicalDamage) dmg).SLASHDAMAGE > 0) {
                                int BESHLD = SHLD;
                                SHLD -= ((PhysicalDamage) dmg).SLASHDAMAGE * 0.8f;
                                if (SHLD < 0) {
                                    SHLD = 0;
                                }
                                ((PhysicalDamage) dmg).SLASHDAMAGE -= (BESHLD - SHLD) / 0.8f;
                            }
                        }
                    }
                    dmg.effictiveslddamage += BSHLD - SHLD;
                } else SHLD = 0;
                //------------------------------------
                ArmorHandle((PhysicalDamage) dmg);
            }
            else
            {
                ArmorHandle((PhysicalDamage) dmg);
                if (SHLD > 0) {
                    int BSHLD = SHLD;
                    if (((PhysicalDamage) dmg).IMPACTDAMAGE > 0) {
                        int BESHLD = SHLD;
                        SHLD -= Math.ceil(((PhysicalDamage) dmg).IMPACTDAMAGE * 1.5f);
                        if (SHLD < 0) {
                            SHLD = 0;
                        }
                        ((PhysicalDamage) dmg).IMPACTDAMAGE -= (BESHLD - SHLD) / 1.5f;
                    }//impact can damage shield in a high rate
                    if (SHLD < 0) {
                        SHLD = 0;
                    } else {
                        if (((PhysicalDamage) dmg).PUNCTUREDAMAGE > 0) {
                            int BESHLD = SHLD;
                            SHLD -= Math.floor(((PhysicalDamage) dmg).PUNCTUREDAMAGE) * 0.6f;
                            if (SHLD < 0) {
                                SHLD = 0;
                            }
                            ((PhysicalDamage) dmg).PUNCTUREDAMAGE -= (BESHLD - SHLD) / 0.6f;
                        }//shield is very effective when handling this,in compensate for the order,may be changed later.
                        if (SHLD < 0) {
                            SHLD = 0;
                        } else {
                            if (((PhysicalDamage) dmg).SLASHDAMAGE > 0) {
                                int BESHLD = SHLD;
                                SHLD -= ((PhysicalDamage) dmg).SLASHDAMAGE * 0.8f;
                                if (SHLD < 0) {
                                    SHLD = 0;
                                }
                                ((PhysicalDamage) dmg).SLASHDAMAGE -= (BESHLD - SHLD) / 0.8f;
                            }
                        }
                    }
                    dmg.effictiveslddamage += BSHLD - SHLD;
                } else SHLD = 0;
                if (!isAlive()) {
                    die(src);
                    return;
                }
            }
            //------------------------------------
            int BHP = HP;//in order to get how much damage has been caused,which will be used in many ways
            if(HP>0) {
                if (((PhysicalDamage) dmg).IMPACTDAMAGE > 0) {
                    HP -= Math.floor(((PhysicalDamage) dmg).IMPACTDAMAGE) * 0.8f;
                }//shield is very effective when handling this,in compensate for the order,may be changed later.
                if (HP < 0) {
                    HP = 0;
                } else {
                    if (((PhysicalDamage) dmg).PUNCTUREDAMAGE > 0) {
                        HP -= Math.floor(((PhysicalDamage) dmg).PUNCTUREDAMAGE) * 0.9f;
                    }//shield is very effective when handling this,in compensate for the order,may be changed later.
                    if (HP < 0) {
                        HP = 0;
                    } else {
                        if (((PhysicalDamage) dmg).SLASHDAMAGE > 0) {
                            HP -= ((PhysicalDamage) dmg).SLASHDAMAGE * 1.5f;
                            if (HP < 0) {
                                HP = 0;
                            }
                        }
                    }
                }
                if (HP < 0) HP = 0;
                dmg.effictivehpdamage += BHP - HP;
            }else { HP=0;}
            //----------------------------------------------
            //next,puncture against armor
            damageconsult(dmg, this);
            if (!isAlive()) {
                die(src);
                return;
            }
        } else if (dmg instanceof MagicalDamage) {
            if (((MagicalDamage) dmg).hassingleelement())//to magicaldamage,it will remix first
            {
                ((MagicalDamage) dmg).remix();
            }
            if (((MagicalDamage) dmg).hasremixedelement())//remixdamage first
            {
                if (((MagicalDamage) dmg).STEAMDAMAGE > 0) {
                    damageconsult(dmg, this);
                    if (!isAlive()) {
                        die(src);
                        return;
                    }
                }
                if (((MagicalDamage) dmg).CURSEDFLAMEDAMAGE > 0) {
                    int BSHLD = SHLD;
                    if (SHLD > 0) {
                        SHLD -= ((MagicalDamage) dmg).CURSEDFLAMEDAMAGE * 0.8;
                        if (SHLD < 0) SHLD = 0;
                        dmg.effictiveslddamage += BSHLD - SHLD;
                    } else {
                            BSHLD = 1;
                            int BHP = HP;
                            HP -= ((MagicalDamage) dmg).CURSEDFLAMEDAMAGE;
                            if (HP < 0) HP = 0;
                            dmg.effictivehpdamage += BHP - HP;
                        if (Random.Float() > SHLD / BSHLD)
                            Buff.affect(this, CursedFlame.class);
                    }
                    damageconsult(dmg, this);
                    if (!isAlive()) {
                        die(src);
                        return;
                    }
                }
                if (((MagicalDamage) dmg).MAGNETICDAMAGE > 0) {
                    int BSHLD = SHLD;
                    if (((MagicalDamage) dmg).MAGNETICDAMAGE < SHLD) {
                        if (Random.Float() > ((MagicalDamage) dmg).MAGNETICDAMAGE / SHLD) {
                            SHLD -= ((MagicalDamage) dmg).MAGNETICDAMAGE * 4;
                            if (SHLD < 0) SHLD = 0;
                        } else
                            SHLD = 0;
                    } else {
                        SHLD = 0;
                    }
                    dmg.effictiveslddamage += BSHLD - SHLD;
                    damageconsult(dmg, this);
                    if (!isAlive()) {
                        die(src);
                        return;
                    }
                }
                if (((MagicalDamage) dmg).SLURRYDAMAGE > 0) {
                    int BSHLD = 1;
                    if (SHLD > 0) {
                        BSHLD = SHLD;
                        SHLD -= ((MagicalDamage) dmg).SLURRYDAMAGE * 0.8;
                        dmg.effictiveslddamage += BSHLD - SHLD;
                    }
                    if (Random.Float() < SHLD / BSHLD)
                        Buff.affect(this, Slow.class, SHLD - BSHLD);
                    damageconsult(dmg, this);
                    if (!isAlive()) {
                        die(src);
                        return;
                    }
                }
                if (((MagicalDamage) dmg).HOLLOWDAMAGE > 0) {
                    int BHP = HP;
                    HP -= ((MagicalDamage) dmg).HOLLOWDAMAGE;
                    if (HP < 0) HP = 0;
                    dmg.effictivehpdamage += BHP - HP;
                    damageconsult(dmg, this);
                    if (!isAlive()) {
                        die(src);
                        return;
                    }
                }
                if (((MagicalDamage) dmg).CORROSIVEDAMAGE > 0) {
                    Buff.affect(this, Ooze.class);
                    if (SHLD > 0) {
                        int BSHLD = SHLD;
                        SHLD -= ((MagicalDamage) dmg).CORROSIVEDAMAGE * 0.8;
                        if (SHLD < 0) SHLD = 0;
                        dmg.effictiveslddamage += BSHLD - SHLD;
                    } else {
                        int BHP = HP;
                        HP -= ((MagicalDamage) dmg).CORROSIVEDAMAGE;
                        if (HP < 0) HP = 0;
                        dmg.effictivehpdamage += BHP - HP;
                    }
                }
            }
            if (((MagicalDamage) dmg).hassingleelement())//then singleelement
            {
                int BARMOR = ARMOR;
                int BSHLD = SHLD;
                int BHP = HP;
                if (((MagicalDamage) dmg).FIREDAMAGE > 0) {
                    this.getmagicaldamage((MagicalDamage) dmg, ((MagicalDamage) dmg).FIREDAMAGE);
                    dmg.effictivehpdamage = BHP - HP;
                    dmg.effictiveslddamage = BSHLD - SHLD;
                    dmg.effictivearmordamage = BARMOR - ARMOR;
                }
                if (((MagicalDamage) dmg).ICEDAMAGE > 0) {
                    this.getmagicaldamage((MagicalDamage) dmg, ((MagicalDamage) dmg).ICEDAMAGE);
                    dmg.effictivehpdamage = BHP - HP;
                    dmg.effictiveslddamage = BSHLD - SHLD;
                    dmg.effictivearmordamage = BARMOR - ARMOR;
                }
                if (((MagicalDamage) dmg).HOLYDAMAGE > 0) {
                    this.getmagicaldamage((MagicalDamage) dmg, ((MagicalDamage) dmg).HOLYDAMAGE);
                    dmg.effictivehpdamage = BHP - HP;
                    dmg.effictiveslddamage = BSHLD - SHLD;
                    dmg.effictivearmordamage = BARMOR - ARMOR;
                }
                if (((MagicalDamage) dmg).SHADOWDAMAGE > 0) {
                    this.getmagicaldamage((MagicalDamage) dmg, ((MagicalDamage) dmg).SHADOWDAMAGE);
                    dmg.effictivehpdamage = BHP - HP;
                    dmg.effictiveslddamage = BSHLD - SHLD;
                    dmg.effictivearmordamage = BARMOR - ARMOR;
                }
                if (((MagicalDamage) dmg).LIGHTNINGDAMAGE > 0) {
                    this.getmagicaldamage((MagicalDamage) dmg, ((MagicalDamage) dmg).LIGHTNINGDAMAGE);
                    dmg.effictivehpdamage = BHP - HP;
                    dmg.effictiveslddamage = BSHLD - SHLD;
                    dmg.effictivearmordamage = BARMOR - ARMOR;
                }
                if (((MagicalDamage) dmg).NATUREDAMAGE > 0) {
                    this.getmagicaldamage((MagicalDamage) dmg, ((MagicalDamage) dmg).NATUREDAMAGE);
                    dmg.effictivehpdamage = BHP - HP;
                    dmg.effictiveslddamage = BSHLD - SHLD;
                    dmg.effictivearmordamage = BARMOR - ARMOR;
                }
                if (((MagicalDamage) dmg).ARCANEDAMAGE > 0) {
                    int remain=0;
                    if (SHLD > 0) {
                        BSHLD = SHLD;
                        SHLD -= ((MagicalDamage) dmg).ARCANEDAMAGE;
                        if (SHLD < 0) {remain= - SHLD;SHLD = 0;}
                        dmg.effictiveslddamage += BSHLD - SHLD;
                    }
                    else {
                        remain = (int) ((MagicalDamage) dmg).ARCANEDAMAGE;
                    }
                    if(remain>0)
                    {
                            BHP = HP;
                            HP -= remain;
                            if (HP < 0) HP = 0;
                            dmg.effictivehpdamage += BHP - HP;
                    }
                }
            }
            damageconsult(dmg, this);
            if (!isAlive()) {
                die(src);
                return;
            }
        }

        if (buff(Paralysis.class) != null) {
            if (Random.Int(dmg.effictivehpdamage) >= Random.Int(HP)) {
                Buff.detach(this, Paralysis.class);
                if (Dungeon.visible[pos]) {
                    GLog.i(Messages.get(Char.class, "out_of_paralysis", name));
                }
            }
        }

        if (dmg instanceof AbsoluteDamage) {
        } else {
            if (this.buff(ShieldRecharging.class) != null)
                this.buff(ShieldRecharging.class).reset();
            else {
                Buff.affect(this, ShieldRecharging.class);
            }
        }
        if (!isAlive()) {
            die(src);
        }
    }

    public void damageconsult(Damage damage, Char enemy) {
        if (damage.effictiveslddamage <= 0 && damage.effictivearmordamage <= 0 && damage.effictivehpdamage <= 0) {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, Integer.toString(0));
            return;
        }

        if (damage.effictivehpdamage > 0)
            enemy.sprite.showStatus(CharSprite.HPDMG, Integer.toString(damage.effictivehpdamage));
        if (damage.effictiveslddamage > 0)
            enemy.sprite.showStatus(CharSprite.SHLDDMG, Integer.toString(damage.effictiveslddamage));
        if (damage.effictivearmordamage > 0)
            enemy.sprite.showStatus(CharSprite.ARMORDMG, Integer.toString(damage.effictivearmordamage));
    }

    private void getmagicaldamage(MagicalDamage damage, float dmg) {
        float percentage = 0f;
        float remain=0;
        if (SHLD > 0)
        {
           // percentage = 0.4f;
            int BSHLD = SHLD;
            //SHLD -= dmg * percentage;
            SHLD -= dmg;
            if (SHLD < 0) { dmg= -SHLD;SHLD = 0;}
            damage.effictiveslddamage += BSHLD - SHLD;
        }
        //if(remain+dmg * (1 - percentage)>0)
        if(dmg>0)
        {
            //remain+=dmg * (1 - percentage);
                int BHP = HP;
                HP -= dmg*1.2f;
                if (HP < 0) HP = 0;
                damage.effictivehpdamage += BHP - HP;
        }
    }

    public void ArmorHandle(PhysicalDamage dmg)
    {
        int BARMOR=ARMOR;
        if(ARMOR>0) {
            int threshold = (this instanceof Hero ? (((Hero) this).belongings.armor == null ? 1 : ((Hero) this).belongings.armor.slashthreshold()) : SlashThreshold);

            //float impact=dmg.IMPACTDAMAGE;
            float puncture = dmg.PUNCTUREDAMAGE;
            float slash = dmg.SLASHDAMAGE;

            dmg.IMPACTDAMAGE -= ARMOR / 5;
            dmg.PUNCTUREDAMAGE -= ARMOR / 6;
            dmg.SLASHDAMAGE -= ARMOR / 3;

            if (slash >= threshold) {
                ARMOR -= (Math.floor((slash - threshold) / threshold) + 1);
            }
            if (puncture > 0) {
                    ARMOR -= Math.floor(puncture * 0.3f);
                }
            dmg.effictivearmordamage=BARMOR-ARMOR;
            }
        else {ARMOR=0;}

    }

    public void destroy() {
        HP = 0;
        SHLD = 0;
        ARMOR = 0;
        Actor.remove(this);
    }

    public void die(Object src) {
        destroy();
        sprite.die();
    }

    public boolean isAlive() {
        return HP > 0;
    }

    @Override
    protected void spend(float time) {

        float timeScale = 1f;
        if (buff(Slow.class) != null) {
            timeScale *= 0.5f;
            //slowed and chilled do not stack
        } else if (buff(Chill.class) != null) {
            timeScale *= buff(Chill.class).speedFactor();
        }
        if (buff(Speed.class) != null) {
            timeScale *= 2.0f;
        }
        super.spend(time / timeScale);
    }

    public HashSet<Buff> buffs() {
        return buffs;
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends Buff> HashSet<T> buffs(Class<T> c) {
        HashSet<T> filtered = new HashSet<>();
        for (Buff b : buffs) {
            if (c.isInstance(b)) {
                filtered.add((T) b);
            }
        }
        return filtered;
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends Buff> T buff(Class<T> c) {
        for (Buff b : buffs) {
            if (c.isInstance(b)) {
                return (T) b;
            }
        }
        return null;
    }

    public boolean isCharmedBy(Char ch) {
        int chID = ch.id();
        for (Buff b : buffs) {
            if (b instanceof Charm && ((Charm) b).object == chID) {
                return true;
            }
        }
        return false;
    }

    public void add(Buff buff) {

        buffs.add(buff);
        Actor.add(buff);

        if (sprite != null)
            switch (buff.type) {
                case POSITIVE:
                    sprite.showStatus(CharSprite.POSITIVE, buff.toString());
                    break;
                case NEGATIVE:
                    sprite.showStatus(CharSprite.NEGATIVE, buff.toString());
                    break;
                case NEUTRAL:
                    sprite.showStatus(CharSprite.NEUTRAL, buff.toString());
                    break;
                case SILENT:
                default:
                    break; //show nothing
            }

    }

    public void remove(Buff buff) {

        buffs.remove(buff);
        Actor.remove(buff);

    }

    public void remove(Class<? extends Buff> buffClass) {
        for (Buff buff : buffs(buffClass)) {
            remove(buff);
        }
    }

    @Override
    protected void onRemove() {
        for (Buff buff : buffs.toArray(new Buff[buffs.size()])) {
            buff.detach();
        }
    }

    public void updateSpriteState() {
        for (Buff buff : buffs) {
            buff.fx(true);
        }
    }

    public int stealth() {
        return 0;
    }

    public void move(int step) {

        if (Dungeon.level.adjacent(step, pos) && buff(Vertigo.class) != null) {
            sprite.interruptMotion();
            int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
            if (!(Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos]) || Actor.findChar(newPos) != null)
                return;
            else {
                sprite.move(pos, newPos);
                step = newPos;
            }
        }

        if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
            Door.leave(pos);
        }

        pos = step;

        if (flying && Dungeon.level.map[pos] == Terrain.DOOR) {
            Door.enter(pos);
        }

        if (this != hero) {
            sprite.visible = Dungeon.visible[pos];
        }

        if (!flying) {
            Dungeon.level.press( pos, this );
        }
    }

    public int distance(Char other) {
        return Dungeon.level.distance(pos, other.pos);
    }

    public void onMotionComplete() {
        //Does nothing by default
        //The main actor thread already accounts for motion,
        // so calling next() here isn't necessary (see Actor.process)
    }

    public void onAttackComplete() {
        next();
    }

    public void onOperateComplete() {
        next();
    }

    public HashSet<Class<?>> resistances() {
        return EMPTY;
    }

    public HashSet<Class<?>> immunities() {
        return EMPTY;
    }

    public HashSet<Property> properties() {
        return properties;
    }

    public enum Property {
        BOSS,
        MINIBOSS,
        UNDEAD,
        DEMONIC,
        IMMOVABLE
    }
}
