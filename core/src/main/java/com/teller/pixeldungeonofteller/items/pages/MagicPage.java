package com.teller.pixeldungeonofteller.items.pages;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Blindness;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.particles.ElmoParticle;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.artifacts.UnstableSpellbook;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.HeroSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class MagicPage extends Item {
    {
        stackable = true;
        defaultAction = AC_READ;
    }

    public Spell spell;
    public static final float TIME_TO_READ = 1;

    protected WndBag.Mode mode = WndBag.Mode.SCROLL;
    public static final String AC_READ = "READ";
    public static final String AC_JOIN = "JOIN";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        actions.add(AC_JOIN);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_READ)) {
            if (hero.buff(Blindness.class) != null) {
                GLog.w(Messages.get(this, "blinded"));
            }
             else {
                curUser = hero;
                hero.buff(Noise.class).readScrollNoise();
                hero.busy();
                hero.spend(1f);
                hero.sprite.operate(hero.pos);
                ((HeroSprite) curUser.sprite).read();
                Sample.INSTANCE.play(Assets.SND_BURNING);
                hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);

                spell.conjure(true);
             }
        }
        else if(action.equals(AC_JOIN))
        {
            GameScene.selectItem(bookSelector, mode, Messages.get(this, "prompt"));
        }

    }

    protected WndBag.Listener bookSelector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null && item instanceof MagicBook) {
                if(((MagicBook) item).fulfilled())
                {
                    GLog.w(Messages.get(MagicPage.class, "fulfilled"));
                    return;
                }
                else if(((MagicBook) item).reiterated(spell))
                {
                    GLog.w(Messages.get(MagicPage.class, "reiterated"));
                    return;
                }
                Hero hero = Dungeon.hero;
                hero.busy();
                hero.spend(1f);
                hero.sprite.operate(hero.pos);
                ((HeroSprite) curUser.sprite).read();
                detach(hero.belongings.backpack);
                ((MagicBook) item).joinNew(spell);
                GLog.i(Messages.get(MagicPage.class, "joined",spell.name(),item.name()));
                return;
            }
        }
    };
}
