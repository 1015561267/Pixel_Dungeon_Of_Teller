package com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.curses.Wayward;
import com.teller.pixeldungeonofteller.items.weapon.melee.MeleeWeapon;
import com.teller.pixeldungeonofteller.messages.Messages;

public class DualWieldWeapon extends Weapon {
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

    public int STRReq(int lvl) {
        lvl = Math.max(0, lvl);
        //strength req decreases at +1,+3,+6,+10,etc.
        return (8 + tier * 2) - (int) (Math.sqrt(8 * lvl + 1) - 1) / 2;
    }

    public int DEXReq(int lvl) {
        lvl = Math.max(0, lvl);
        return (8 + tier * 2) - (int) (Math.sqrt(8 * lvl + 1) - 1) / 2;
    }

    @Override
    public String info() {
        String info = desc();

        if (levelKnown) {
            info += "\n\n" + Messages.get(this, "stats_known", tier, imbue.damageFactor(min()), imbue.damageFactor(max()), STRReq(), DEXReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += "\n " + Messages.get(Weapon.class, "too_heavy");
            }
            if (DEXReq() > Dungeon.hero.DEX()) {
                info += "\n " + Messages.get(Weapon.class, "no_enough_dex");
            }
            //else if (Dungeon.hero.STR() > STRReq()){
            //info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            //}
        } else {
            info += "\n\n" + Messages.get(this, "stats_unknown", tier, min(0), max(0), STRReq(0), DEXReq());
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
            if (DEXReq(0) > Dungeon.hero.DEX()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_no_enough_dex");
            }
        }
        info += "\n这是一把双持武器";
        String stats_desc = Messages.get(this, "stats_desc");

        switch (stealth())
        {
            case 0: info += "\n\n" + Messages.get(Weapon.class, "no_stealth");break;
            case 1: info += "\n\n" + Messages.get(Weapon.class, "low_stealth");break;
            case 2: info += "\n\n" + Messages.get(Weapon.class, "normal_stealth");break;
            case 3: info += "\n\n" + Messages.get(Weapon.class, "high_stealth");break;
            case 4: info += "\n\n" + Messages.get(Weapon.class, "excellent_stealth");break;
        }

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

    public float cooldown() {
        return 0;
    }

    @Override
    public float accuracyFactor(Hero hero) {
        int encumbrance = DEXReq() - hero.DEX();
        if (hasEnchant(Wayward.class))
            encumbrance = Math.max(3, encumbrance + 3);
        float ACC = this.ACC;
        //if (this instanceof Tamahawk) {
        //   int bonus = RingOfSharpshooting.getBonus(hero, RingOfSharpshooting.Aim.class);
        //   ACC *= (float)(Math.pow(1.2, bonus));
        //}
        return encumbrance > 0 ? (float) (ACC / Math.pow(1.5, encumbrance)) : ACC;
    }
}