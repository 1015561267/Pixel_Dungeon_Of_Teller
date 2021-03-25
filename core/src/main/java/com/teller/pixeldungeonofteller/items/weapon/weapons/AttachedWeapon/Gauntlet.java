package com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon;

import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Gauntlet extends Weapon {

    {
        image = ItemSpriteSheet.GAUNTLET;
        tier = 2;
    }

    public Weapon.Type WeaponType()
    {
        return Weapon.Type.Attached;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public String info() {
        String info = super.info();
        info += "\n\n" + Messages.get(this, "aboutblock", BlockChance(), MinBlock(), MaxBlock());
        return info;
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
    public int min(int lvl) {
        return 0;
    }

    @Override
    public int max(int lvl) {
        return 0;
    }

    public int STRReq(int lvl) {
        return 1;
    }

    public int DEXReq(int lvl) {
        return 1;
    }

    public int BlockChance() {
        return 20 + 8 * DEXFACTOR();
    }

    private int BlockNumber() {
        return Random.Int(MinBlock(), MaxBlock());
    }

    private int MaxBlock() {
        return 2 * DEXFACTOR() + 1;
    }

    private int MinBlock() {
        return 1;
    }

    public PhysicalDamage damageproc(PhysicalDamage damage) {
        int absorbdamage = BlockNumber();
        if (absorbdamage > damage.IMPACTDAMAGE) {
            absorbdamage -= damage.IMPACTDAMAGE;
            damage.IMPACTDAMAGE = 0;

            if (absorbdamage > damage.PUNCTUREDAMAGE) {
                absorbdamage -= damage.PUNCTUREDAMAGE;
                damage.PUNCTUREDAMAGE = 0;
                damage.SLASHDAMAGE -= absorbdamage;
                if (damage.SLASHDAMAGE < 0)
                    damage.SLASHDAMAGE = 0;
            } else {
                damage.PUNCTUREDAMAGE -= absorbdamage;
            }
        } else {
            damage.IMPACTDAMAGE -= absorbdamage;
        }
        return damage;
    }
    //can not attack but has almost same machine of armor,maybe union code later

    public enum Type {
        Attached
    }

}
