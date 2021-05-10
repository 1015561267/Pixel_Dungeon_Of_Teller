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

import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.Calendar;

public class Statistics {

    private static final String GOLD = "score";
    private static final String DEEPEST = "maxDepth";
    private static final String SLAIN = "enemiesSlain";
    private static final String FOOD = "foodEaten";
    private static final String ALCHEMY = "potionsCooked";
    private static final String PIRANHAS = "priranhas";
    private static final String NIGHT = "nightHunt";
    private static final String ANKHS = "ankhsUsed";
    private static final String DURATION = "duration";
    private static final String AMULET = "amuletObtained";

    private static final String DAY = "day";//have inspired and supported by Egoal and his darkest pd

    public static int goldCollected;
    public static int deepestFloor;
    public static int enemiesSlain;
    public static int foodEaten;
    public static int potionsCooked;
    public static int piranhasKilled;
    public static int nightHunt;
    public static int ankhsUsed;
    public static float duration;
    public static boolean qualifiedForNoKilling = false;
    public static boolean completedWithNoKilling = false;
    public static boolean amuletObtained = false;

    public static dayAndNight day = new dayAndNight();

    public static void reset() {

        goldCollected = 0;
        deepestFloor = 0;
        enemiesSlain = 0;
        foodEaten = 0;
        potionsCooked = 0;
        piranhasKilled = 0;
        nightHunt = 0;
        ankhsUsed = 0;

        duration = 0;

        qualifiedForNoKilling = false;

        amuletObtained = false;

        day =  new dayAndNight();

        day.init();
    }

    public static void storeInBundle(Bundle bundle) {
        bundle.put(GOLD, goldCollected);
        bundle.put(DEEPEST, deepestFloor);
        bundle.put(SLAIN, enemiesSlain);
        bundle.put(FOOD, foodEaten);
        bundle.put(ALCHEMY, potionsCooked);
        bundle.put(PIRANHAS, piranhasKilled);
        bundle.put(NIGHT, nightHunt);
        bundle.put(ANKHS, ankhsUsed);
        bundle.put(DURATION, duration);
        bundle.put(AMULET, amuletObtained);
        bundle.put(DAY,day.totalMinutes);
    }

    public static void restoreFromBundle(Bundle bundle) {
        goldCollected = bundle.getInt(GOLD);
        deepestFloor = bundle.getInt(DEEPEST);
        enemiesSlain = bundle.getInt(SLAIN);
        foodEaten = bundle.getInt(FOOD);
        potionsCooked = bundle.getInt(ALCHEMY);
        piranhasKilled = bundle.getInt(PIRANHAS);
        nightHunt = bundle.getInt(NIGHT);
        ankhsUsed = bundle.getInt(ANKHS);
        duration = bundle.getFloat(DURATION);
        amuletObtained = bundle.getBoolean(AMULET);

        day.totalMinutes = bundle.getFloat(DAY);
        day.update();
    }

    public static void preview( GamesInProgress.Info info, Bundle bundle ){
        info.goldCollected  = bundle.getInt( GOLD );
        info.maxDepth       = bundle.getInt( DEEPEST );
    }

    public static class dayAndNight
    {
        public float totalMinutes = 0f ;
        public DayState state = DayState.Day;
        public int day = (int)Math.floor(totalMinutes) / 60 / 24;
        public int hour = (int)Math.floor(totalMinutes) / 60 % 24;
        public int minute = (int)Math.floor(totalMinutes) % 60;

        public String time = String.format("%02d:%02d" , hour, minute);

        public void init()
        {
            final Calendar calendar = Calendar.getInstance();

            calendar.get(Calendar.AM_PM);

            totalMinutes = 9*60f;
            day = 0 ;
            hour = 9 ;
            minute = 0;

            state = DayState.Day;
        }

        public void spend(float time)
        {
            totalMinutes += time ;
            update();
        }


        public boolean needNotice()
        {
            if(hour == 2 || hour == 7 || hour == 19 || hour == 22)
            {
                if(minute >= 0 && minute <= 20)
                {
                    time = String.format("%02d:%02d" , hour, minute);
                    return true;
                }
            }
            return false;
        }

        public void update()
        {
            day = (int)Math.floor(totalMinutes) / 60 / 24;
            hour = (int)Math.floor(totalMinutes) / 60 % 24;
            minute = (int)Math.floor(totalMinutes) % 60;

            DayState newState;
            if(hour >= 7 && hour<= 19 )
            newState = DayState.Day;
            else if(hour <2 || hour > 22)
            newState = DayState.Midnight;
            else newState = DayState.Night;

            if(newState!= state)
            {
                state = newState;
                String msg = "";
                switch (state)
                {
                    case Day:      msg = Messages.get(Hero.class , "clock-day");break;
                    case Night:    msg = Messages.get(Hero.class , "clock-night");break;
                    case Midnight: msg = Messages.get(Hero.class , "clock-midnight");break;
                }
                GLog.w(msg);
            }
        }
    }

    public enum DayState{ Day , Night , Midnight }
    public enum MoonPhase{ New , WaxingCrescent , Quarter , WaxingGibbous , Full ,WaningGibbous , LastQuarter , WaningCrecent , Blood }
}
