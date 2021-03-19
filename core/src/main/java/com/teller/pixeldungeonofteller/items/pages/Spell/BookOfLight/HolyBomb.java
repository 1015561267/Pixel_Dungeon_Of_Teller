package com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight;

import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.items.Holybomb;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.BookOfLight.HolyBombSprite;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.teller.pixeldungeonofteller.sprites.MissileSprite;
import com.teller.pixeldungeonofteller.ui.OffHandIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Callback;

import static com.teller.pixeldungeonofteller.Dungeon.hero;

public class HolyBomb extends Spell {

    {
        name= Messages.get(this,"name");
        image= ItemSpriteSheet.PAGE_HOLYBOMB;
        spriteClass = HolyBombSprite.class;
        usesTargeting=true;
    }

    @Override
    public String desc()
    {
        return "\n"+Messages.get(this, "desc",3,min(),max());
    }

    @Override
    public Class<? extends MagicSpellSprite> Spellsprite() {
        return spriteClass;
    }

    public static int min(){return 4+ 2* hero.INT();}

    public static int max(){return 22+ 8* hero.INT();}

    public boolean equals(Object object)
    {
        return object instanceof HolyBomb;
    }

    public int ManaCost()
    {
        return 15;
    }

    public void conjure(boolean useMagicPage, MagicPage p)
    {
        if (checkmana() || useMagicPage) {
            if(useMagicPage) { useMagicpage = true;
            }
            GameScene.selectCell(thrower);
        }
        else {
            GLog.w(Messages.get(MagicBook.class, "nomana"));
            OffHandIndicator.cancel();
        }
    }

    CellSelector.Listener thrower = new CellSelector.Listener() {
        @Override
        public void onSelect(final Integer target) {
            if (target != null) {
                if(!useMagicpage)
                { hero.MANA-=ManaCost(); }
                useMagicpage=false;

                final Ballistica shot = new Ballistica(hero.pos,target, Ballistica.PROJECTILE);
                final int cell = shot.collisionPos;
                Invisibility.dispel();
                final Holybomb holybomb=new Holybomb();
                ((MissileSprite)  hero.sprite.parent.recycle(MissileSprite.class)).
                        reset( hero.pos, cell, holybomb, new Callback() {
                            @Override
                            public void call() {
                               holybomb.onThrow(cell);
                               hero.spendAndNext(1f);
                            }
                        });
            }
        }

        @Override
        public String prompt() {
            return Messages.get(MagicBook.class, "prompt");
        }
    };
}
