package com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook;

import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.items.pages.Spell.OldBook.MagicMissile;
import com.teller.pixeldungeonofteller.items.pages.Spell.OldBook.LightUp;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class OldBook extends MagicBook {

    private static final String SELECTEDSPELL = "SELECTEDSPELL";

    public OldBook()
    {
        image = ItemSpriteSheet.OLDBOOK;
        storedspells= new Spell[]{new MagicMissile(),new LightUp()};
        if(selectedspell==null) {
            selectedspell = storedspells[0];
            usesTargeting=storedspells[0].usesTargeting;
            selftargeting=storedspells[0].selftargeting;
        }
        else
        {
            usesTargeting=selectedspell.usesTargeting;
            selftargeting=selectedspell.selftargeting;
        }
    }
}
