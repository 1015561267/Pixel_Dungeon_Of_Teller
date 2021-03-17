package com.teller.pixeldungeonofteller.actors;

public class MagicalDamage extends Damage {

    public float FIREDAMAGE = 0f;
    public float ICEDAMAGE = 0f;
    public float HOLYDAMAGE = 0f;
    public float SHADOWDAMAGE = 0f;
    public float LIGHTNINGDAMAGE = 0f;
    public float NATUREDAMAGE = 0f;
    public float ARCANEDAMAGE = 0f;

    public float STEAMDAMAGE = 0f;
    public float CURSEDFLAMEDAMAGE = 0f;
    public float MAGNETICDAMAGE = 0f;
    public float SLURRYDAMAGE = 0f;
    public float HOLLOWDAMAGE = 0f;
    public float CORROSIVEDAMAGE = 0f;

    public MagicalDamage() {
        cancel();
    }

    public void AddFire(int v) {
        FIREDAMAGE += v;
    }

    public void AddIce(int v) {
        ICEDAMAGE += v;
    }

    public void AddHoly(int v) {
        HOLYDAMAGE += v;
    }

    public void AddShadow(int v) {
        SHADOWDAMAGE += v;
    }

    public void AddLightning(int v) {
        LIGHTNINGDAMAGE += v;
    }

    public void AddNature(int v) {
        NATUREDAMAGE += v;
    }

    public void AddArcane(int v) {
        ARCANEDAMAGE += v;
    }

    public void AddSteam(int v) {
        STEAMDAMAGE += v;
    }

    public void AddCursedFlame(int v) {
        CURSEDFLAMEDAMAGE += v;
    }

    public void AddMagnetic(int v) {
        MAGNETICDAMAGE += v;
    }

    public void AddSlurry(int v) {
        SLURRYDAMAGE += v;
    }

    public void AddHollow(int v) {
        HOLLOWDAMAGE += v;
    }

    public void AddCorrosive(int v) {
        CORROSIVEDAMAGE += v;
    }

    @Override
    public void cancel() {
        FIREDAMAGE = 0;
        ICEDAMAGE = 0;
        HOLYDAMAGE = 0;
        SHADOWDAMAGE = 0;
        LIGHTNINGDAMAGE = 0;
        NATUREDAMAGE = 0;
        ARCANEDAMAGE = 0;

        STEAMDAMAGE = 0;
        CURSEDFLAMEDAMAGE = 0;
        MAGNETICDAMAGE = 0;
        SLURRYDAMAGE = 0;
        HOLLOWDAMAGE = 0;
        CORROSIVEDAMAGE = 0;
    }

    public void remix()//the process of remix will follow a order
    {
        if (FIREDAMAGE > 0 && ICEDAMAGE > 0)//Steam damage is the most effective way do deal with flesh without any armor or shield
        {
            float tdamage = Math.min(FIREDAMAGE, ICEDAMAGE);//just in case i will let some attack have remixed damage at first
            STEAMDAMAGE += tdamage;
            FIREDAMAGE -= tdamage;
            ICEDAMAGE -= tdamage;
        }

        if (FIREDAMAGE > 0 && SHADOWDAMAGE > 0)//shadowflame will give a stable way to consume enemy's flesh,the best way to deal with enemy with heavy armor and shield but has poor hp
        {
            float tdamage = Math.min(FIREDAMAGE, SHADOWDAMAGE);
            CURSEDFLAMEDAMAGE += tdamage;
            FIREDAMAGE -= tdamage;
            ICEDAMAGE -= tdamage;
        }

        if (ICEDAMAGE > 0 && LIGHTNINGDAMAGE > 0)//magnetic has overwhelming advantage on enemy with heavy shield,more shield,more profit
        {
            float tdamage = Math.min(ICEDAMAGE, LIGHTNINGDAMAGE);
            MAGNETICDAMAGE += tdamage;
            ICEDAMAGE -= tdamage;
            LIGHTNINGDAMAGE -= tdamage;
        }

        if (ICEDAMAGE > 0 && NATUREDAMAGE > 0)//Slurry almost cannot do anydamage,but it will make enemy slow in speed,accuracy and evasion,useful when deal with fast but weak ones
        {
            float tdamage = Math.min(ICEDAMAGE, NATUREDAMAGE);
            SLURRYDAMAGE += tdamage;
            ICEDAMAGE -= tdamage;
            NATUREDAMAGE -= tdamage;
        }

        if (HOLYDAMAGE > 0 && SHADOWDAMAGE > 0)//Hollow damage,I want to make it aim at the resourse which enemy live on,but now that everyone live on hp,then it will do directly at hp
        {
            float tdamage = Math.min(HOLYDAMAGE, SHADOWDAMAGE);
            HOLLOWDAMAGE += tdamage;
            HOLYDAMAGE -= tdamage;
            SHADOWDAMAGE -= tdamage;
        }

        if (SHADOWDAMAGE > 0 && NATUREDAMAGE > 0)//corrosive aim at armor,the best way to deal with heavy armor
        {
            float tdamage = Math.min(SHADOWDAMAGE, NATUREDAMAGE);
            CORROSIVEDAMAGE += tdamage;
            SHADOWDAMAGE -= tdamage;
            NATUREDAMAGE -= tdamage;
        }
    }

