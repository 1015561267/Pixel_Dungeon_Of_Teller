package com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook;

import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.Flash;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.HolyBomb;
import com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight.Healing;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class BookOfLight extends MagicBook {

    private static final String SELECTEDSPELL = "SELECTEDSPELL";

    {
        image = ItemSpriteSheet.BOOKOFLIGHT;
    }

    public MagicBook addRaw()
    {
        storedspells = new ArrayList<Spell>();

        storedspells.add(new Flash());
        storedspells.add(new Healing());
        storedspells.add(new HolyBomb());

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
