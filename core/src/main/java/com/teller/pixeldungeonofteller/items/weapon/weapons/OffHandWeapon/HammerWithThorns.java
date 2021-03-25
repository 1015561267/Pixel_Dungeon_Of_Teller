package com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon;

import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class HammerWithThorns extends Weapon {

    public int Impactdamage(){return Random.Int(2,10)+level()*Random.Int(1,5);}
    public int Slashdamage() {return Random.Int(1,4);}
    public int Puncturedamage(){return 0;}

    @Override
    public Type WeaponType() {
        return Type.OffHand;
    }

    {
        image = ItemSpriteSheet.HAMMERWITHTHORNS;
        tier = 2;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");    }

    @Override
    public int min(int lvl) {
        return 2 + 1 * lvl;
    }

    @Override
    public int max(int lvl) {
        return 13 + 5 * lvl;
    }

    @Override
    public int STRReq(int lvl) {
        return 3;
    }

    @Override
    public int DEXReq(int lvl) {
        return 2;
    }

    public int STRMINSCALE() { return 1; }

    public int STRMAXSCALE() { return 4; }

    public int DEXMAXSCALE() { return 2; }

    @Override
    public float cooldown() {
        return 40f;
    }

    public boolean attackable() {
        return true;
    }
}
