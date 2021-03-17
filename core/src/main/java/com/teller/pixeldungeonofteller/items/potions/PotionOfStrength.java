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
package com.teller.pixeldungeonofteller.items.potions;

import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndOptions;

public class PotionOfStrength extends Potion {

    {
        initials = 10;

        bones = true;
    }

    @Override
    public void apply(final Hero hero) {
        setKnown();
        GameScene.show(
                new WndOptions(
                        Messages.get(this, "name"),
                        Messages.get(this, "information"),
                        Messages.get(this, "str"),
                        Messages.get(this, "dex"),
                        Messages.get(this, "int"),
                        Messages.get(this, "cancel")) {
                    @Override
                    protected void onSelect(int index) {
                        switch (index) {
                            case 0:
                                hero.STR++;
                                hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(PotionOfStrength.class, "msg_1"));
                                GLog.p(Messages.get(PotionOfStrength.class, "msg_4"));
                                break;
                            case 1:
                                hero.DEX++;
                                hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(PotionOfStrength.class, "msg_2"));
                                GLog.p(Messages.get(PotionOfStrength.class, "msg_5"));
                                break;
                            case 2:
                                hero.INT++;
                                hero.MANA+=3;
                                hero.MANACAP+=3;
                                hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(PotionOfStrength.class, "msg_3"));
                                GLog.p(Messages.get(PotionOfStrength.class, "msg_6"));
                                break;
                            case 3:
                            default:
                                new PotionOfStrength().collect();
                                break;
                        }
                    }

                    public void onBackPressed() {
                    }

                }
        );
        Badges.validateStrengthAttained();
    }

    @Override
    public int price() {
        return isKnown() ? 50 * quantity : super.price();
    }
}
