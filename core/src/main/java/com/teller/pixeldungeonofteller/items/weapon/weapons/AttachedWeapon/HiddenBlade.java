package com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon;

import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class HiddenBlade extends AttachedWeapon {

    public int Impactdamage(){return Random.Int(3,20);}
    public int Slashdamage() {return Random.Int(1,3)+level()*Random.Int(1,3);}
    public int Puncturedamage(){return Random.Int(1,2);}

    {
        image = ItemSpriteSheet.HIDDENBLADE;
        tier = 2;
    }

    @Override
    public boolean isUpgradable() {
        return false;
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
    public String desc() {
        return Messages.get(this, "desc") ;    }

    @Override
    public int min(int lvl) {
        return 2;
    }
    @Override
    public int max(int lvl) {
        return 5;
    }

    @Override
    public int DEXReq(int lvl) {
        return 1;
    }
    public int DEXMINSCALE() { return 1; }
    public int DEXMAXSCALE() { return 3; }
}
