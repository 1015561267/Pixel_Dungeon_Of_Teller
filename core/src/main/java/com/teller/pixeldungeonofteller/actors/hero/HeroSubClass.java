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

import com.teller.pixeldungeonofteller.messages.Messages;
import com.watabou.utils.Bundle;

public enum HeroSubClass {

    NONE(null),

    GLADIATOR("gladiator"),
    BERSERKER("berserker"),

    WARLOCK("warlock"),
    BATTLEMAGE("battlemage"),

    ASSASSIN("assassin"),
    FREERUNNER("freerunner"),

    SNIPER("sniper"),
    WARDEN("warden");

    private static final String SUBCLASS = "subClass";
    private String title;

    HeroSubClass(String title) {
        this.title = title;
    }

    public static HeroSubClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(SUBCLASS);
        return valueOf(value);
    }

    public String title() {
        return Messages.get(this, title);
    }

    public String desc() {
        return Messages.get(this, title + "_desc");
    }

    public void storeInBundle(Bundle bundle) {
        bundle.put(SUBCLASS, toString());
    }

}
