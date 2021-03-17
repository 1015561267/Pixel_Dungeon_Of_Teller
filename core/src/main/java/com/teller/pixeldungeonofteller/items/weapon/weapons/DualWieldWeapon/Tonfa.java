package com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Tonfa extends DualWieldWeapon {

    @Override
    public int stealth() {return 2;}

    public int Impactdamage(){return Random.Int(4,18)+level()*Random.Int(1,4);}
    public int Slashdamage() {return 0;}
    public int Puncturedamage(){return Random.Int(2,10)+level()*Random.Int(1,1);}

    {
        image = ItemSpriteSheet.TONFA;
        tier = 4;
        DLY = 0.8f;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc") ;    }

    @Override
    public int min(int lvl) {
        return 6 + 2 *lvl;
    }

    @Override
    public int max(int lvl) {
        return 28 + 5 * lvl;
    }

    @Override
    public int STRReq(int lvl) {
        return 5;
    }

    @Override
    public int DEXReq(int lvl) {
        return 3;
    }

    public int STRMINSCALE() { return 1; }
    public int DEXMINSCALE() { return 1; }
    public int STRMAXSCALE() { return 2; }
    public int DEXMAXSCALE() { return 2; }

    @Override
    public boolean attackable() {
        return true;
    }

    @Override
    public float cooldown() {
        if (Dungeon.hero.belongings.mainhandweapon instanceof Tonfa) {
            return 20f;
        } else if (Dungeon.hero.belongings.mainhandweapon instanceof DualWieldWeapon) {
            return 20f;
        } else return 20f;
    }
}
