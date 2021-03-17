package com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.items.weapon.weapons.OffHandWeapon.OffHandWeapon;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class MagicBook extends OffHandWeapon {

    public static final String AC_CAST = "CAST";
    public static final String AC_SWITCH = "SWITCH";

    private static final String SELECTEDSPELL = "SELECTEDSPELL";

    private static final float TIME_TO_CAST = 1f;
    private static final float TIME_TO_SWITCH = 1f;

    public boolean attackable() {
        return false;
    }

    public Spell storedspells[];
    public Spell selectedspell;

    @Override
    public boolean isUpgradable()
    {return false;}

    public void joinNew(Spell spell)
    {
        storedspells[storedspells.length] = spell;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SELECTEDSPELL, selectedspell);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        selectedspell = (Spell)(bundle.get(SELECTEDSPELL));
        usesTargeting = selectedspell.usesTargeting;
        selftargeting = selectedspell.selftargeting;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if(Dungeon.hero.belongings.offhandweapon==this)
        {
            actions.add(AC_CAST);
            actions.add(AC_SWITCH);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero,action);
        if(action.equals(AC_SWITCH)||action.equals(AC_CAST)) {
            if (hero.belongings.offhandweapon == this) {
                if (action.equals(AC_CAST)) {
                    selectedspell.conjure(false);
                } else if (action.equals(AC_SWITCH)) {
                    int length = storedspells.length;
                    if (selectedspell.equals(storedspells[storedspells.length - 1])) {
                        selectedspell = storedspells[0];
                    } else {
                        for (int i = 0; i < length; i++) {
                            if (selectedspell.equals(storedspells[i]))
                            {
                                selectedspell = storedspells[i + 1];
                                break;
                            }
                        }
                    }
                    GameScene.scene.offhandupdate();
                    usesTargeting=selectedspell.usesTargeting;
                    selftargeting=selectedspell.selftargeting;
                    //FIXME:It is really stuipd to locate index of element in array but now that I want the spells in a sort(especially in the book of chaos) I can't think up other good data structure,considering the number of spells in a book is limited(<=4),the code below looks not so stupid XD.
                    hero.spendAndNext(TIME_TO_SWITCH);
                }
            } else {
                GLog.w(Messages.get(MagicBook.class, "unequip"));
            }
        }



    }

    @Override
    public String info() {
        String info = desc();

        info+="\n\n"+Messages.get(MagicBook.class, "all");
        for (int i = 0; i < storedspells.length; i++) {
            info+=storedspells[i].name();
            if(i!= storedspells.length-1)
            {
                info+=",";
            }
        }

        info+="\n\n"+Messages.get(MagicBook.class, "selected",selectedspell.name());
        info+="\n\n"+selectedspell.desc();
        info+="\n\n"+Messages.get(MagicBook.class, "manacost",selectedspell.ManaCost());
        return info;
    }

    public Class<? extends CharSprite> SpellSprite()
    {
        return selectedspell.Spellsprite();
    }

    public boolean fulfilled()
    {
        return storedspells.length>=3;
    }

    public boolean reiterated(Spell toadd)
    {
        for(Spell contained :storedspells)
        {
            if(toadd.equals(contained))
            {
                return true;
            }
        }
            return false;
    }
}
