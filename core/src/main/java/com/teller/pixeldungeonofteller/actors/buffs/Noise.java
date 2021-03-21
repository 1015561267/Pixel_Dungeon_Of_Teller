package com.teller.pixeldungeonofteller.actors.buffs;

import com.teller.pixeldungeonofteller.Assets;
import com.teller.pixeldungeonofteller.Dungeon;
import com.teller.pixeldungeonofteller.actors.mobs.Mob;
import com.teller.pixeldungeonofteller.effects.Speck;
import com.teller.pixeldungeonofteller.items.artifacts.CloakOfShadows;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.text.DecimalFormat;

public class Noise extends Buff{

    private boolean triggered = false;//use this to avoid trigger multiple times

    private static final int NOISECAP = 80;
    private static final String NOISE = "NOISE";
    //private static final String TIME_TO_FADE = "TIME_TO_FADE";
    private static final String FADE_STEP = "FADE_STEP";
    private static final String STEP_TIME = "STEP_TIME";

    protected int noise;
    //protected int fade_time = 1;
    protected int fade_step = 1;
    protected int step_time = 0;
    float increased = 0;

    @Override
    public boolean act() {
        if(target.buff(Light.class)!=null)
        {
            target.buff(Noise.class).lightNoise();
        }
        if(target.buff(Invisibility.class)!=null || target.buff(CloakOfShadows.cloakStealth.class)!=null)
        {
            target.buff(Noise.class).invisibilityNoise();
        }
        triggered = false;

        noise = (int) Math.ceil(noise * 0.9f);

        spend(TICK);
        return true;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(NOISE, noise);
       // bundle.put(TIME_TO_FADE,fade_time);
        bundle.put(FADE_STEP,fade_step);
        bundle.put(STEP_TIME,step_time);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        noise = bundle.getInt(NOISE);
        //fade_time = bundle.getInt(TIME_TO_FADE);
        fade_step = bundle.getInt(FADE_STEP);
        step_time = bundle.getInt(STEP_TIME);
    }

    public int getNoise()
    {return noise;}

    public void fade()
    {
        if(Dungeon.hero.stealthLevel()!=1)
        {
            step_time--;
            if(step_time<=0)
            {
                increaseStep();
            }
        }
        decreaseNoise(fade_step);
    }

    private void increaseStep() {
        fade_step ++;
        switch (Dungeon.hero.stealthLevel()) {
            case 2:step_time=10;break;
            case 3:step_time=8;break;
            case 4:step_time=6;break;
            case 5:step_time=4;break;
        }
    }

    public void stealthLevelAlter(int level)
    {
        fade_step = 1;
        switch (Dungeon.hero.stealthLevel()) {
            case 2:step_time=10;break;
            case 3:step_time=8;break;
            case 4:step_time=6;break;
            case 5:step_time=4;break;
        }
    }

    public void increaseNoise(float n)
    {
        noise+=n;
        if(noise>NOISECAP) {
            increased = noise - NOISECAP;
            noise = NOISECAP;
        }
    }

    public void decreaseNoise(float n)
    {
        noise-=n;
        if(noise<0) noise=0;
    }

