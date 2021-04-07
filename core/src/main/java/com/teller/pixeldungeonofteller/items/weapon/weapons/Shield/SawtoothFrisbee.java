package com.teller.pixeldungeonofteller.items.weapon.weapons.Shield;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.hazards.Frisbee;
import com.teller.pixeldungeonofteller.actors.hazards.Hazard;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.EquipableItem;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SawtoothFrisbee extends Shield {
    @Override
    public Type WeaponType() {
        return Type.OffHand;
    }

    public int Impactdamage(){return 0;}
    public int Slashdamage() {return Random.Int(1,5)+level();}
    public int Puncturedamage(){return Random.Int(1,3)+level()*Random.Int(0,1);}//Note that this is every time it triggered damage

    private static final float TIME_TO_CAST = 1;
    private static final String AC_CAST = "CAST";
    {
        image = ItemSpriteSheet.SAWTOOTHFRISBEE;
        tier = 4;
        usesTargeting = true;
        defaultAction = AC_CAST;
    }

    private int duration(){return 5;}

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int min(int lvl) {
        return 0;
    }
    @Override
    public int max(int lvl) {
        return 0;
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
    public boolean attackable() {
        return false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.belongings.offhandweapon == this) {
            actions.add(AC_CAST);
            actions.remove(AC_THROW);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CAST)) {
            if (hero.belongings.offhandweapon == this) {
                if (cursed) {
                    GLog.w(Messages.get(EquipableItem.class, "unequip_cursed"));
                    return;
                }
                curItem = this;
                GameScene.selectCell(caster);
            } else {
                GLog.w(Messages.get(this, "needtoequip"));
            }
        }
    }


    public void throwout(Integer pos, Integer target, final Ballistica way)
    {

        if (curItem.isEquipped(curUser))
        {
            curUser.belongings.offhandweapon = null;
            //curItem.collect();
            GameScene.scene.offhandupdate();
        }

        final Integer dst = target;

        ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                reset(pos, dst, this, new Callback() {
                    @Override
                    public void call() {
                        Item detached = (curItem).detach(curUser.belongings.backpack);
                        for (int c : way.subPath(1, dst)) {
                            Char ch;
                            if ((ch = Actor.findChar(c)) != null) {
                                PhysicalDamage dmg = damageRoll(Dungeon.hero);
                                ch.damage(dmg, this);
                                ch.sprite.flash();
                            }
                        }
                        Frisbee frisbee = Hazard.findHazard(dst, Frisbee.class);
                        if (frisbee == null) {
                            frisbee = new Frisbee();
                            frisbee.setValues(dst,duration(), (SawtoothFrisbee) curItem);
                            GameScene.add(frisbee);
                            ((Frisbee.FrisbeeSprite) frisbee.sprite).appear();
                        }
                        else
                        { ((SawtoothFrisbee)curItem).retrieve( dst ); }
                        curUser.spendAndNext(TIME_TO_CAST);
                    }
                });
    }

    public void retrieve(Integer pos)
    {
        Integer start = pos;
        final Integer end = Dungeon.hero.pos;
        curItem = this;

        final Ballistica shot = new Ballistica(start, end, Ballistica.STOP_TARGET);

        ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                reset(start, end, this, new Callback() {
                    @Override
                    public void call() {
                        for (int c : shot.subPath(1, end)) {
                            Char ch;
                            if ((ch = Actor.findChar(c)) != null) {
                                PhysicalDamage dmg = damageRoll(Dungeon.hero);
                                ch.damage(dmg, this);
                            }
                        }
                        if(Dungeon.hero.belongings.offhandweapon == null &&(Dungeon.hero.belongings.mainhandweapon== null || Dungeon.hero.belongings.mainhandweapon.WeaponType() != Type.TwoHanded))
                        {
                            Dungeon.hero.belongings.offhandweapon = (KindOfWeapon) curItem;
                            GameScene.scene.offhandupdate();
                            GLog.h("Equipped");
                        }
                        else
                        {
                            if(!curItem.collect())
                            {
                                Dungeon.level.drop(curItem,end);
                                GLog.h("Dropped");
                            }
                        }
                    }
                });
    }

    public void fastreturn(Integer pos)
    //callback becomes invalid in ascend and descend or things like that,because the anim have no time to callback
    {
        Integer start = pos;
        final Integer end = Dungeon.hero.pos;

        GLog.n(start.toString());

        curItem = this;

        final Ballistica shot = new Ballistica(start, end, Ballistica.STOP_TARGET);

        for (int c : shot.subPath(1, end)) {
            Char ch;
            if ((ch = Actor.findChar(c)) != null) {
                PhysicalDamage dmg = damageRoll(Dungeon.hero);
                ch.damage(dmg, this);
            }
        }

        if(Dungeon.hero.belongings.offhandweapon == null &&(Dungeon.hero.belongings.mainhandweapon== null || Dungeon.hero.belongings.mainhandweapon.WeaponType() != Type.TwoHanded))
        {
            Dungeon.hero.belongings.offhandweapon = (KindOfWeapon) curItem;
            GameScene.scene.offhandupdate();
        }

        else
        {
            if(!curItem.collect())
            {
                Dungeon.level.drop(curItem,end);
            }
        }

    }

    private CellSelector.Listener caster = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {

                curUser = Dungeon.hero;

                final Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.FRISBEE);
                final Integer cell = shot.collisionPos;
                //final Ballistica way = new Ballistica(curUser.pos, target, Ballistica.FRISBEE);
                final Ballistica way = new Ballistica(curUser.pos, cell, Ballistica.FRISBEE);

                ((SawtoothFrisbee)curItem).throwout(curUser.pos,cell,way );
            }
        }
        @Override
        public String prompt() {
            return Messages.get(SawtoothFrisbee.class, "prompt");
        }
    };
}
