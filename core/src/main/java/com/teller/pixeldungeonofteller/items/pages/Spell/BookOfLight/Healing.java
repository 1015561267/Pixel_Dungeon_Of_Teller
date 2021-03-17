package com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Charm;
import com.teller.pixeldungeonofteller.actors.buffs.Corruption;
import com.teller.pixeldungeonofteller.actors.buffs.HolyHealing;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.CellSelector;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.BookOfLight.HealingSprite;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.teller.pixeldungeonofteller.ui.OffHandIndicator;
import com.teller.pixeldungeonofteller.utils.GLog;

public class Healing extends Spell {

    {
        name= Messages.get(this,"name");
        image= ItemSpriteSheet.HOLYHEAL;
        spriteClass = HealingSprite.class;
        usesTargeting = true;
        selftargeting = true;
    }

    @Override
    public String desc()
    {
        return Messages.get(this, "desc",healingsum(),healingeach());
    }

    @Override
    public Class<? extends MagicSpellSprite> Spellsprite() {
        return spriteClass;
    }

    public static int healingsum() { return 2+3*Dungeon.hero.INT(); }

    public static int healingeach() {return (int)Math.floor(1+Dungeon.hero.INT()/3); }

    public boolean equals(Object object)
    {
        return object instanceof Healing;
    }

    public int ManaCost()
    {
        return 8;
    }

    public void conjure(boolean useMagicPage)
    {
        if(checkmana())
        {
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

                Char ch=Actor.findChar(target);

                boolean effictive=false;

                if((ch!=null))
                {
                    if(ch instanceof Hero)
                    {
                        effictive=true;
                    }
                    else if(ch.properties().contains(Char.Property.UNDEAD))
                    {
                        if (!(((Mob) ch).ally || ch.buff(Charm.class) != null || ch.buff(Corruption.class) != null))
                        {
                            effictive=true;
                        }
                    }
                }

                if(effictive)
                {
                    Dungeon.hero.MANA-=8;
                    curUser.busy();
                    curUser.sprite.zap(ch.pos);
                    Buff.affect(ch, HolyHealing.class).set(healingsum(),healingeach());
                    Dungeon.hero.spendAndNext(1f);
                    Invisibility.dispel();
                }
                else
                {
                    GLog.w(Messages.get(Healing.class, "targetrequired"));
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(MagicBook.class, "prompt");
        }
    };

    public static Char autoAim(Char target) {

        if(target==null)
            return Dungeon.hero;

        if(Dungeon.hero.fieldOfView.length>target.pos&&target.pos>0) {
            if (Dungeon.hero.fieldOfView[target.pos]) {
                if (target.properties().contains(Char.Property.UNDEAD)) {
                    if (!(((Mob) target).ally || target.buff(Charm.class) != null || target.buff(Corruption.class) != null)) {
                        return target;
                    }
                }
            }
        }
        else {
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (Dungeon.hero.fieldOfView[mob.pos]) {
                        if (mob.properties().contains(Char.Property.UNDEAD)) {
                            if (!((mob).ally || mob.buff(Charm.class) != null || mob.buff(Corruption.class) != null)) {
                                return mob;
                        }
                    }
                }
            }
        }
        return Dungeon.hero;
    }
}


