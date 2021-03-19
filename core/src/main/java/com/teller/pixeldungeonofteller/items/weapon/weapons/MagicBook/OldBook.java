package com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook;

import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.items.pages.Spell.OldBook.MagicMissile;
import com.teller.pixeldungeonofteller.items.pages.Spell.OldBook.LightUp;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class OldBook extends MagicBook {

    private static final String SELECTEDSPELL = "SELECTEDSPELL";
    {
        image = ItemSpriteSheet.OLDBOOK;
    }

    public MagicBook addRaw()
    {
        storedspells = new ArrayList<Spell>();

        storedspells.add(new MagicMissile());
        storedspells.add(new LightUp());

        if(selectedspell==null) {
            selectedspell = storedspells.get(0);
            usesTargeting=storedspells.get(0).usesTargeting;
            selftargeting=storedspells.get(0).selftargeting;
        }
        else
        {
            usesTargeting=selectedspell.usesTargeting;
            selftargeting=selectedspell.selftargeting;
        }
        return this;
    }
}
