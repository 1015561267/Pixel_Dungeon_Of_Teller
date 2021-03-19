package com.teller.pixeldungeonofteller.items.pages.Spell.OldBook;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.buffs.Buff;
import com.teller.pixeldungeonofteller.actors.buffs.Invisibility;
import com.teller.pixeldungeonofteller.actors.buffs.Light;
import com.teller.pixeldungeonofteller.effects.particles.FlameParticle;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook.MagicBook;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.sprites.ItemSpriteSheet;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.Normal.OldBook.LightUpSprite;
import com.teller.pixeldungeonofteller.sprites.MagicSpellSprite.MagicSpellSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.particles.Emitter;

public class LightUp  extends Spell {

    {
        name= Messages.get(this,"name");
        image= ItemSpriteSheet.PAGE_LIGHTUP;
        spriteClass = LightUpSprite.class;
    }

    @Override
    public String desc()
    {
        return "\n"+Messages.get(this, "desc");
    }

    @Override
    public Class<? extends MagicSpellSprite> Spellsprite() {
        return spriteClass;
    }

    public int ManaCost()
    {
        return 10;
    }

    public void conjure(boolean useMagicPage, MagicPage page)
    {
          if(checkmana()||useMagicPage)
          {
              if(!useMagicpage)
              { Dungeon.hero.MANA-=ManaCost(); }
              useMagicpage=false;
              Dungeon.hero.busy();
              Dungeon.hero.sprite.zap(Dungeon.hero.pos);
              Buff.affect(Dungeon.hero, Light.class, 60f+30f*Dungeon.hero.INT);
              Emitter emitter = Dungeon.hero.sprite.centerEmitter();
              emitter.start(FlameParticle.FACTORY, 0.2f, 3);
              Invisibility.dispel();
              Dungeon.hero.spendAndNext(1f);
          }
          else
              GLog.w( Messages.get(MagicBook.class, "nomana"));
    }

    public boolean equals(Object object)
    {
        return object instanceof LightUp;
    }
}
