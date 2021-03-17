package com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon;

import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Wakizashi extends OffHandWeapon {

    public int Slashdamage(){return  Random.Int(2,8)+level()* Random.Int(1,2);}
    public int Puncturedamage() {return Random.Int(1,6)+level()* 1;}

    {
        image = ItemSpriteSheet.WAKIZASHI;
        tier = 3;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") ;    }

    @Override
    public int min(int lvl) {
        return 3 + 2 * lvl;
    }

    @Override
    public int max(int lvl) {
        return 14 + 3 * lvl;
    }

    @Override
    public int STRReq(int lvl) {
        return 3;
    }

    @Override
    public int DEXReq(int lvl) {
        return 4;
    }

    public int DEXMINSCALE() { return 1; }

    public int STRMAXSCALE() { return 1; }

    public int DEXMAXSCALE() { return 4; }

    @Override
    public float cooldown() {
        return 20f;
    }

    public boolean attackable() {
        return true;
    }
}
