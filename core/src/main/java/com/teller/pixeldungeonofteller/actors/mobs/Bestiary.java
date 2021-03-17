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
package com.teller.pixeldungeonofteller.actors.mobs;

import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Bestiary {

    public static ArrayList<Class<? extends Mob>> getMobRotation(int depth ){
        ArrayList<Class<? extends Mob>> mobs = standardMobRotation( depth );
        addRareMobs(depth, mobs);
        swapMobAlts(mobs);
        Random.shuffle(mobs);
        return mobs;
    }

    //returns a rotation of standard mobs, unshuffled.
    private static ArrayList<Class<? extends Mob>> standardMobRotation( int depth ){
        switch(depth){

            // Sewers
            case 1: default:
                //10x rat
                return new ArrayList<Class<? extends Mob>>(Arrays.asList(
                        Rat.class, Rat.class, Rat.class, Rat.class, Rat.class,
                        Rat.class, Rat.class, Rat.class, Rat.class, Rat.class));
            case 2:
                //3x rat, 3x gnoll
                return new ArrayList<>(Arrays.asList(Rat.class, Rat.class, Rat.class,
                        Gnoll.class, Gnoll.class, Gnoll.class));
            case 3:
                //2x rat, 4x gnoll, 1x crab, 1x swarm
                return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,
                        Gnoll.class, Gnoll.class, Gnoll.class, Gnoll.class,
                        Crab.class, Swarm.class));
            case 4:
                //1x rat, 2x gnoll, 3x crab, 1x swarm
                return new ArrayList<>(Arrays.asList(Rat.class,
                        Gnoll.class, Gnoll.class,
                        Crab.class, Crab.class, Crab.class,
                        Swarm.class));

            // Prison
            case 6:
                //3x skeleton, 1x thief, 1x swarm
                return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
                        Thief.class,
                        Swarm.class));
            case 7:
                //3x skeleton, 1x thief, 1x shaman, 1x guard
                return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
                        Thief.class,
                        Shaman.class,
                        Guard.class));
            case 8:
                //3x skeleton, 1x thief, 2x shaman, 2x guard
                return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
                        Thief.class,
                        Shaman.class, Shaman.class,
                        Guard.class, Guard.class));
            case 9:
                //3x skeleton, 1x thief, 2x shaman, 3x guard
                return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class, Skeleton.class,
                        Thief.class,
                        Shaman.class, Shaman.class,
                        Guard.class, Guard.class, Guard.class));

            // Caves
            case 11:
                //5x bat, 1x brute
                return new ArrayList<>(Arrays.asList(
                        Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
                        Brute.class));
            case 12:
                //5x bat, 5x brute, 1x spinner
                return new ArrayList<>(Arrays.asList(
                        Bat.class, Bat.class, Bat.class, Bat.class, Bat.class,
                        Brute.class, Brute.class, Brute.class, Brute.class, Brute.class,
                        Spinner.class));
            case 13:
                //1x bat, 3x brute, 1x shaman, 1x spinner
                return new ArrayList<>(Arrays.asList(
                        Bat.class,
                        Brute.class, Brute.class, Brute.class,
                        Shaman.class,
                        Spinner.class));
            case 14:
                //1x bat, 3x brute, 1x shaman, 4x spinner
                return new ArrayList<>(Arrays.asList(
                        Bat.class,
                        Brute.class, Brute.class, Brute.class,
                        Shaman.class,
                        Spinner.class, Spinner.class, Spinner.class, Spinner.class));

            // City
            case 16:
                //5x elemental, 5x warlock, 1x monk
                return new ArrayList<>(Arrays.asList(
                        Elemental.class, Elemental.class, Elemental.class, Elemental.class, Elemental.class,
                        Warlock.class, Warlock.class, Warlock.class, Warlock.class, Warlock.class,
                        Monk.class));
            case 17:
                //2x elemental, 2x warlock, 2x monk
                return new ArrayList<>(Arrays.asList(
                        Elemental.class, Elemental.class,
                        Warlock.class, Warlock.class,
                        Monk.class, Monk.class));
            case 18:
                //1x elemental, 1x warlock, 2x monk, 1x golem
                return new ArrayList<>(Arrays.asList(
                        Elemental.class,
                        Warlock.class,
                        Monk.class, Monk.class,
                        Golem.class));
            case 19:
                //1x elemental, 1x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        Elemental.class,
                        Warlock.class,
                        Monk.class, Monk.class,
                        Golem.class, Golem.class, Golem.class));

            // Halls
            case 22:
                //3x succubus, 3x evil eye
                return new ArrayList<>(Arrays.asList(
                        Succubus.class, Succubus.class, Succubus.class,
                        Eye.class, Eye.class, Eye.class));
            case 23:
                //2x succubus, 4x evil eye, 2x scorpio
                return new ArrayList<>(Arrays.asList(
                        Succubus.class, Succubus.class,
                        Eye.class, Eye.class, Eye.class, Eye.class,
                        Scorpio.class, Scorpio.class));
            case 24:
                //1x succubus, 2x evil eye, 3x scorpio
                return new ArrayList<>(Arrays.asList(
                        Succubus.class,
                        Eye.class, Eye.class,
                        Scorpio.class, Scorpio.class, Scorpio.class));
        }

    }

    //has a chance to add a rarely spawned mobs to the rotation
    public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){

        switch (depth){

            // Sewers
            default:
                return;
            case 4:
                if (Random.Float() < 0.01f) rotation.add(Skeleton.class);
                if (Random.Float() < 0.01f) rotation.add(Thief.class);
                return;

            // Prison
            case 6:
                if (Random.Float() < 0.2f)  rotation.add(Shaman.class);
                return;
            case 8:
                if (Random.Float() < 0.02f) rotation.add(Bat.class);
                return;
            case 9:
                if (Random.Float() < 0.02f) rotation.add(Bat.class);
                if (Random.Float() < 0.01f) rotation.add(Brute.class);
                return;

            // Caves
            case 13:
                if (Random.Float() < 0.02f) rotation.add(Elemental.class);
                return;
            case 14:
                if (Random.Float() < 0.02f) rotation.add(Elemental.class);
                if (Random.Float() < 0.01f) rotation.add(Monk.class);
                return;

            // City
            case 19:
                if (Random.Float() < 0.02f) rotation.add(Succubus.class);
                return;
        }
    }

    //switches out regular mobs for their alt versions when appropriate
    private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
        for (int i = 0; i < rotation.size(); i++){
            if (Random.Int( 50 ) == 0) {
                Class<? extends Mob> cl = rotation.get(i);
                if (cl == Rat.class) {
                    cl = Albino.class;
                } else if (cl == Thief.class) {
                    cl = Bandit.class;
                } else if (cl == Brute.class) {
                    cl = Shielded.class;
                } else if (cl == Monk.class) {
                    cl = Senior.class;
                } else if (cl == Scorpio.class) {
                    cl = Acidic.class;
                }
                rotation.set(i, cl);
            }
        }
    }

    public static Mob mob(int depth) {
        @SuppressWarnings("unchecked")
        Class<? extends Mob> cl = (Class<? extends Mob>) mobClass(depth);
        try {
            return cl.newInstance();
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    public static Mob mutable(int depth) {
        @SuppressWarnings("unchecked")
        Class<? extends Mob> cl = (Class<? extends Mob>) mobClass(depth);

        if (Random.Int(30) == 0) {
            if (cl == Rat.class) {
                cl = Albino.class;
            } else if (cl == Thief.class) {
                cl = Bandit.class;
            } else if (cl == Brute.class) {
                cl = Shielded.class;
            } else if (cl == Monk.class) {
                cl = Senior.class;
            } else if (cl == Scorpio.class) {
                cl = Acidic.class;
            }
        }

        try {
            return cl.newInstance();
        } catch (Exception e) {
            PixelDungeonOfTeller.reportException(e);
            return null;
        }
    }

    private static Class<?> mobClass(int depth) {

        float[] chances;
        Class<?>[] classes;

        switch (depth) {
            case 1:
                chances = new float[]{1};
                classes = new Class<?>[]{Rat.class};
                break;
            case 2:
                chances = new float[]{1, 1};
                classes = new Class<?>[]{Rat.class, Gnoll.class};
                break;
            case 3:
                chances = new float[]{2, 4, 1, 1};
                classes = new Class<?>[]{Rat.class, Gnoll.class, Crab.class, Swarm.class};
                break;
            case 4:
                chances = new float[]{1, 2, 3, 1, 0.01f, 0.01f};
                classes = new Class<?>[]{Rat.class, Gnoll.class, Crab.class, Swarm.class, Skeleton.class, Thief.class};
                break;

            case 5:
                chances = new float[]{1};
                classes = new Class<?>[]{Goo.class};
                break;

            case 6:
                chances = new float[]{3, 1, 1, 0.2f};
                classes = new Class<?>[]{Skeleton.class, Thief.class, Swarm.class, Shaman.class};
                break;
            case 7:
                chances = new float[]{3, 1, 1, 1};
                classes = new Class<?>[]{Skeleton.class, Shaman.class, Thief.class, Guard.class};
                break;
            case 8:
                chances = new float[]{3, 2, 2, 1, 0.02f};
                classes = new Class<?>[]{Skeleton.class, Shaman.class, Guard.class, Thief.class, Bat.class};
                break;
            case 9:
                chances = new float[]{3, 3, 2, 1, 0.02f, 0.01f};
                classes = new Class<?>[]{Skeleton.class, Guard.class, Shaman.class, Thief.class, Bat.class, Brute.class};
                break;

            case 10:
                chances = new float[]{1};
                classes = new Class<?>[]{Tengu.class};
                break;

            case 11:
                chances = new float[]{1, 0.2f};
                classes = new Class<?>[]{Bat.class, Brute.class};
                break;
            case 12:
                chances = new float[]{1, 1, 0.2f};
                classes = new Class<?>[]{Bat.class, Brute.class, Spinner.class};
                break;
            case 13:
                chances = new float[]{1, 3, 1, 1, 0.02f};
                classes = new Class<?>[]{Bat.class, Brute.class, Shaman.class, Spinner.class, Elemental.class};
                break;
            case 14:
                chances = new float[]{1, 3, 1, 4, 0.02f, 0.01f};
                classes = new Class<?>[]{Bat.class, Brute.class, Shaman.class, Spinner.class, Elemental.class, Monk.class};
                break;

            case 15:
                chances = new float[]{1};
                classes = new Class<?>[]{DM300.class};
                break;

            case 16:
                chances = new float[]{1, 1, 0.2f};
                classes = new Class<?>[]{Elemental.class, Warlock.class, Monk.class};
                break;
            case 17:
                chances = new float[]{1, 1, 1};
                classes = new Class<?>[]{Elemental.class, Monk.class, Warlock.class};
                break;
            case 18:
                chances = new float[]{1, 2, 1, 1};
                classes = new Class<?>[]{Elemental.class, Monk.class, Golem.class, Warlock.class};
                break;
            case 19:
                chances = new float[]{1, 2, 3, 1, 0.02f};
                classes = new Class<?>[]{Elemental.class, Monk.class, Golem.class, Warlock.class, Succubus.class};
                break;

            case 20:
                chances = new float[]{1};
                classes = new Class<?>[]{King.class};
                break;

            case 22:
                chances = new float[]{1, 1};
                classes = new Class<?>[]{Succubus.class, Eye.class};
                break;
            case 23:
                chances = new float[]{1, 2, 1};
                classes = new Class<?>[]{Succubus.class, Eye.class, Scorpio.class};
                break;
            case 24:
                chances = new float[]{1, 2, 3};
                classes = new Class<?>[]{Succubus.class, Eye.class, Scorpio.class};
                break;

            case 25:
                chances = new float[]{1};
                classes = new Class<?>[]{Yog.class};
                break;

            default:
                chances = new float[]{1};
                classes = new Class<?>[]{Eye.class};
        }

        return classes[Random.chances(chances)];
    }
}
