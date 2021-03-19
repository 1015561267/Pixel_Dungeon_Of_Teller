package com.teller.pixeldungeonofteller.items.pages;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.buffs.Blindness;
import com.teller.pixeldungeonofteller.actors.buffs.Noise;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.particles.ElmoParticle;
import com.teller.pixeldungeonofteller.items.Bomb;
import com.teller.pixeldungeonofteller.items.Item;
import com.teller.pixeldungeonofteller.items.artifacts.UnstableSpellbook;
import com.teller.pixeldungeonofteller.items.food.Blandfruit;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.items.rings.RingOfMagic;
import com.teller.pixeldungeonofteller.items.scrolls.Scroll;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.levels.Terrain;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.HeroSprite;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.teller.pixeldungeonofteller.windows.WndBag;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class MagicPage extends Item {

    private static final String STOREDSPELL = "stored_spell";


    public Spell spell = null;
    public static final float TIME_TO_READ = 1;
    {
        stackable = true;
        defaultAction = AC_READ;
    }

    public MagicPage()
    {
        image = ItemSpriteSheet.NULLWARN;
    }

    public MagicPage(Spell spell)
    {
        this.spell=spell;
        image = spell.image;
    }

    public Item getspell(Spell sp)
    {
        spell = sp;
        image = sp.image;
        return this;
    }

    @Override
    public boolean isIdentified() { return true; }
    public boolean isUpgradable() {
        return false;
    }
    protected WndBag.Mode mode = WndBag.Mode.MAGICBOOK;
    public static final String AC_READ = "READ";
    public static final String AC_JOIN = "JOIN";

    @Override
    public boolean isSimilar(Item item) {
        if(item instanceof MagicPage)
        {
            return this.spell.getClass() == ((MagicPage) item).spell.getClass();
        }
        return false;
    }


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
                hero.buff(Noise.class).readScrollNoise();
                hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);
                this.detach(Dungeon.hero.belongings.backpack);
                spell.conjure(true , this);
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

    public String name() {
        return name + spell.name();
    }

    public String desc() {
        return Messages.get(this, "desc" ,spell.name()) + spell.desc();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STOREDSPELL, spell);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(STOREDSPELL)) {
             getspell((Spell) bundle.get(STOREDSPELL));
        }
    }
}
