package com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.melee.MeleeWeapon;
import com.teller.pixeldungeonofteller.messages.Messages;

public class MainHandWeapon extends Weapon {
    public int tier;

    @Override
    public int stealth() {return 1;}

    @Override
    public int min(int lvl) {
        return tier +  //base
                lvl;    //level scaling
    }

    @Override
    public int max(int lvl) {
        return 5 * (tier + 1) +    //base
                lvl * (tier + 1);   //level scaling
    }

    public int STRReq(int lvl) {
        lvl = Math.max(0, lvl);
        //strength req decreases at +1,+3,+6,+10,etc.
        return (8 + tier * 2) - (int) (Math.sqrt(8 * lvl + 1) - 1) / 2;
    }

    public int DEXReq(int lvl) {
        return 1;
    }

    @Override
    public String info() {
        String info = desc();
        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, imbue.damageFactor(min()), imbue.damageFactor(max()), STRReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } //else if (Dungeon.hero.STR() > STRReq()){
            //info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            //}
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
        }
        info += "\n\n这是一把主手武器";
        String stats_desc = Messages.get(this, "stats_desc");
        if (!stats_desc.equals("") && !stats_desc.equals("!!!NO TEXT FOUND!!!"))
            info += "\n\n" + stats_desc;
        switch (imbue) {
            case LIGHT:
                info += "\n\n" + Messages.get(Weapon.class, "lighter");
                break;
            case HEAVY:
                info += "\n\n" + Messages.get(Weapon.class, "heavier");
                break;
            case NONE:
        }

        switch (stealth())
        {
            case 0: info += "\n\n" + Messages.get(Weapon.class, "no_stealth");break;
            case 1: info += "\n\n" + Messages.get(Weapon.class, "low_stealth");break;
            case 2: info += "\n\n" + Messages.get(Weapon.class, "normal_stealth");break;
            case 3: info += "\n\n" + Messages.get(Weapon.class, "high_stealth");break;
            case 4: info += "\n\n" + Messages.get(Weapon.class, "excellent_stealth");break;
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())) {
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }
        if (cursed && isEquipped(Dungeon.hero)) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        }
        return info;
    }

    @Override
    public int price() {
        int price = 20 * tier;
        if (hasGoodEnchant()) {
            price *= 1.5;
        }
        if (cursedKnown && (cursed || hasCurseEnchant())) {
            price /= 2;
        }
        if (levelKnown && level() > 0) {
            price *= (level() + 1);
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    @Override
    public boolean attackable() {
        return true;
    }
}