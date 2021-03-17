package com.teller.pixeldungeonofteller.items.journal;

import com.teller.pixeldungeonofteller.journal.Document;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;

public class GuidePage extends DocumentPage {

    {
        image = ItemSpriteSheet.GUIDE_PAGE;
    }

    @Override
    public Document document() {
        return Document.ADVENTURERS_GUIDE;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", document().pageTitle(page()));
    }
}
