package com.teller.pixeldungeonofteller.items.weapon.weapons.MagicBook;

import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.hero.Hero;
import com.teller.pixeldungeonofteller.effects.particles.ElmoParticle;
import com.teller.pixeldungeonofteller.items.pages.MagicPage;
import com.teller.pixeldungeonofteller.items.pages.Spell.Spell;
import com.teller.pixeldungeonofteller.items.weapon.Weapon;
import com.teller.pixeldungeonofteller.items.weapon.weapons.DualWieldWeapon.Tonfa;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.scenes.GameScene;
import com.teller.pixeldungeonofteller.sprites.CharSprite;
import com.teller.pixeldungeonofteller.sprites.HeroSprite;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class MagicBook extends Weapon {

    @Override
    public Type WeaponType() {
        return Type.OffHand;
    }

    public static final String AC_CAST = "CAST";
    public static final String AC_SWITCH = "SWITCH";
    public static final String AC_TEAR = "TEAR";

    private static final String SELECTEDSPELL = "SELECTEDSPELL";
    private static final String STOREDSPELL = "STOREDSPELL";

    private static final float TIME_TO_CAST = 1f;
    private static final float TIME_TO_SWITCH = 1f;
    private static final float TIME_TO_TEAR = 1f;

    @Override
    public int min(int lvl) {
        return 0;
    }

    @Override
    public int max(int lvl) {
        return 0;
    }

    public boolean attackable() {
        return false;
    }

    public ArrayList<Spell> storedspells = new ArrayList<Spell>();
    ;
    public Spell selectedspell;

    @Override
    public boolean isUpgradable()
    {return false;}

    public void joinNew(Spell spell)
    {
        storedspells.add(spell);
        GameScene.scene.offhandupdate();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SELECTEDSPELL, selectedspell);
        bundle.put(STOREDSPELL,storedspells);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        selectedspell = (Spell)(bundle.get(SELECTEDSPELL));

        for (Bundlable item : bundle.getCollection(STOREDSPELL)) {
            if (item != null) storedspells.add((Spell) item);
        }

        usesTargeting = selectedspell.usesTargeting;
        selftargeting = selectedspell.selftargeting;
    }

    @Override
    public int STRReq(int lvl) {
        return 0;
    }

    @Override
    public int DEXReq(int lvl) {
        return 0;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if(Dungeon.hero.belongings.offhandweapon==this)
        {
            actions.add(AC_CAST);
            actions.add(AC_SWITCH);
            actions.add(AC_TEAR);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero,action);
        if(action.equals(AC_SWITCH)||action.equals(AC_CAST)) {
            if (hero.belongings.offhandweapon == this) {
                if (action.equals(AC_CAST)) {
                    selectedspell.conjure(false, null);
                } else if (action.equals(AC_SWITCH)) {
                    int index = storedspells.indexOf(selectedspell);
                    if(index == storedspells.size() -1 )
                    {
                        index = -1;
                    }
                    selectedspell = storedspells.get(index+1);
                    GameScene.scene.offhandupdate();
                    usesTargeting=selectedspell.usesTargeting;
                    selftargeting=selectedspell.selftargeting;
                    //FIXME:It is really stuipd to locate index of element in array but now that I want the spells in a sort(especially in the book of chaos) I can't think up other good data structure,considering the number of spells in a book is limited(<=4),the code below looks not so stupid XD.
                    hero.spendAndNext(TIME_TO_SWITCH);
                }
            } else {
                GLog.w(Messages.get(MagicBook.class, "unequip"));
            }
        }
        else if(action.equals(AC_TEAR))
        {
            if(storedspells.size()>1) {

                String name=selectedspell.name();
                Spell teared = selectedspell;

                int index = storedspells.indexOf(selectedspell);
                storedspells.remove(selectedspell);
                if(index == storedspells.size() || index<0)
                {
                    index = 0;
                }
                selectedspell = storedspells.get(index);

                GameScene.scene.offhandupdate();
                hero.busy();
                hero.sprite.operate(hero.pos);
                ((HeroSprite) curUser.sprite).read();
                hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);
                hero.spendAndNext(TIME_TO_TEAR);
                usesTargeting=selectedspell.usesTargeting;
                selftargeting=selectedspell.selftargeting;
                GLog.w(Messages.get(MagicBook.class, "teared",name,name()));
                new MagicPage(teared).collect();
            }
            else {
                GLog.w(Messages.get(MagicBook.class, "onlyone"));
            }
        }
    }

    @Override
    public String info() {
        String info = desc();

        info+="\n\n"+Messages.get(MagicBook.class, "all");
        for (int i = 0; i < storedspells.size(); i++) {
            info+=storedspells.get(i).name();
            if(i!= storedspells.size()-1)
            {
                info+=",";
            }
        }

        info+="\n"+Messages.get(MagicBook.class, "selected",selectedspell.name());
        info+= selectedspell.desc();
        info+="\n"+Messages.get(MagicBook.class, "manacost",selectedspell.ManaCost());
        return info;
    }

    public Class<? extends CharSprite> SpellSprite()
    {
        return selectedspell.Spellsprite();
    }

    public boolean reiterated(Spell Toadd)
    {
        for (int i=0;i<storedspells.size();i++) {
            if(Toadd.getClass() == storedspells.get(i).getClass())
            return true;
        }
        return false;
    }

    public boolean fulfilled()
    {
        return storedspells.size()>=3;
    }

    public MagicBook addRaw()
    { return null;}
}