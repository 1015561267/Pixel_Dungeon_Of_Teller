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
package com.teller.pixeldungeonofteller.journal;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Journal {

    public static final String JOURNAL_FILE = "journal.dat";

    private static boolean loaded = false;

    public static void loadGlobal(){
        if (loaded){
            return;
        }

        Bundle bundle;
        try {
            bundle = FileUtils.bundleFromFile( JOURNAL_FILE );

        } catch (IOException e){
            bundle = new Bundle();
        }

        Catalog.restore( bundle );
        Document.restore( bundle );

        loaded = true;
    }

    //package-private
    static boolean saveNeeded = false;

    public static void saveGlobal(){
        if (!saveNeeded){
            return;
        }

        Bundle bundle = new Bundle();

        Catalog.store(bundle);
        Document.store(bundle);

        try {
            FileUtils.bundleToFile( JOURNAL_FILE, bundle );
            saveNeeded = false;
        } catch (IOException e) {
            PixelDungeonOfTeller.reportException(e);
        }

    }

    public static ArrayList<Record> records;

    public static void reset() {
        records = new ArrayList<Journal.Record>();
    }

    public static void add(Feature feature) {
        int size = records.size();
        for (int i = 0; i < size; i++) {
            Record rec = records.get(i);
            if (rec.feature == feature && rec.depth == Dungeon.depth) {
                return;
            }
        }

        records.add(new Record(feature, Dungeon.depth));
    }

    public static void remove(Feature feature) {
        int size = records.size();
        for (int i = 0; i < size; i++) {
            Record rec = records.get(i);
            if (rec.feature == feature && rec.depth == Dungeon.depth) {
                records.remove(i);
                return;
            }
        }
    }

    public enum Feature {
        WELL_OF_HEALTH,
        WELL_OF_AWARENESS,
        WELL_OF_TRANSMUTATION,
        ALCHEMY,
        GARDEN,
        STATUE,

        GHOST,
        WANDMAKER,
        TROLL,
        IMP;

        public String desc() {
            return Messages.get(this, name());
        }
    }

    public static class Record implements Comparable<Record>, Bundlable {

        private static final String FEATURE = "feature";
        private static final String DEPTH = "depth";

        public Feature feature;
        public int depth;

        public Record() {
        }

        public Record(Feature feature, int depth) {
            this.feature = feature;
            this.depth = depth;
        }

        @Override
        public int compareTo(Record another) {
            return another.depth - depth;
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            feature = Feature.valueOf(bundle.getString(FEATURE));
            depth = bundle.getInt(DEPTH);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            bundle.put(FEATURE, feature.toString());
            bundle.put(DEPTH, depth);
        }
    }
}
