package com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tonfa;
import com.teller.pixeldungeonofteller.tiles.DungeonTilemap;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.Spark;
import com.teller.pixeldungeonofteller.effects.particles.SmokeParticle;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.teller.pixeldungeonofteller.ui.MainHandIndicator;
import com.teller.pixeldungeonofteller.ui.OffHandIndicator;
import com.teller.pixeldungeonofteller.ui.QuickSlotButton;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class Flintlock extends FireArm {

    public static final String AC_SHOOT = "SHOOT";
    public static final String AC_DOUBLESHOOT = "DOUBLESHOOT";
    private static final String COOLDOWN = "cooldown";

    private static final float TIME_TO_SHOOT = 1f;

    public float cooldown;

    @Override
    public Type WeaponType() {
        return Type.DualWield;
    }

    @Override
    public int stealth() {return 2;}

    public int Impactdamage(){return Random.Int(1,5)+level();}
    public int Slashdamage() {return 0;}
    public int Puncturedamage(){return  0;}

    public Boolean Loaded()
    {
        return cooldown==0;
    }

    public void resetcooldown()
    {
        cooldown=3f;
    }

    @Override
    public int STRReq(int lvl) {
        return 1;
    }

    @Override
    public int DEXReq(int lvl) {
        return 1;
    }

    {
        image = ItemSpriteSheet.FLINTLOCK;
        tier = 1;
        usesTargeting = true;
        cooldown = 3;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") ;}

    @Override
    public int min(int lvl) {
        return 1 + 1 * lvl;
    }

    @Override
    public int max(int lvl) {
        return 5 + 2 * lvl;
    }

    @Override
    public boolean attackable() {
        return true;
    }

    @Override
    public float cooldown() {

        boolean same1 = false;
        boolean same2 = false;
        boolean dual1 = false;
        boolean dual2 = false;

        if (hero.belongings.mainhandweapon!=null)
        {
            if(hero.belongings.mainhandweapon instanceof Flintlock){
                same1 = true;
            }
            else if(hero.belongings.mainhandweapon.WeaponType() == Type.DualWield)
            {
                dual1 = true;
            }
        }

        if (hero.belongings.offhandweapon!=null)
        {
            if(hero.belongings.offhandweapon instanceof Flintlock){
                same2 = true;
            }
            else if(hero.belongings.offhandweapon.WeaponType() == Type.DualWield)
            {
                dual2 = true;
            }
        }

        if(same1&&same2) { return 30f; }
        else if(dual1&&dual2) { return 30f; }
        else return 30f;
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
        if ((hero.belongings.offhandweapon == this) || (hero.belongings.mainhandweapon == this)) {
            actions.add(AC_SHOOT);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SHOOT)) {
            if ((hero.belongings.offhandweapon == this) || (hero.belongings.mainhandweapon == this)) {
                if (cooldown == 0) {
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                } else {
                        MainHandIndicator.cancel();
                    OffHandIndicator.cancel();
                    GLog.n(Messages.get(FireArm.class, "outofammo"));
                }
            } else {
                GLog.n(Messages.get(FireArm.class, "unequip"));
            }
            updateQuickslot();
        }

        //FIXME: I don't want to make everything complex but double gun of course need double shot,in order to give more effectiveness I have to copy some same code,althouth it looks very stuipd.
        else if(action.equals(AC_DOUBLESHOOT)) {
            if ((hero.belongings.mainhandweapon == this)) {
                if (Loaded()) {
                    if(hero.belongings.offhandweapon instanceof Flintlock)
                    {
                        if(((Flintlock) hero.belongings.offhandweapon).Loaded())
                        {
                            curUser = hero;
                            curItem = this;
                            GameScene.selectCell(doubleshooter);
                        }
                        else this.execute(Dungeon.hero,AC_SHOOT);//shot mainhand
                    }
                    else this.execute(Dungeon.hero,AC_SHOOT);
                }
                else
                {
                    if(hero.belongings.offhandweapon instanceof Flintlock)
                    {
                        if(((Flintlock) hero.belongings.offhandweapon).Loaded())
                        {
                            hero.belongings.offhandweapon.execute(hero,AC_SHOOT);//shot offhand
                        }
                        else
                        {
                                MainHandIndicator.cancel();
                                OffHandIndicator.cancel();
                            GLog.n(Messages.get(FireArm.class, "outofammo"));
                        }
                    }
                    else
                    {
                        MainHandIndicator.cancel();
                        OffHandIndicator.cancel();
                        GLog.n(Messages.get(FireArm.class, "outofammo"));
                    }
                }
            }
                else if (hero.belongings.offhandweapon == this) {
                    if (Loaded()) {
                        if(hero.belongings.mainhandweapon instanceof Flintlock)
                        {
                            if(((Flintlock) hero.belongings.mainhandweapon).Loaded())
                            {
                                //double shoot
                                curUser = hero;
                                curItem = this;
                                GameScene.selectCell(doubleshooter);
                            }
                            else this.execute(Dungeon.hero,AC_SHOOT);//shot mainhand
                        }
                        else this.execute(Dungeon.hero,AC_SHOOT);
                    }
                    else
                    {
                        if(hero.belongings.mainhandweapon instanceof Flintlock)
                        {
                            if(((Flintlock) hero.belongings.mainhandweapon).Loaded())
                            {
                                hero.belongings.mainhandweapon.execute( Dungeon.hero,AC_SHOOT);//shot mainhand
                            }
                            else
                            {
                                MainHandIndicator.cancel();
                                OffHandIndicator.cancel();
                                GLog.n(Messages.get(FireArm.class, "outofammo"));
                            }
                        }
                        else
                        {
                            MainHandIndicator.cancel();
                            OffHandIndicator.cancel();
                            GLog.n(Messages.get(FireArm.class, "outofammo"));
                        }
                    }
                }
            }
    }

    @Override
    public String status() {
        return Messages.format("%d",(int)(Math.ceil(cooldown)));
    }

    @Override
    public String info() {
        String info = super.info();
        info+="\n"+Messages.get(this, "shotdamage",shootmin() ,shootmax());
        info+="\n"+Messages.get(this, "turntoload",(int)cooldown);
        info+="\n"+Messages.get(this, "specialmoveinfo");
        return info;
    }

    public int shootmin() {
        return 3 + level();
    }

    public int shootmax() {
        return 15 + level();
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

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                if(cooldown != 0)
                {
                    GLog.n(Messages.get(FireArm.class, "outofammo"));
                    MainHandIndicator.cancel();
                    OffHandIndicator.cancel();
                    return;
                }
                final Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT);
                int cell = shot.collisionPos;
                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i(Messages.get(Flintlock.class, "self_target"));
                    return;
                }
                //new Bullet().cast(hero,target);
                //FIXME:Because of so many problems caused by directly use cast()(partly caused by my offhand machine paryly because I just don't like the melee hit sound),I has had to copy the ideal code at here.
                cell = throwPos(hero, target);
                hero.sprite.zap(cell);
                Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);
                final Char enemy = Actor.findChar(cell);
                QuickSlotButton.target(enemy);
                ((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
                        reset(hero.pos, cell, new Bullet(), new Callback() {
                            @Override
                            public void call() {
                                if(enemy!=null) {
                                    if(hero.hit(hero,enemy,false))
                                    {
                                        Damage dmg1 = ShootDamageRoll();
                                        //dmg1 = hero.attackProc(enemy, dmg1);
                                        dmg1 = enemy.defenseProc(hero, dmg1);
                                        enemy.damage(dmg1, this);
                                        if(!enemy.isAlive())
                                        {
                                            GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                        }
                                        else {
                                            int effectiveDamage = Math.max(dmg1.effictivehpdamage, 0);
                                            enemy.sprite.bloodBurstA(enemy.sprite.center(), effectiveDamage);
                                            enemy.sprite.flash();
                                            float shake = 0f;
                                            if (shake > 1f)
                                                Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
                                        }
                                    }
                                   else
                                    {
                                        String defense = enemy.defenseVerb();
                                        enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                                    }
                                }
                                hero.busy();
                                hero.spendAndNext(1f);
                                Invisibility.dispel();
                                cooldown=3;
                            }
                        });

                hero.buff(Noise.class).fire_firearm();

                Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + 1 * 0.2f, 0.4f + 1 * 0.2f, 1.55f - 1 * 0.15f);
                Camera.main.shake(1, 0.1f);
                PointF pf = DungeonTilemap.tileCenterToWorld(curUser.pos);
                PointF pt = DungeonTilemap.tileCenterToWorld(cell);
                curUser.sprite.emitter().burst(SmokeParticle.FACTORY, 3 + 1);
                Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 3 + 1);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(FireArm.class, "prompt");
        }
    };

    private CellSelector.Listener doubleshooter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                final Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT);
                int cell = shot.collisionPos;
                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i(Messages.get(Flintlock.class, "self_target"));
                    return;
                }
                //new Bullet().cast(hero,target);
                //FIXME:Because of so many problems caused by directly use cast()(partly caused by my offhand machine paryly because I just don't like the melee hit sound),I has had to copy the ideal code at here.
                cell = throwPos(hero, target);
                hero.sprite.zap(cell);
                Sample.INSTANCE.play(Assets.SND_MISS, 0.6f, 0.6f, 1.5f);
                final Char enemy = Actor.findChar(cell);
                QuickSlotButton.target(enemy);
                ((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
                        reset(hero.pos, cell, new DoubleShoot(), new Callback() {
                            @Override
                            public void call() {
                                if(enemy!=null) {
                                    if(hero.hit(hero,enemy,false))
                                    {
                                        Damage dmg1 = ((Flintlock)hero.belongings.mainhandweapon).ShootDamageRoll();
                                        //dmg1 = hero.attackProc(enemy, dmg1);
                                        dmg1 = enemy.defenseProc(hero, dmg1);
                                        enemy.damage(dmg1, this);
                                        if(!enemy.isAlive())
                                        {
                                            GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                        }
                                        else {
                                            if(!enemy.isAlive())
                                            {
                                                GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                            }
                                            int effectiveDamage = Math.max(dmg1.effictivehpdamage, 0);
                                            enemy.sprite.bloodBurstA(enemy.sprite.center(), effectiveDamage);
                                            enemy.sprite.flash();
                                            float shake = 0f;
                                            if (shake > 1f)
                                                Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
                                        }
                                    }
                                    else
                                    {
                                        String defense = enemy.defenseVerb();
                                        enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                                    }

                                    if(enemy.isAlive())
                                    {
                                        if(hero.hit(hero,enemy,false))
                                        {
                                            Damage dmg2 = ((Flintlock)hero.belongings.offhandweapon).ShootDamageRoll();
                                            //dmg2 = hero.attackProc(enemy, dmg2);
                                            dmg2 = enemy.defenseProc(hero, dmg2);
                                            enemy.damage(dmg2, this);
                                            if(!enemy.isAlive())
                                            {
                                                GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                            }
                                            else {
                                                if(!enemy.isAlive())
                                                {
                                                    GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name)));
                                                }
                                                int effectiveDamage = Math.max(dmg2.effictivehpdamage, 0);
                                                enemy.sprite.bloodBurstA(enemy.sprite.center(), effectiveDamage);
                                                enemy.sprite.flash();
                                                float shake = 0f;
                                                if (shake > 1f)
                                                    Camera.main.shake(GameMath.gate(1, shake, 5), 0.3f);
                                            }
                                        }
                                        else
                                        {
                                            String defense = enemy.defenseVerb();
                                            enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);
                                        }
                                    }
                                }
                                hero.busy();
                                hero.spendAndNext(1f);
                                Invisibility.dispel();
                                ((Flintlock)hero.belongings.mainhandweapon).cooldown=3;
                                ((Flintlock)hero.belongings.offhandweapon).cooldown=3;
                            }
                        });
                Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + 1 * 0.2f, 0.4f + 1 * 0.2f, 1.55f - 1 * 0.15f);
                Camera.main.shake(1, 0.1f);
                PointF pf = DungeonTilemap.tileCenterToWorld(curUser.pos);
                PointF pt = DungeonTilemap.tileCenterToWorld(cell);
                curUser.sprite.emitter().burst(SmokeParticle.FACTORY, 3 + 1);
                Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 3 + 1);

                Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + 1 * 0.2f, 0.4f + 1 * 0.2f, 1.55f - 1 * 0.15f);
                curUser.sprite.emitter().burst(SmokeParticle.FACTORY, 3 + 1);
                Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 3 + 1);;

                hero.buff(Noise.class).fire_firearm();
            }
        }

        @Override
        public String prompt() {
            return Messages.get(FireArm.class, "prompt");
        }
    };

    public PhysicalDamage ShootDamageRoll()
    {
        PhysicalDamage shootdamage=new PhysicalDamage();
        shootdamage.AddImpact(imbue.damageFactor(Random.Int(3,12)+level()));
        shootdamage.AddPuncture(imbue.damageFactor((Random.Int(1,3)+level())));
        return shootdamage;
    }


    public class Bullet extends Item {
        {
            image = ItemSpriteSheet.BULLET;
        }
    }

    public class DoubleShoot extends Item {

        {
            image = ItemSpriteSheet.DOUBLESHOOT;
        }
    }
}