    public void triggerPenlty()
    {
        if(!triggered&&increased>0)
        {
            if(Random.Float()<increased*0.01)
            {
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    mob.beckon(Dungeon.hero.pos);
                }
                triggered = true;
                Dungeon.hero.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
                Sample.INSTANCE.play(Assets.SND_CHALLENGE);
                GLog.w(Messages.get(this, "attract"));
            }
            increased = 0;
        }
    }

    public void moveNoise()//see Hero.actMove()
    {
        float possibility = 0;
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:possibility=0.5f;break;
            case 2:possibility=0.33f;break;
            case 3:possibility=0.25f;break;
            case 4:possibility=0.1f;break;
            case 5:break;
        }
        if(Random.Float()<possibility)
        {increaseNoise(1 ); triggerPenlty();}
    }

    public void moveTowaterNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:increaseNoise(2 );break;
            case 2:
                if(Random.Float()<0.5) {increaseNoise(1 );}
                increaseNoise(1 );break;
            case 3:increaseNoise(1 );break;
            case 4: if(Random.Float()<0.75) {increaseNoise(1 );}break;
            case 5: if(Random.Float()<0.5) {increaseNoise(1 );}break;
        }
        triggerPenlty();
    }

    public void moveTograssNoise()
    {
        float chance = Random.Float();
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:if(chance<0.5)
                increaseNoise(1); triggerPenlty();break;
            case 2:if(chance<0.25)
                increaseNoise(1); triggerPenlty();break;
            case 3:if(chance<0.2)
                increaseNoise(1); triggerPenlty();break;
            case 4:break;
            case 5:if(Random.Float()<0.2)
                decreaseNoise(1);break;
        }
    }

    public void levitationNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1: if(Random.Float()<0.5) {increaseNoise(1); triggerPenlty();}break;
        }
    }

    public void pressNoise()//see Level.press()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:increaseNoise(2 );break;
            case 2:if(Random.Float()<0.5) {increaseNoise(1);}break;
            case 3:
            case 4:
            case 5:increaseNoise(1 );break;
        }
        triggerPenlty();
    }

    public void trapNoise()//see Level.press()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:increaseNoise(10 );break;
        }
        triggerPenlty();
    }

    public void openChestNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:increaseNoise(4);break;
            case 2:
            case 3:increaseNoise(3 );break;
            case 4:
            case 5:increaseNoise(2 );break;
        }
        triggerPenlty();
    }

    public void attackNoise()
    {
        int weaponfactor = Dungeon.hero.weaponStealth();
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:increaseNoise(10);break;
            case 2:increaseNoise(10 - weaponfactor);break;
            case 3:increaseNoise(9 - weaponfactor);break;
            case 4:increaseNoise(8 - weaponfactor);break;
            case 5:increaseNoise(9 - 2 * weaponfactor);break;
        }
        triggerPenlty();
    }

    public void defenseNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:increaseNoise(3);break;
            case 2:
            case 3:increaseNoise(2);break;
            case 4:
            case 5:increaseNoise(1);break;
        }
        triggerPenlty();
    }

    public void pickNoise()//see Hero.actPickup
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:increaseNoise(3);break;
            case 2:
            case 3:increaseNoise(2);break;
            case 4:
            case 5:increaseNoise(1);break;
        }
        triggerPenlty();
    }

    public void killNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:decreaseNoise(3);break;
            case 2:decreaseNoise(4);break;
            case 3:decreaseNoise(5);break;
            case 4:decreaseNoise(6);break;
            case 5:decreaseNoise(7);break;
        }
    }

    public void lightNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1: if(Random.Float()<0.5) {increaseNoise(1); }
            case 2: increaseNoise(1);triggerPenlty();break;
            case 3:
            case 4:
            case 5: if(Random.Float()<0.5) {increaseNoise(1); triggerPenlty();}break;
        }
    }

    public void invisibilityNoise() {
        switch (Dungeon.hero.stealthLevel()) {
            case 1:
            case 2: break;
            case 3:
            case 4:
            case 5: decreaseNoise(1);break;
        }
    }

    public void throwPotionNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:increaseNoise(2);break;
            case 2:
            case 3:increaseNoise(1);break;
        }
        triggerPenlty();
    }

    public void readScrollNoise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:
            case 2: increaseNoise(2);break;
            case 3:
            case 4:increaseNoise(1);break;
        }
        triggerPenlty();
    }

    public void readRage()
    {
        noise = 80;
    }

    public int get_stealth_level()
    {
        return Dungeon.hero.stealthLevel();
    }

    public void fire_firearm()
    {
        increaseNoise(10); triggerPenlty();
    }

    public void press_trap()
    {
        increaseNoise(10); triggerPenlty();
    }

    public double stealthRevise()
    {
        switch (Dungeon.hero.stealthLevel())
        {
            case 1:return 1.4;
            case 2:return 1.2;
            case 3:return 1;
            case 4:return 0.9;
            case 5:return 0.8;
        }
        return 1;
    }
}
