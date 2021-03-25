package com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class Tonfa extends Weapon {

    @Override
    public Type WeaponType() {
        return Type.DualWield;
    }

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

        boolean same1 = false;
        boolean same2 = false;
        boolean dual1 = false;
        boolean dual2 = false;

        if (hero.belongings.mainhandweapon!=null)
        {
            if(hero.belongings.mainhandweapon instanceof Tonfa){
                same1 = true;
            }
            else if(hero.belongings.mainhandweapon.WeaponType() == Type.DualWield)
            {
                dual1 = true;
            }
        }

        if (hero.belongings.offhandweapon!=null)
        {
            if(hero.belongings.offhandweapon instanceof Tonfa){
                same2 = true;
            }
            else if(hero.belongings.offhandweapon.WeaponType() == Type.DualWield)
            {
                dual2 = true;
            }
        }

        if(same1&&same2) { return 20f; }
        else if(dual1&&dual2) { return 20f; }
        else return 20f;
    }
}
