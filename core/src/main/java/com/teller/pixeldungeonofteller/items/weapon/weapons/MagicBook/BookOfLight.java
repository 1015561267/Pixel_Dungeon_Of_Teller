package com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook;

import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.Flash;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.HolyBomb;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.Healing;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class BookOfLight extends MagicBook {

    private static final String SELECTEDSPELL = "SELECTEDSPELL";

    public BookOfLight()
    {
        image = ItemSpriteSheet.BOOKOFLIGHT;
        storedspells= new Spell[]{new Flash(),new Healing(),new HolyBomb()};
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
