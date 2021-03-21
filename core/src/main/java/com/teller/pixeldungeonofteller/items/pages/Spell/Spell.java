package com.teller.pixeldungeonofteller.items.pages.Spell;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.LockedFloor;
import com.teller.pixeldungeonofteller.actors.buffs.Recharging;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.watabou.utils.Bundle;

public class Spell extends Item {

    public int tier;

    protected int cooldown;
    protected int charge;

    public Class<? extends MagicSpellSprite> spriteClass;

    protected boolean usePage = false;//I have to add this to handle with spells which needs to aim,else the page would be lost if player cancel targeting

    public int ManaCost()
    {
        return 0;
    }
    protected CoolDowner charger;
    private static final String COOLDOWN = "COOLDOWN";
    private static final String CHARGE = "CHARGE";

    protected Buff passivebuff;

    public Class<? extends MagicSpellSprite> Spellsprite()
    {
        return spriteClass;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN, cooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldown=bundle.getInt(COOLDOWN);
    }


    @Override
    public int image() {
        return this.image;
    }

    @Override
    public boolean isIdentified() { return true; }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public boolean passive()
    {
        return false;
    }

    //public boolean multiplecharge(){return false;}

    //public void destory() {
    //    if(!passive()){
    //        if (charger != null) {
    //            charger.detach();
    //            charger = null;
    //        }
    //    }
    //    else
    //    {
    //        detachpassivebuff();
    //    }
    //}

    public boolean checkmana()
    { return Dungeon.hero.MANA>=ManaCost(); }

    public void conjure(boolean useMagicPage, MagicPage page) { }

    //public void charge(Char owner) {
    //        charger = new CoolDowner();
    //        charger.attachTo(owner);
    //}

    @Override
    public String desc()
    {
        return "\n"+super.desc();
    }

    public class CoolDowner extends Buff {
        @Override
        public boolean act() {
            recharge();
            spend(TICK);
            return true;
        }
        private void recharge() {
            if(cooldown>0) {
                LockedFloor lock = target.buff(LockedFloor.class);
                if (lock == null || lock.regenOn()) {
                    cooldown--;
                }
                Recharging bonus = target.buff(Recharging.class);
                if (bonus != null && bonus.remainder() > 0f) {
                    cooldown -= 8;
                }
            }
            if(cooldown<0) {cooldown=0;}
            updateQuickslot();
        }
    }

    public void passivebuffactivate(Char ch)
    {
        return ;
    }

    public void detachpassivebuff()
    {
        return;
    }

    public float fireAdaptability(){return 0;}
    public float iceAdaptability(){return 0;}
    public float lightningAdaptability(){return 0;}
    public float holyAdaptability(){return 0;}
    public float shadowAdaptability(){return 0;}
    public float natureAdaptability(){return 0;}
    public float arcaneAdaptability(){return 0;}

}
