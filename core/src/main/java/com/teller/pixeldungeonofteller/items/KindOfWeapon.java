/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.teller.pixeldungeonofteller.items;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.Damage;
import com.teller.pixeldungeonofteller.actors.PhysicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.CombinationCoolDown;
import com.teller.pixeldungeonofteller.actors.buffs.CombinationReady;
import com.teller.pixeldungeonofteller.actors.buffs.Guard;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.AttachedWeapon.AttachedWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.DualWieldWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MainHandWeapon.MainHandWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.OffHandWeapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.TwoHandedWeapon.TwoHandedWeapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.ui.StatusPane;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndOptions;

abstract public class KindOfWeapon extends EquipableItem {
    protected static final float TIME_TO_EQUIP = 1f;

    @Override
    public boolean isEquipped(Hero hero) {
        return hero.belongings.mainhandweapon == this || hero.belongings.offhandweapon == this;
    }

    @Override
    public boolean doEquip(final Hero hero) {
        detachAll(hero.belongings.backpack);
        if (this instanceof MainHandWeapon) {
            if (hero.belongings.mainhandweapon == null || hero.belongings.mainhandweapon.doUnequip(hero, true)) {
                hero.belongings.mainhandweapon = this;
                activate(hero);
                updateQuickslot();
                cursedKnown = true;
                if (cursed) {
                    equipCursed(hero);
                    GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                }
                hero.spendAndNext(TIME_TO_EQUIP);
                return true;
            } else {
                collect(hero.belongings.backpack);
                return false;
            }
        } else if (this instanceof OffHandWeapon) {
            if (hero.belongings.offhandweapon == null || hero.belongings.offhandweapon.doUnequip(hero, true)) {
                if (hero.belongings.mainhandweapon instanceof TwoHandedWeapon && (!hero.belongings.mainhandweapon.doUnequip(hero, true))) {
                    collect(hero.belongings.backpack);
                    return false;
                } else {
                    if (hero.belongings.mainhandweapon instanceof TwoHandedWeapon) {
                        hero.belongings.mainhandweapon.doUnequip(hero, true);
                    }
                    hero.belongings.offhandweapon = this;
                    activate(hero);
                    updateQuickslot();
                    cursedKnown = true;
                    if (cursed) {
                        equipCursed(hero);
                        GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                    }
                    hero.spendAndNext(TIME_TO_EQUIP);
                    if (this.attackable()) {
                        Buff.affect(Dungeon.hero, CombinationCoolDown.class);
                        Dungeon.hero.buff(CombinationCoolDown.class).set(hero.belongings.offhandweapon.cooldown());
                    }

                    if(this instanceof MagicBook)
                    {
                        GameScene.scene.offhandupdate();
                    }
                    return true;
                }
            } else collect(hero.belongings.backpack);
            return false;
        } else if (this instanceof TwoHandedWeapon) {
            if (hero.belongings.offhandweapon instanceof AttachedWeapon) {
                if ((hero.belongings.mainhandweapon == null || hero.belongings.mainhandweapon.doUnequip(hero, true))) {
                    hero.belongings.mainhandweapon = this;
                    activate(hero);
                    updateQuickslot();
                    cursedKnown = true;
                    if (cursed) {
                        equipCursed(hero);
                        GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                    }
                    hero.spendAndNext(TIME_TO_EQUIP);
                    return true;
                } else {
                    collect(hero.belongings.backpack);
                    return false;
                }
            } else if ((hero.belongings.mainhandweapon == null && hero.belongings.offhandweapon == null)) {
                hero.belongings.mainhandweapon = this;
                activate(hero);
                updateQuickslot();
                cursedKnown = true;
                if (cursed) {
                    equipCursed(hero);
                    GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                }
                hero.spendAndNext(TIME_TO_EQUIP);
                return true;
            } else if (hero.belongings.offhandweapon == null) {
                if (hero.belongings.mainhandweapon.doUnequip(hero, true)) {
                    hero.belongings.mainhandweapon = this;
                    activate(hero);
                    updateQuickslot();
                    cursedKnown = true;
                    if (cursed) {
                        equipCursed(hero);
                        GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                    }
                    hero.spendAndNext(TIME_TO_EQUIP);
                    return true;
                } else {
                    collect(hero.belongings.backpack);
                    return false;
                }
            } else if (hero.belongings.mainhandweapon == null) {
                if (hero.belongings.offhandweapon.doUnequip(hero, true)) {
                    hero.belongings.mainhandweapon = this;
                    activate(hero);
                    updateQuickslot();
                    cursedKnown = true;
                    if (cursed) {
                        equipCursed(hero);
                        GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                    }
                    hero.spendAndNext(TIME_TO_EQUIP);
                    return true;
                } else {
                    collect(hero.belongings.backpack);
                    return false;
                }
            } else {
                if (hero.belongings.mainhandweapon.doUnequip(hero, true) && hero.belongings.offhandweapon.doUnequip(hero, true)) {
                    hero.belongings.mainhandweapon = this;
                    activate(hero);
                    updateQuickslot();
                    cursedKnown = true;
                    if (cursed) {
                        equipCursed(hero);
                        GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                    }
                    hero.spendAndNext(TIME_TO_EQUIP);
                    return true;
                } else
                    collect(hero.belongings.backpack);
                return false;
            }
        } else if (this instanceof DualWieldWeapon) {
            GameScene.show(
                    new WndOptions(Messages.get(KindOfWeapon.class, "dualweild"),
                            Messages.get(KindOfWeapon.class, "detail"),
                            Messages.get(KindOfWeapon.class, "mainhand"),
                            Messages.get(KindOfWeapon.class, "offhand")) {
                        @Override
                        protected void onSelect(int index) {
                            switch (index) {
                                case 0:
                                    doEquipMainHand(hero);
                                    break;
                                case 1:
                                    doEquipOffHand(hero);
                                    break;
                                default:
                                    collect(hero.belongings.backpack);
                                    break;
                            }
                        }

                        public void onBackPressed() {
                        }

                    });
        } else if (this instanceof AttachedWeapon) {
            if (hero.belongings.offhandweapon == null || hero.belongings.offhandweapon.doUnequip(hero, true)) {
                hero.belongings.offhandweapon = this;
                activate(hero);
                updateQuickslot();
                cursedKnown = true;
                if (cursed) {
                    equipCursed(hero);
                    GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                }
                hero.spendAndNext(TIME_TO_EQUIP);
                return true;
            } else collect(hero.belongings.backpack);
            return false;
        } else collect(hero.belongings.backpack);
        return false;
    }

