package com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon;

import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.NinjaProsthesis;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.ui.QuickSlotButton;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class JavelinBarrel extends Weapon {

    private static final float TIME_TO_RELOAD = 1;
    private static final String AMMO = "ammo";
    private static final String AC_SHOOT = "SHOOT";
    private static final String AC_RELOAD = "RELOAD";
    private int ammo;

    @Override
    public Type WeaponType() {
        return Type.OffHand;
    }

    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {

                final Ballistica shot = new Ballistica(hero.pos,target, Ballistica.PROJECTILE);
                final int cell = shot.collisionPos;

                if (cell == curUser.pos) {
                    GLog.i(Messages.get(NinjaProsthesis.class, "self_target"));
                    return;
                }
                if (Actor.findChar(cell) != null)
                    QuickSlotButton.target(Actor.findChar(cell));
                //else
                //  QuickSlotButton.target(Actor.findChar(cell));
                ammo--;
                new Javelin().cast(hero, cell);
                hero.busy();
                hero.spend(TIME_TO_THROW);
                //updateQuickslot();
            }
        }

        @Override
        public String prompt() {
            return Messages.get(JavelinBarrel.class, "prompt");
        }
    };

    {
        image = ItemSpriteSheet.JAVELINBARREL;
        defaultAction = AC_SHOOT;
        tier = 4;
        ammo = 0;
    }

    @Override
    public int STRReq(int lvl) {
        return 2;
    }

    @Override
    public int DEXReq(int lvl) {
        return 5;
    }

    @Override
    public boolean doEquip(final Hero hero) {
        if (super.doEquip(hero)) {
            identify();
            return true;
        } else {
            return false;
        }
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
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.belongings.offhandweapon == this) {
            actions.add(AC_RELOAD);
            if (ammo > 0) {
                actions.add(AC_SHOOT);
            }
        }
        return actions;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(AMMO, ammo);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        ammo = bundle.getInt(AMMO);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        updateQuickslot();
        if (action.equals(AC_RELOAD)) {
            if (hero.belongings.offhandweapon == this) {
                if (ammo < 3) {
                    curUser.sprite.operate(curUser.pos);
                    curUser.spend(TIME_TO_RELOAD);
                    curUser.busy();
                    ammo = 3;
                    updateQuickslot();
                } else {
                    GLog.w(Messages.get(JavelinBarrel.class, "fullammo"));
                }
            } else {
                GLog.w(Messages.get(JavelinBarrel.class, "needtoequip"));
            }
            updateQuickslot();
        } else if (action.equals(AC_SHOOT)) {
            if (hero.belongings.offhandweapon == this) {
                if (ammo > 0) {
                    curUser = hero;
                    curItem = this;
                    GameScene.selectCell(shooter);
                } else {
                    GLog.w(Messages.get(JavelinBarrel.class, "outofammo"));
                    execute(hero, AC_RELOAD);
                }
            } else {
                GLog.w(Messages.get(JavelinBarrel.class, "needtoequip"));
            }
        }
        updateQuickslot();
    }

    @Override
    public String info() {
        String info = super.info();
        //info += "\n\n标枪可造成" + new Javelin().min() + "~" + new Javelin().max() + "点伤害";
        info += "\n\n标枪只会造成穿刺伤害";
        return info;
    }

    @Override
    public String status() {
        return Messages.format("%d/%d", ammo, 3);
    }

    public static class Javelin extends Item {
        {
            image = ItemSpriteSheet.JAVELIN;
        }

        @Override
        public String desc() {
            return "\n\n标枪只造成穿刺伤害";
        }
    }
}
