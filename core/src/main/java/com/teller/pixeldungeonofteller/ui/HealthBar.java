/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
 *
 * //Changed by Teller in 15/7/2019
 * //In order to show armor in the same way,may be changed later.
 */
package com.teller.pixeldungeonofteller.ui;

import com.teller.pixeldungeonofteller.actors.Char;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

public class HealthBar extends Component {

    private static final int COLOR_BG = 0xFFCC0000;
    private static final int COLOR_HP = 0xFF00EE00;

    private static final int COLOR_SHLD = 0xFF00C9FF;
    private static final int COLOR_ARMOR = 0xFF003FFF;

    //private static final int COLOR_SHLD = 0xFF8CD4F4;
    //private static final int COLOR_ARMOR= 0xFFBBC6C7;

    //private static final int COLOR_SHLD = 0xFFBBEEBB;
    //private static final int COLOR_ARMOR= 0xFFB8860B;

    private static final int HEIGHT = 2;

    private ColorBlock Bg;
    private ColorBlock Shld;
    private ColorBlock Hp;
    private ColorBlock Armor;

    private float health;
    private float shield;
    private float armor;

    @Override
    protected void createChildren() {
        Bg = new ColorBlock(1, 1, COLOR_BG);
        add(Bg);

        Armor = new ColorBlock(1, 1, COLOR_ARMOR);
        add(Armor);

        Shld = new ColorBlock(1, 1, COLOR_SHLD);
        add(Shld);

        Hp = new ColorBlock(1, 1, COLOR_HP);
        add(Hp);

        height = HEIGHT;
    }

    @Override
    protected void layout() {

        Bg.x = Armor.x = Shld.x = Hp.x = x;
        Bg.y = Armor.y = Shld.y = Hp.y = y;

        Bg.size(width, HEIGHT);
        Shld.size(width * shield, HEIGHT);
        Hp.size(width * health, HEIGHT);
        Armor.size(width * armor, HEIGHT);

        height = HEIGHT;
    }

    public void level(float value) {
        level(value, 0f, 0f);
    }

    public void level(float health, float shield, float armor) {
        this.health = health;
        this.shield = shield;
        this.armor = armor;
        layout();
    }

    public void level(Char c) {
        float health = c.HP;
        float shield = c.SHLD;
        float armor = c.ARMOR;

        float max = Math.max(health + shield + armor, c.HT + c.MAXSHLD + c.ARMOR);

        level(health / max, (health + shield) / max, (health + shield + armor) / max);
    }
}