    public boolean doEquipMainHand(Hero hero) {
        if ((hero.belongings.mainhandweapon == null || hero.belongings.mainhandweapon.doUnequip(hero, true))) {
            hero.belongings.mainhandweapon = this;
            activate(hero);
            updateQuickslot();
            cursedKnown = true;
            if (cursed) {
                equipCursed(hero);
                GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
            }
            hero.spendAndNext(TIME_TO_EQUIP);
            GameScene.scene.updateweaponindicator((Weapon)this,true);
            return true;
        } else {
            collect(hero.belongings.backpack);
            return false;
        }
    }

    public boolean doEquipOffHand(Hero hero) {
        if (hero.belongings.offhandweapon == null || hero.belongings.offhandweapon.doUnequip(hero, true)) {
            if (hero.belongings.mainhandweapon instanceof TwoHandedWeapon && (!hero.belongings.mainhandweapon.doUnequip(hero, true))) {
                collect(hero.belongings.backpack);
                return false;
            } else {
                if (hero.belongings.mainhandweapon instanceof TwoHandedWeapon) {
                    hero.belongings.mainhandweapon.doUnequip(hero, true);
                }
                hero.belongings.offhandweapon = this;
                activate(hero);
                updateQuickslot();
                cursedKnown = true;
                if (cursed) {
                    equipCursed(hero);
                    GLog.n(Messages.get(KindOfWeapon.class, "cursed"));
                }
                hero.spendAndNext(TIME_TO_EQUIP);
                if (this.attackable()) {
                    Buff.affect(Dungeon.hero, CombinationCoolDown.class);
                    Dungeon.hero.buff(CombinationCoolDown.class).set(hero.belongings.offhandweapon.cooldown());
                }
                GameScene.scene.updateweaponindicator((Weapon)this,false);
                return true;
            }
        } else {
            collect(hero.belongings.backpack);
            return false;
        }
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)) {
            updateQuickslot();
            if (this instanceof MainHandWeapon) {
                hero.belongings.mainhandweapon = null;
                this.collect();
                return true;
            } else if (this instanceof OffHandWeapon) {
                hero.belongings.offhandweapon = null;
                this.collect();
                if (hero.buff(CombinationCoolDown.class) != null)
                    Buff.detach(hero, CombinationCoolDown.class);
                if (hero.buff(CombinationReady.class) != null)
                    Buff.detach(hero, CombinationReady.class);
                if (hero.buff(Guard.class) != null) Buff.detach(hero, Guard.class);
                return true;
            } else if (this instanceof TwoHandedWeapon) {
                hero.belongings.mainhandweapon = null;
                this.collect();
                return true;
            } else if (this instanceof DualWieldWeapon) {
                if (hero.belongings.mainhandweapon == this) {
                    hero.belongings.mainhandweapon = null;
                    return true;
                } else if (hero.belongings.offhandweapon == this) {
                    hero.belongings.offhandweapon = null;
                    if (hero.buff(CombinationCoolDown.class) != null)
                        Buff.detach(hero, CombinationCoolDown.class);
                    if (hero.buff(CombinationReady.class) != null)
                        Buff.detach(hero, CombinationReady.class);
                    return true;
                }
                return false;
            } else if (this instanceof AttachedWeapon) {
                hero.belongings.offhandweapon = null;
                this.collect();
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public int min() {
        return min(level());
    }

    public int max() {
        return max(level());
    }

    abstract public int min(int lvl);

    abstract public int max(int lvl);

    public PhysicalDamage damageRoll(Char owner) {
        return new PhysicalDamage();
    }
    //public PhysicalDamage damageRoll( Hero owner ) {
    //	return new PhysicalDamage();
    //}

    public float accuracyFactor(Hero hero) {
        return 1f;
    }

    public float speedFactor(Hero hero) {
        return 1f;
    }

    public int reachFactor(Hero hero) {
        return 1;
    }

    public int defenseFactor(Hero hero) {
        return 0;
    }

    public Damage proc(Char attacker, Char defender, Damage damage) {
        return damage;
    }

    public boolean attackable() {
        return false;
    }

    public float cooldown() {
        return 0;
    }

    public int stealth() {return 0;}
}
