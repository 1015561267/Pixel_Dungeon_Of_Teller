package com.teller.pixeldungeonofteller.items;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.items.armor.Armor;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndBag;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class OverpricedSewingKit extends Item {

    private static final float TIME_TO_PATCH = 1;

    private static final String AC_PATCH = "PATCH";
    private final WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {
                OverpricedSewingKit.this.patch((Armor) item);
            }
        }
    };

    {
        stackable = true;
        image = ItemSpriteSheet.OVERPRICED_SEWING_KIT;
        defaultAction = AC_PATCH;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PATCH);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action == AC_PATCH) {
            curUser = hero;
            GameScene.selectItem(itemSelector, WndBag.Mode.ARMOR, Messages.get(this, "prompt"));
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private void patch(Armor armor) {
        if (curUser.belongings.armor == armor) {
            if (hero.ARMOR < armor.GETMAXARMOR()) {
                curUser.spend(TIME_TO_PATCH);
                curUser.busy();
                int patchedarmor = Math.min(armor.GETMAXARMOR() / 2, armor.GETMAXARMOR() - hero.ARMOR);
                hero.ARMOR += patchedarmor;
                GLog.w(Messages.get(this, "patched", armor.name()));
                detach(curUser.belongings.backpack);
                curUser.sprite.operate(curUser.pos);
                Sample.INSTANCE.play(Assets.SND_EVOKE);
            } else {
                GLog.w(Messages.get(this, "unpatched", armor.name()));
            }
        } else {
            if (armor.armor < armor.GETMAXARMOR()) {
                curUser.spend(TIME_TO_PATCH);
                curUser.busy();
                int patchedarmor = Math.min(armor.GETMAXARMOR() / 2, armor.GETMAXARMOR() - armor.armor);
                armor.armor += patchedarmor;
                GLog.w(Messages.get(this, "patched", armor.name()));
                detach(curUser.belongings.backpack);
                curUser.sprite.operate(curUser.pos);
                Sample.INSTANCE.play(Assets.SND_EVOKE);
            } else {
                GLog.w(Messages.get(this, "unpatched", armor.name()));

            }
        }
    }

    @Override
    public int price() {
        return 20 * quantity;
    }
}
