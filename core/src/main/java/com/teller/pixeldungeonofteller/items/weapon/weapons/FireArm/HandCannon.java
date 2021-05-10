package com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Spark;
import com.teller.pixeldungeonofteller.effects.particles.SmokeParticle;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Dagger;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.ui.MainHandIndicator;
import com.teller.pixeldungeonofteller.ui.OffHandIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class HandCannon extends FireArm {
    @Override
    public Type WeaponType() {
        return Type.OffHand;
    }

    public static final String AC_SHOOT = "SHOOT";
    private static final String COOLDOWN = "cooldown";

    private static final float TIME_TO_SHOOT = 1f;

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public float cooldown;

    {
        image = ItemSpriteSheet.HANDCANNON;
        tier = 1;
        usesTargeting = true;
        cooldown = 5;
    }

    public void reload(float time)
    {
        if(cooldown==0) return;
        else
        {
            cooldown-=time;
            if(cooldown<=0) cooldown=0;
        }
    }

    @Override
    public int DEXReq(int lvl) {
        return 3;
    }

    @Override
    public String status() {
        return Messages.format("%d",(int)(Math.ceil(cooldown)));
    }

    @Override
    public int stealth() {return 1;}

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN, cooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getInt(COOLDOWN);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.belongings.offhandweapon == this) {
            actions.add(AC_SHOOT);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SHOOT)) {
            if ((hero.belongings.offhandweapon == this)) {
                if (cooldown == 0) {
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                } else {
                    OffHandIndicator.cancel();
                    GLog.n(Messages.get(FireArm.class, "outofammo"));
                }
            } else {
                GLog.n(Messages.get(FireArm.class, "unequip"));
            }
        }
    }

    public int shoottime()
    {
        return 4;
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                boolean vaild = false;
                if(target!= curUser.pos) {
                    Ballistica soundOut = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
                    if(soundOut.path.size()>1)
                    {
                        int direction = soundOut.path.get(1)   ;//we wants to get the right number of neighbour8 block the target is
                        int index = 0;
                        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                            if(curUser.pos + PathFinder.NEIGHBOURS8[i] == direction  )
                            {
                                index = i;
                                break;
                            }
                        }

                        ArrayList<Integer> theoryZone = new ArrayList<>();
                        for (int i = index * 5;i<= index * 5 + 4 ;i++)
                        {
                            theoryZone.add(curUser.pos + PathFinder.CUSTOMCONE[i]);
                        }

                       HashSet<Integer> trueZone = new HashSet<>();//here should not get same one
                        for(int cell:theoryZone)
                        {
                            int realCell = new Ballistica(curUser.pos,cell,Ballistica.PROJECTILE).collisionPos;
                            if(Dungeon.level.insideMap(realCell) && realCell!= curUser.pos)
                            {
                                trueZone.add(realCell);
                            }
                        }

                        if(trueZone.size()!=0)
                        {
                            ArrayList<Integer> potentialPos = new ArrayList<>(trueZone);
                            ArrayList<Integer> shootTime = new ArrayList<>();
                            for (int i=0;i<potentialPos.size();i++){ shootTime .add(0);}


                            for(int i=0;i<shoottime();i++)
                            {
                                Integer result = Random.element(potentialPos);
                                int time = shootTime.get(potentialPos.indexOf(result));
                                shootTime.set(potentialPos.indexOf(result),time+1);
                            }

                            for(final int cell : potentialPos)
                            {
                                Item ammo = null;
                                final int level = shootTime.get(potentialPos.indexOf(cell));
                                switch (level)
                                {
                                    case 1:ammo = new TinyBullet();
                                    case 2:
                                    case 3:ammo = new Bullet();
                                    case 4:ammo = new GiantBullet();
                                }

                                if(level>0)
                                {
                                    ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class)).
                                            reset(Dungeon.hero.pos, cell, ammo, new Callback() {
                                                @Override
                                                public void call() {
                                                    Char enemy = Actor.findChar(cell);
                                                    if(enemy!=null)
                                                    {
                                                        PhysicalDamage dmg = shootdamageRoll(level);
                                                        enemy.damage(dmg, this);
                                                    }
                                                    hero.spendAndNext(0f);
                                                }
                                            });
                                    Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + 1 * 0.2f, 0.4f + 1 * 0.2f, 1.55f - 1 * 0.15f);
                                    Camera.main.shake(1, 0.1f);
                                    PointF pf = DungeonTilemap.tileCenterToWorld(curUser.pos);
                                    PointF pt = DungeonTilemap.tileCenterToWorld(cell);
                                    curUser.sprite.emitter().burst(SmokeParticle.FACTORY, 3 + 1);
                                    Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 3 + 1);
                                }
                            }
                            vaild = true ;
                            hero.buff(Noise.class).fire_firearm();
                            Invisibility.dispel();
                            cooldown=5;
                            hero.busy();
                            hero.spendAndNext(1f);
                        }
                    }
                }
                if(!vaild)
                {
                    GLog.i(Messages.get(HandCannon.class, "invalid"));
                }
            }
        }
        @Override
        public String prompt() {
            return Messages.get(FireArm.class, "prompt");
        }
    };

    public PhysicalDamage shootdamageRoll(int level)
    {
        PhysicalDamage shootdamage=new PhysicalDamage();
        switch (level)
        {
            case 1:  shootdamage.AddImpact(imbue.damageFactor(Random.Int(3,12)+level()));
                shootdamage.AddPuncture(imbue.damageFactor((Random.Int(1,3)+level())));
            case 2:  shootdamage.AddImpact(imbue.damageFactor(Random.Int(3,12)+level()));
                shootdamage.AddPuncture(imbue.damageFactor((Random.Int(1,3)+level())));
            case 3:  shootdamage.AddImpact(imbue.damageFactor(Random.Int(3,12)+level()));
                shootdamage.AddPuncture(imbue.damageFactor((Random.Int(1,3)+level())));
            case 4:  shootdamage.AddImpact(imbue.damageFactor(Random.Int(3,12)+level()));
                shootdamage.AddPuncture(imbue.damageFactor((Random.Int(1,3)+level())));
        }
        return shootdamage;
    }

    private class Bullet extends Item {
        {
            image = ItemSpriteSheet.BULLET;
        }
    }

    private class TinyBullet extends Item {
        {
            image = ItemSpriteSheet.TINYBULLET;
        }
    }

    private class GiantBullet extends Item {
        {
            image = ItemSpriteSheet.GIANTBULLET;
        }
    }
}
