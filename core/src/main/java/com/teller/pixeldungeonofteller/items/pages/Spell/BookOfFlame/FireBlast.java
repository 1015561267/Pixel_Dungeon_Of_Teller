package com.teller.pixeldungeonofteller.items.pages.Spell.BookOfFlame;

import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class FireBlast extends Spell {

    private static final String COOLDOWN = "COOLDOWN";
    private static final String CHARGE = "CHARGE";
    {
        tier=4;
        name= Messages.get(this,"name");
        image= ItemSpriteSheet.FIREBLAST;
        cooldown=75;
    }

    @Override
    public String desc()
    {
        return Messages.get(this, "desc");
    }

    public boolean multiplecharge(){return true;}

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COOLDOWN, cooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldown=bundle.getInt(COOLDOWN);
    }
}

