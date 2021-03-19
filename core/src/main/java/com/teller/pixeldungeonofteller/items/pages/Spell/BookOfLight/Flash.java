package com.teller.pixeldungeonofteller.items.pages.Spell.BookOfLight;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.Actor;
import com.teller.pixeldungeonofteller.actors.Char;
import com.teller.pixeldungeonofteller.actors.buffs.Blindness;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Cripple;
import com.teller.pixeldungeonofteller.actors.buffs.FlashOverLoad;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.levels.Level;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.BookOfLight.FlashSprite;
import com.teller.pixeldungeonofteller.utils.GLog;

public class Flash extends Spell {

    {
        name= Messages.get(this,"name");
        image= ItemSpriteSheet.PAGE_FLASH;
        spriteClass = FlashSprite.class;
    }

    @Override
    public String desc()
    {
        return "\n"+Messages.get(this, "desc");
    }

    public boolean equals(Object object)
    {
        return object instanceof Flash;
    }

    public int ManaCost()
    {
        if(Dungeon.hero.buff(FlashOverLoad.class) != null)
        {
            return Dungeon.hero.buff(FlashOverLoad.class).manacost();
        }
        else return 6;
    }

    public void conjure(boolean useMagicPage, MagicPage page)
    {
        if(checkmana()|| useMagicPage) {
            if(!useMagicpage) { Dungeon.hero.MANA-=ManaCost(); }
            useMagicpage=false;
            Dungeon.hero.busy();
            Dungeon.hero.sprite.zap(Dungeon.hero.pos);
            boolean overloading=Dungeon.hero.buff(FlashOverLoad.class) != null;
            int power=8;
            int attenuation=2;
            if(!overloading)
            {
                Buff.affect(Dungeon.hero,FlashOverLoad.class).set();
            }
            else
            {
                power=Dungeon.hero.buff(FlashOverLoad.class).power();
                attenuation=Dungeon.hero.buff(FlashOverLoad.class).attenuation();
                Dungeon.hero.buff(FlashOverLoad.class).update();
            }
            Level l = Dungeon.level;
            int cell = Dungeon.hero.pos;
            for (Char ch : Actor.chars()){
                if(!(ch instanceof Hero)) {
                    if (Dungeon.hero.checkvisible(ch)) {
                        power = power - attenuation * l.distance(ch.pos, cell);
                        if (power < 3) power = 3;
                        Buff.prolong(ch, Blindness.class, power);
                        Buff.prolong(ch, Cripple.class, (int)Math.floor(power/2));
                        ch.sprite.emitter().burst(FlameParticle.FACTORY, (int)Math.floor(power + 1)/2);

                    }
                }
            }
            GameScene.flash(0xFFFFFF);
            Invisibility.dispel();
            Dungeon.hero.spendAndNext(1f);
        }
        else GLog.w( Messages.get(MagicBook.class, "nomana"));
    }
}