    public boolean hassingleelement() {
        return !(FIREDAMAGE == 0 & ICEDAMAGE == 0) || HOLYDAMAGE != 0 || SHADOWDAMAGE != 0 || LIGHTNINGDAMAGE != 0 || NATUREDAMAGE != 0 || ARCANEDAMAGE != 0;
    }

    public boolean hasremixedelement() {
        return STEAMDAMAGE != 0 || CURSEDFLAMEDAMAGE != 0 || MAGNETICDAMAGE != 0 || SLURRYDAMAGE != 0 || HOLLOWDAMAGE != 0 || CORROSIVEDAMAGE != 0;
    }

    @Override
    public boolean useful() {
        return this.hasremixedelement() || this.hassingleelement();
    }

    @Override
    public void multiplie(double number) {
        FIREDAMAGE *= number;
        if (FIREDAMAGE < 0.001f) {
            FIREDAMAGE = 0;
        }
        ICEDAMAGE *= number;
        if (ICEDAMAGE < 0.001f) {
            ICEDAMAGE = 0;
        }
        HOLYDAMAGE *= number;
        if (HOLYDAMAGE < 0.001f) {
            HOLYDAMAGE = 0;
        }
        SHADOWDAMAGE *= number;
        if (SHADOWDAMAGE < 0.001f) {
            SHADOWDAMAGE = 0;
        }
        LIGHTNINGDAMAGE *= number;
        if (LIGHTNINGDAMAGE < 0.001f) {
            LIGHTNINGDAMAGE = 0;
        }
        NATUREDAMAGE *= number;
        if (NATUREDAMAGE < 0.001f) {
            NATUREDAMAGE = 0;
        }
        ARCANEDAMAGE *= number;
        if (ARCANEDAMAGE < 0.001f) {
            ARCANEDAMAGE = 0;
        }
        STEAMDAMAGE *= number;
        if (STEAMDAMAGE < 0.001f) {
            STEAMDAMAGE = 0;
        }
        CURSEDFLAMEDAMAGE *= number;
        if (CURSEDFLAMEDAMAGE < 0.001f) {
            CURSEDFLAMEDAMAGE = 0;
        }
        MAGNETICDAMAGE *= number;
        if (MAGNETICDAMAGE < 0.001f) {
            MAGNETICDAMAGE = 0;
        }
        SLURRYDAMAGE *= number;
        if (SLURRYDAMAGE < 0.001f) {
            SLURRYDAMAGE = 0;
        }
        HOLLOWDAMAGE *= number;
        if (HOLLOWDAMAGE < 0.001f) {
            HOLLOWDAMAGE = 0;
        }
        CORROSIVEDAMAGE *= number;
        if (CORROSIVEDAMAGE < 0.001f) {
            CORROSIVEDAMAGE = 0;
        }
    }

    @Override
    public float sum() {
        return FIREDAMAGE + ICEDAMAGE + HOLYDAMAGE + SHADOWDAMAGE + LIGHTNINGDAMAGE + NATUREDAMAGE + ARCANEDAMAGE + STEAMDAMAGE + CURSEDFLAMEDAMAGE + MAGNETICDAMAGE + SLURRYDAMAGE + HOLLOWDAMAGE + CORROSIVEDAMAGE;
    }
}
