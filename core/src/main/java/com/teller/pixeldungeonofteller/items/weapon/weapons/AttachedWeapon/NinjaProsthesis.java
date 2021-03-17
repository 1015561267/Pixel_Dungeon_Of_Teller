package com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.AbsoluteDamage;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.PhysicalPercentage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.weapon.missiles.MissileWeapon;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.ui.QuickSlotButton;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class NinjaProsthesis extends AttachedWeapon {

    public static final String AC_SHOOT = "SHOOT";
    //protected int collisionProperties=Ballistica.PROJECTILE;
    private static final float TIME_TO_THROW = 1f;

    private static final String PRITICALCHARGE = "particalcharge";
    private static final String CHARGE = "charge";
    private static final String CHARGECAP = "chargecap";
    protected int collisionProperties = Ballistica.PROJECTILE;
    private int partialcharge;
    private int charge;
    private int chargecap;
    private Buff passivebuff;
    private HashSet<Integer> targetedCells;
    private int direction = 0;
    private int maxDist = 0;
    private CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                final Ballistica shot = new Ballistica(curUser.pos, target, collisionProperties);
                int cell = shot.collisionPos;
                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i(Messages.get(NinjaProsthesis.class, "self_target"));
                    return;
                }
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                if (Dungeon.level.distance(hero.pos, target) <= 3) {
                    if (Dungeon.level.adjacent(hero.pos, target)) {
                        maxDist = 4;
                    } else {
                        maxDist = 3;
                    }
                    if (charge >= 3) {
                        charge -= 3;
                        ThrowShuriken(shot, target);
                        hero.busy();
                        hero.spend(TIME_TO_THROW);
                    } else {
                        GLog.n("充能不足!");
                    }

                } else {
                    if (charge >= 4) {
                        charge -= 4;
                        ThrowaKunai(shot, target);
                        hero.busy();
                        hero.spend(TIME_TO_THROW);
                    } else {
                        GLog.n("充能不足!");
                    }
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(NinjaProsthesis.class, "prompt");
        }
    };
    //private static HashSet<Char> hittedtarget;

    {
        image = ItemSpriteSheet.NINJAPROSTHESIS;
        tier = 4;

        defaultAction = AC_SHOOT;
        usesTargeting = true;

        partialcharge = 0;
        chargecap = 25;
        charge = 10;
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
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
        bundle.put(CHARGECAP, chargecap);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt(CHARGE);
        chargecap = bundle.getInt(CHARGECAP);
    }

    public void activate(Char ch) {
        passivebuff = passiveBuff();
        passivebuff.attachTo(ch);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {
            passivebuff.detach();
            passivebuff = null;
            return true;
        } else {
            return false;
        }
    }

    protected Buff passiveBuff() {
        return new passivebuff();
    }

    @Override
    public String status() {
        return Messages.format("%d/%d", charge, chargecap);
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
        updateQuickslot();
        if (action.equals(AC_SHOOT)) {
            if (hero.belongings.offhandweapon == this) {
                curUser = hero;
                curItem = this;
                GameScene.selectCell(shooter);
            } else {
                GLog.n(Messages.get(this, "unequip"));
            }
        }
    }

    @Override
    public int STRReq(int lvl) {
        return 3;
    }

    @Override
    public int DEXReq(int lvl) {
        return 4;
    }

    @Override
    public String info() {
        String info = super.info();
        info += "\n手里剑可造成" + new Shuriken().min() + "~" + new Shuriken().max() + "伤害,实际造成的冲击:切割:穿刺比为:30%:70%:0%,每次消耗3能量";
        info += "\n苦无可造成" + new Kunai().min() + "~" + new Kunai().max() + "伤害,实际造成的冲击:切割:穿刺比为:40%:30%:30%,每次消耗4能量";
        info += "\n当前充能为:" + charge + "/" + chargecap;
        return info;
    }

    public Shuriken getShuriken() {
        return new Shuriken();
    }

    public Kunai getKunai() {
        return new Kunai();
    }

    private int left(int direction) {
        return direction == 0 ? 7 : direction - 1;
    }

    private int right(int direction) {
        return direction == 7 ? 0 : direction + 1;
    }

    public void ThrowShuriken(Ballistica bolt, Integer target) {
        //targetedCells.clear();
        targetedCells = new HashSet<>();
        int dist = Math.min(bolt.dist, maxDist);
        for (int i = 0; i < PathFinder.CIRCLE8.length; i++) {
            if (bolt.sourcePos + PathFinder.CIRCLE8[i] == bolt.path.get(1)) {
                direction = i;
                break;
            }
        }
        float strength = maxDist;
        if (strength == 3) {
            for (int c : bolt.subPath(1, dist)) {
                strength--;
                FindTarget(c + PathFinder.CIRCLE8[left(direction)], strength - 1f);
                FindTarget(c + PathFinder.CIRCLE8[direction], strength - 1);
                FindTarget(c + PathFinder.CIRCLE8[right(direction)], strength - 1f);
            }
        } else {
            for (int c : bolt.subPath(1, dist)) {
                strength--;
                FindTarget(c + PathFinder.CIRCLE8[left(direction)], strength - 2f);
                FindTarget(c + PathFinder.CIRCLE8[direction], strength - 1);
                FindTarget(c + PathFinder.CIRCLE8[right(direction)], strength - 2f);
            }
        }

        int shurikentothrow = 0;
        int shurikenunthrow = 3;

        for (int t : targetedCells) {
            if (!(new Ballistica(hero.pos, t, Ballistica.STOP_TARGET).collisionPos == t)) {
                shurikentothrow++;
                continue;
            } else {
                do {
                    getShuriken().cast(hero, t);
                    shurikenunthrow--;
                    shurikentothrow--;
                } while (shurikentothrow >= 0);
                shurikentothrow = 0;
            }
        }

        int unthrowedshuriken = 0;
        if (shurikenunthrow == 3) {
            unthrowedshuriken = 3;
            ThrowShurikenfx(bolt, target);
            return;
        } else {
            unthrowedshuriken += shurikenunthrow;
        }

        if (unthrowedshuriken > 0) {
            for (int t : targetedCells) {
                if ((new Ballistica(hero.pos, t, Ballistica.STOP_TARGET).collisionPos == t)) {
                    while (unthrowedshuriken > 0) {
                        getShuriken().cast(hero, t);
                        unthrowedshuriken--;
                    }
                    break;
                }
            }
        }
        targetedCells.clear();
    }

    public void ThrowaKunai(Ballistica shot, Integer target) {
        getKunai().cast(hero, target);
    }

    public void Shigan(Char enemy) {
        if (enemy != null) {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, "Shigan!");
            enemy.damage(new AbsoluteDamage(1), hero);
        }
    }

    public void ThrowShurikenfx(Ballistica bolt, Integer target) {
        final Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
        int cell = shot.collisionPos;
        targetedCells = new HashSet<>();
        maxDist = 2;
        int dist = Math.min(shot.dist, maxDist);
        float strength = maxDist;
        for (int c : shot.subPath(1, dist)) {
            strength--;
            FindTargetfx(c + PathFinder.CIRCLE8[left(direction)], strength - 1f);
            FindTargetfx(c + PathFinder.CIRCLE8[direction], strength - 1f);
            FindTargetfx(c + PathFinder.CIRCLE8[right(direction)], strength - 1f);
        }
        if (targetedCells.isEmpty()) {
            GLog.w("无法投掷!");
            return;
        }
        for (int t : targetedCells) {
            getShuriken().cast(hero, t);
        }
    }

    public boolean CheckPath(Integer target) {
        Ballistica attack = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);
        return attack.collisionPos == target;
    }

    public void FindTarget(Integer cell, float strength) {
        if (strength >= 0) {
            if (Dungeon.level.distance(hero.pos, cell) == 3) {
                targetedCells.add(cell);
            }
            if (strength >= 1.5f) {
                FindTarget(cell + PathFinder.CIRCLE8[left(direction)], strength - 1.5f);
                FindTarget(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
                FindTarget(cell + PathFinder.CIRCLE8[right(direction)], strength - 1.5f);
            }
        }
    }

    public void FindTargetfx(Integer cell, float strength) {
        if (strength >= 0) {
            if (Dungeon.level.distance(hero.pos, cell) == 2) {
                targetedCells.add(cell);
            }
            if (strength >= 1.5f) {
                FindTargetfx(cell + PathFinder.CIRCLE8[left(direction)], strength - 1.5f);
                FindTargetfx(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
                FindTargetfx(cell + PathFinder.CIRCLE8[right(direction)], strength - 1.5f);
            }
        }
    }

    public static class Shuriken extends MissileWeapon {
        static int IMPACTFACTOR = 30;
        static int SLASHFACTOR = 70;
        static int PUNCTUREFACTOR = 0;
        private PhysicalPercentage percentage() { return new PhysicalPercentage(0.3f,0.7f,0); }

        private Char enemy;

        {
            image = ItemSpriteSheet.SHURIKEN;
            DLY = 0f;
        }

        public Shuriken() {
            this(1);
        }

        public Shuriken(int number) {
            super();
            quantity = number;
        }

        @Override
        public String desc() {
            return "\n\n手里剑实际造成的冲击:切割:穿刺比为:\n" + IMPACTFACTOR + "%:" + SLASHFACTOR + "%:" + PUNCTUREFACTOR + "%" + "\n每次消耗3能量";
        }

        @Override
        public int min(int lvl) {
            return 1 + Dungeon.depth / 10;
        }

        @Override
        public int max(int lvl) {
            return 2 + Dungeon.depth / 2 + 2 * DEXFACTOR();
        }

        @Override
        public int STRReq(int lvl) {
            return 3;
        }

        @Override
        public int DEXReq(int lvl) {
            return 4;
        }

        @Override
        public Damage proc(Char attacker, Char defender, Damage damage) {
            return new PhysicalDamage(Random.NormalIntRange(min(), max()), percentage());
        }

        @Override
        protected void onThrow(int cell) {
            enemy = Actor.findChar(cell);
            if (enemy != null) {
                if (curUser.shoot(enemy, this)) {
                }
            }
        }
    }

    public class passivebuff extends Buff {
        public void gainCharge(float levelPortion) {
            if (charge < chargecap) {
                partialcharge += levelPortion * (8 + 2.5 * hero.DEX);
                while (partialcharge >= 1) {
                    charge++;
                    partialcharge -= 1;
                    if (charge == chargecap) {
                        partialcharge = 0;
                    }
                }
                updateQuickslot();
            } else
                partialcharge = 0;
        }
    }

    public class Kunai extends MissileWeapon {
        private PhysicalPercentage percentage() { return new PhysicalPercentage(0.4f,0.3f,0.3f); }

        int IMPACTFACTOR = 40;
        int SLASHFACTOR = 30;
        int PUNCTUREFACTOR = 30;
        private Char enemy;

        {
            image = ItemSpriteSheet.KUNAI;
            tier = 3;
            DLY = 0f;
        }

        @Override
        protected void onThrow(int cell) {
            enemy = Actor.findChar(cell);
            if (enemy != null) {
                if (curUser.shoot(enemy, this)) {

                }
            }
        }

        @Override
        public String desc() {
            return "\n\n苦无实际造成的冲击:切割:穿刺比为:\n" + IMPACTFACTOR + "%:" + SLASHFACTOR + "%:" + PUNCTUREFACTOR + "%" + "\n每次消耗4能量";
        }

        @Override
        public int min(int lvl) {
            return 3 + Dungeon.depth / 3;
        }

        @Override
        public int max(int lvl) {
            return 5 + Dungeon.depth / 2;
        }

        @Override
        public int STRReq(int lvl) {
            return 3;
        }

        @Override
        public int DEXReq(int lvl) {
            return 4;
        }

        public int DEXMAXSCALE() { return 4; }

        @Override
        public Damage proc(Char attacker, Char defender, Damage damage) {
            return new PhysicalDamage(Random.NormalIntRange(min(), max()), percentage());
        }
    }
}