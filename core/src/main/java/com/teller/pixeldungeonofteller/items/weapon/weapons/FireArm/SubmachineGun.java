package com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Pushing;
import com.teller.pixeldungeonofteller.effects.Spark;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.OffHandWeapon;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.ui.StatusPane;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class SubmachineGun extends OffHandWeapon {

    public boolean openfire;
    public float cooldown;
    public int ammo;
    public int ammolimit;

    public static final String AC_SWITCH= "SWITCH";
    public static final String AC_OPEN="OPEN";
    public static final String AC_CEASE="CEASE";

    private static final String OPENFIRE = "openfire";
    private static final String COOLDOWN = "cooldown";
    private static final String AMMO = "ammo";
    private static final String AMMOLIMIT = "ammolimit";
    {
        image = ItemSpriteSheet.SUBMACHINEGUN;
        tier = 2;
        cooldown = 2f;
        ammo = ammolimit = 5;
        openfire = false;
    }

    @Override
    public String status() {
        if(openfire)
        {
            return Messages.format("%d/%d", ammo, ammolimit);
        }
        else
        {
            return "Off";
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if ((hero.belongings.offhandweapon == this)) {
            if(openfire)
                actions.add(AC_CEASE);
            else
                actions.add(AC_OPEN);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        updateQuickslot();
        if (action.equals(AC_SWITCH)) {
            openfire = !openfire;
        }
        else if(action.equals(AC_CEASE))
        {
            openfire=false;
        }
        else if(action.equals(AC_OPEN))
        {
            openfire=true;
        }
    }

    @Override
    public String info() {
        String info = super.info();
        info+="\n"+Messages.get(this, "ammo",ammo ,ammolimit);
        info+="\n"+Messages.get(this, "shotdamage",shootmin() ,shootmax());
        return info;
    }

    public int shootmin() {
        return 3 + level();
    }

    public int shootmax() {
        return 15 + level();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(OPENFIRE, openfire);
        bundle.put(COOLDOWN, cooldown);
        bundle.put(AMMO, ammo);
        bundle.put(AMMOLIMIT, ammolimit);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        openfire =  bundle.getBoolean(OPENFIRE);
        cooldown = bundle.getInt(COOLDOWN);
        ammo = bundle.getInt(AMMO);
        ammolimit = bundle.getInt(AMMOLIMIT);
    }

    public boolean attackable() {
        return false;
    }

    @Override
    public int STRReq(int lvl) {
        return 1;
    }

    @Override
    public int DEXReq(int lvl) {
        return 3;
    }

    public boolean check()
    {
        if(openfire)
        {
            return ammo>0;
        }
        return false;
    }

    public void shoot(final int heropos, int movepos)
    {
        if(check()) {
            final Ballistica shot = new Ballistica(heropos, movepos, Ballistica.MAGIC_BOLT);
            final int cell = shot.collisionPos;
            if (cell != heropos) {
                final Char enemy = Actor.findChar(cell);
                ((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
                        reset(heropos, cell, new Bullet(), new Callback() {
                            @Override
                            public void call() {
                                if (enemy != null) {
                                    if (hero.hit(hero, enemy, false)) {
                                        Damage dmg = ShootDamageRoll();
                                        dmg = enemy.defenseProc(hero, dmg);
                                        enemy.damage(dmg, this);
                                        if (!enemy.isAlive()) {
                                            enemy.sprite.die();
                                            Dungeon.level.mobs.remove(this);
                                            Actor.remove(enemy);
                                        } else {
                                            int effectiveDamage = Math.max(dmg.effictivehpdamage, 0);
                                            enemy.sprite.bloodBurstA(enemy.sprite.center(), effectiveDamage);
                                            enemy.sprite.flash();
                                            float shake = 0f;
                                            if (shake > 1f)
                                                Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
                                        }
                                    } else {
                                        String defense = enemy.defenseVerb();
                                        enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                                    }
                                }
                                Invisibility.dispel();
                                ammo--;
                            }
                        });
                Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + 1 * 0.2f, 0.4f + 1 * 0.2f, 1.55f - 1 * 0.15f);
                Camera.main.shake(1, 0.1f);
                PointF pf = DungeonTilemap.tileCenterToWorld(heropos);
                PointF pt = DungeonTilemap.tileCenterToWorld(cell);
                Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 1);
                hero.buff(Noise.class).fire_firearm();
            }
        }
    }

    public PhysicalDamage ShootDamageRoll()
    {
        PhysicalDamage shootdamage=new PhysicalDamage();
        shootdamage.AddImpact(imbue.damageFactor(Random.Int(20,90)+level()*Random.Int(1,3)));
        shootdamage.AddPuncture(imbue.damageFactor((Random.Int(10,60))));
        return shootdamage;
    }

    public void reload(float time)
    {
        if(ammo == ammolimit) return;
        else
        {
            while (ammo<ammolimit && time>0) {
              if(cooldown>=time)
              {
                  cooldown-=time;
                  time=0;
              }
              else
              {
                  time-=cooldown;
                  ammo++;
                  cooldown=reloadtime();
              }
            }
        }
    }

    private float reloadtime()
    {
        return 2f;
    }

    public class Bullet extends MissileWeapon {
        {
            image = ItemSpriteSheet.BULLET;
        }
        @Override
        public int STRReq(int lvl) {
            return 0;
        }
        @Override
        public int DEXReq(int lvl) {
            return 0;
        }

        @Override
        public int min(int lvl) {
            return 0;
        }

        @Override
        public int max(int lvl) {
            return 0;
        }
    }

    public static class VirtualActor extends Actor {

        private int Pos;
        private int Target;

        private VirtualEffect effect;

        private Callback callback;
        private CharSprite sprite;

        //{
        //    actPriority = 1; //take priority over mobs, but not the hero
        //}

        public VirtualActor(Char ch,int pos,int target) {
            sprite=ch.sprite;
            Pos=pos;
            Target=target;
        }

        {
            actPriority = Integer.MIN_VALUE; //it's a visual effect, gets priority no matter what
        }

        @Override
        protected boolean act() {

            //((SubmachineGun) hero.belongings.offhandweapon).shoot(Pos,Target);


            if (sprite != null) {

                if (effect == null) {
                    new VirtualEffect();
                }
            }

            Actor.remove(VirtualActor.this);

            for (Actor actor : Actor.all()) {
                if (actor instanceof VirtualActor && actor.cooldown() == 0) {
                    return true;
                }
            }
            return false;
        }

        public class VirtualEffect extends Visual
        {

            private float delay;

            private static final float DELAY = 0.15f;

            public VirtualEffect() {
                super(0, 0, 0, 0);

                delay = 0;

                ((SubmachineGun) hero.belongings.offhandweapon).shoot(Pos,Target);
                if (sprite.parent != null)
                    sprite.parent.add(this);
            }

            @Override
            public void update() {

                super.update();

                if ((delay += Game.elapsed) < DELAY) {

                    //sprite.x = x;
                    //sprite.y = y;

                } else {

                    //sprite.point(end);

                    killAndErase();
                    Actor.remove(VirtualActor.this);
                    if (callback != null) callback.call();

                    next();
                }
            }
        }
    }
}
