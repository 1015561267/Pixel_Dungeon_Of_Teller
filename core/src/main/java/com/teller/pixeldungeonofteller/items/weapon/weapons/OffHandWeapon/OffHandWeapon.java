package com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.melee.MeleeWeapon;
import com.teller.pixeldungeonofteller.messages.Messages;

public class OffHandWeapon extends Weapon {
    public int tier;

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

    public int DEXReq(int lvl) {
        return 1;
    }

    public int STRReq(int lvl) {
        return 1;
    }

    @Override
    public String info() {
        String info = desc();
        if (levelKnown) {
            if (attackable()) {
                info += "\n\n" + Messages.get(OffHandWeapon.class, "stats_known_attackable", tier, imbue.damageFactor(min()), imbue.damageFactor(max()), DEXReq());
            } else {
                info += "\n\n" + Messages.get(OffHandWeapon.class, "stats_known_unattackable", tier, DEXReq());
            }
            if (DEXReq() > Dungeon.hero.DEX()) {
                info += "\n\n" + Messages.get(MeleeWeapon.class, "no_enough_dex");
            }
        } else {
            if (attackable()) {
                info += "\n\n" + Messages.get(OffHandWeapon.class, "stats_unknown_attackable", tier, min(0), max(0), DEXReq(0));
            } else {
                info += "\n\n" + Messages.get(OffHandWeapon.class, "stats_unknown_unattackable", tier, DEXReq(0));
            }
            if (DEXReq(0) > Dungeon.hero.DEX()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_no_enough_dex");
            }
        }
        info += "\n这是一把副手武器";
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

    public boolean attackable() {
        return false;
    }
}
