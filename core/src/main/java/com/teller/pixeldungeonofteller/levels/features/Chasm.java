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
package com.teller.pixeldungeonofteller.levels.features;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Badges;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Bleeding;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Cripple;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.items.artifacts.DriedRose;
import com.teller.pixeldungeonofteller.items.artifacts.TimekeepersHourglass;
import com.teller.pixeldungeonofteller.levels.RegularLevel;
import com.teller.pixeldungeonofteller.levels.rooms.Room;
import com.teller.pixeldungeonofteller.levels.rooms.special.WeakFloorRoom;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.scenes.InterlevelScene;
import com.teller.pixeldungeonofteller.sprites.MobSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Chasm {

    public static boolean jumpConfirmed = false;

    public static void heroJump(final Hero hero) {
        GameScene.show(
                new WndOptions(Messages.get(Chasm.class, "chasm"),
                        Messages.get(Chasm.class, "jump"),
                        Messages.get(Chasm.class, "yes"),
                        Messages.get(Chasm.class, "no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            jumpConfirmed = true;
                            hero.resume();
                        }
                    }
                }
        );
    }

    public static void heroFall(int pos) {

        jumpConfirmed = false;

        Sample.INSTANCE.play(Assets.SND_FALLING);

        Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
        if (buff != null) buff.detach();

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
            if (mob instanceof DriedRose.GhostHero) mob.destroy();

        if (Dungeon.hero.isAlive()) {
            Dungeon.hero.interrupt();
            InterlevelScene.mode = InterlevelScene.Mode.FALL;
            if (Dungeon.level instanceof RegularLevel) {
                Room room = ((RegularLevel) Dungeon.level).room(pos);
                InterlevelScene.fallIntoPit = room != null &&  room instanceof WeakFloorRoom;
            } else {
                InterlevelScene.fallIntoPit = false;
            }
            Game.switchScene(InterlevelScene.class);
        } else {
            Dungeon.hero.sprite.visible = false;
        }
    }

    public static void heroLand() {

        Hero hero = Dungeon.hero;

        hero.sprite.burst(hero.sprite.blood(), 10);
        Camera.main.shake(4, 0.2f);

        Dungeon.level.press( hero.pos, hero );
        Buff.prolong(hero, Cripple.class, Cripple.DURATION);
        Buff.affect(hero, Bleeding.class).set(hero.HT / 6);
        hero.damage(new AbsoluteDamage(Random.NormalIntRange(hero.HP / 4, hero.HT / 4), hero, hero), new Hero.Doom() {
            @Override
            public void onDeath() {
                Badges.validateDeathFromFalling();
                Dungeon.fail(getClass());
                GLog.n(Messages.get(Chasm.class, "ondeath"));
            }
        });
    }

    public static void mobFall(Mob mob) {
        if (mob.isAlive()) mob.die( Chasm.class );
        ((MobSprite) mob.sprite).fall();
    }
}
