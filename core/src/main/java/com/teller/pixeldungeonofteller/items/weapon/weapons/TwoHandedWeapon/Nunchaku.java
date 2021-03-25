package com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon;

import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Nunchaku extends Weapon {

    @Override
    public int stealth() {return 2;}

    public int Impactdamage(){return Random.Int(3,15)+level()*Random.Int(1,2);}
    public int Slashdamage() {return 0;}
    public int Puncturedamage(){return 0;}

    @Override
    public Type WeaponType() {
        return Type.TwoHanded;
    }

    {
        image = ItemSpriteSheet.NUNCHAKU;
        tier = 3;

        DLY = 0.5f; //2x speed
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") ;   }

    @Override
    public int min(int lvl) {
        return 3 + 1 * lvl;
    }

    @Override
    public int max(int lvl) {
        return 15 + 2 * lvl;
    }

    @Override
    public int STRReq(int lvl) {
        return 2;
    }

    @Override
    public int DEXReq(int lvl) {
        return 0;
    }

    public int DEXMINSCALE() { return 1; }
    public int STRMAXSCALE() { return 1; }
    public int DEXMAXSCALE() { return 2; }
}
