package com.teller.pixeldungeonofteller.items.journal;


import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.journal.Document;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.windows.WndJournal;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public abstract class DocumentPage extends Item {

    {
        image = ItemSpriteSheet.MASTERY;
    }

    public abstract Document document();

    private String page;

    public void page( String page ){
        this.page = page;
    }

    public String page(){
        return page;
    }

    @Override
    public final boolean doPickUp(Hero hero) {
        GameScene.pickUpJournal(this, hero.pos);
        GameScene.flashJournal();
        WndJournal.last_index = 0;
        document().addPage(page);
        Sample.INSTANCE.play( Assets.SND_ITEM );
        hero.spendAndNext( TIME_TO_PICK_UP );
        return true;
    }

    private static final String PAGE = "page";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( PAGE, page() );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        page = bundle.getString( PAGE );
    }
}

