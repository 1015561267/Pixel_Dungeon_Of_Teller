package com.teller.pixeldungeonofteller.items.weapon.weapons.FireArm;

import com.teller.pixeldungeonofteller.items.KindOfWeapon;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;

public abstract class FireArm extends Weapon{

    Weapon type() { return null; }

    @Override
    public int STRReq(int lvl) { return 0; }

    @Override
    public int DEXReq(int lvl) { return 0; }

    @Override
    public int min(int lvl) { return 0; }

    @Override
    public int max(int lvl) { return 0; }


}
