package com.teller.pixeldungeonofteller.items.pages.Spell.OldBook;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.MagicalDamage;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.mechanics.Ballistica;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.OldBook.MagicMissleSprite;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.teller.pixeldungeonofteller.ui.OffHandIndicator;
import com.teller.pixeldungeonofteller.ui.QuickSlotButton;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class MagicMissile extends Spell {

    {
        name= Messages.get(this,"name");
        spriteClass = MagicMissleSprite.class;
        usesTargeting=true;
        image= ItemSpriteSheet.PAGE_MAGICMISSILE;
    }

    public Class<? extends MagicSpellSprite> Spellsprite()
    {
        return spriteClass;
    }

    public int ManaCost()
    {
        return 3;
    }

    private MagicPage page;

    public void conjure(boolean MagicPage, MagicPage p)
    {
        if (checkmana() || MagicPage) {
            if(MagicPage) { usePage = true; page = p;}
            curUser = Dungeon.hero;
            GameScene.selectCell(zapper);
        }
        else {
            GLog.w(Messages.get(MagicBook.class, "nomana"));
            OffHandIndicator.cancel();
        }
    }

    protected CellSelector.Listener zapper = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                final MagicMissile magicmissile = (MagicMissile) Spell.curItem;

                final Ballistica shot = new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT);
                int cell = shot.collisionPos;
                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i(Messages.get(MagicBook.class, "self_target"));
                    if(usePage) { new MagicPage(new MagicMissile()).collect(); usePage = false; }
                    return;
                }
                curUser.sprite.zap(target);
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(target));
                    curUser.busy();
                    magicmissile.fx(shot, new Callback() {
                        public void call() {
                            magicmissile.onZap(shot);
                        }
                    });
                    Invisibility.dispel();
            }
        }
        @Override
        public String prompt() {
            return Messages.get(MagicBook.class, "prompt");
        }
    };

    protected void fx(Ballistica bolt, Callback callback) {
        com.teller.pixeldungeonofteller.effects.MagicMissile.whiteLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    protected void onZap(Ballistica bolt) {
        int cell = bolt.collisionPos;
        Char ch = Actor.findChar(bolt.collisionPos);
        if (ch != null) {
            int damage = Random.NormalIntRange(min(), max());
            MagicalDamage dmg = new MagicalDamage();
            dmg.AddArcane(damage);
            ch.damage(dmg, this);
        }
        curUser.spendAndNext(1f);

        if(!usePage)
        {
            Dungeon.hero.MANA-=ManaCost();
        }
        else  {usePage=false; page.detach(Dungeon.hero.belongings.backpack);}
    }

    @Override
    public String desc()
    {
        return "\n"+Messages.get(this, "desc",min(),max());
    }

    public static int min(){return 2+Dungeon.hero.INT;}
    public static int max(){return 6+3*Dungeon.hero.INT;}

    public boolean equals(Object object)
    {
        return object instanceof MagicMissile;
    }
}
