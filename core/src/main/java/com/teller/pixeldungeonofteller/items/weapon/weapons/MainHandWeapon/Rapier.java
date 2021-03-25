package com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon;

import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Rapier extends Weapon {

    @Override
    public int stealth() {return 3;}

    public int Impactdamage(){return 0;}
    public int Slashdamage() {return 0;}
    public int Puncturedamage(){return Random.Int(3,20)+level()*Random.Int(1,4);}

    @Override
    public Type WeaponType() {
        return Type.MainHand;
    }

    {
        image = ItemSpriteSheet.RAPIER;
        tier = 3;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc"); }

    @Override
    public int min(int lvl) {
        return 3 + 1 * lvl;
    }

    @Override
    public int max(int lvl) {
        return 12 + 4 * lvl;
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
    public int STRMAXSCALE() {return 2;}
    @Override
    public int DEXMAXSCALE() {return 3;}
    public int DEXMINSCALE() { return 1; }

    public boolean attackable() {
        return true;
    }
}
