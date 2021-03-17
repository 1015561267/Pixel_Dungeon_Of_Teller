package com.teller.pixeldungeonofteller.actors;

public class PhysicalDamage extends Damage {
    public float IMPACTDAMAGE = 0f;
    public float SLASHDAMAGE = 0f;
    public float PUNCTUREDAMAGE = 0f;

    public PhysicalDamage() {
    }

    public PhysicalDamage(int v, PhysicalPercentage physicalPercentage) {
        //this.from=from;
        //this.to=to;
        IMPACTDAMAGE = physicalPercentage.IMPACTPENCENTAGE * v;
        SLASHDAMAGE = physicalPercentage.SLASHPENCENTAGE * v;
        PUNCTUREDAMAGE = physicalPercentage.PUNCTUREPENCENTAGE * v;
    }

    public PhysicalDamage(int id,int sd,int pd)
    {
        IMPACTDAMAGE=id;
        SLASHDAMAGE=id;
        PUNCTUREDAMAGE=pd;
    }

    public void AddImpact(int v) {
        IMPACTDAMAGE += v;
        if (IMPACTDAMAGE < 0.001f) IMPACTDAMAGE = 0;
    }

    public void AddSlash(int v) {
        SLASHDAMAGE += v;
        if (SLASHDAMAGE < 0.001f) SLASHDAMAGE = 0;
    }

    public void AddPuncture(int v) {
        PUNCTUREDAMAGE += v;
        if (PUNCTUREDAMAGE < 0.001f) PUNCTUREDAMAGE = 0;
    }

    @Override
    public float sum() {
        return IMPACTDAMAGE + SLASHDAMAGE + PUNCTUREDAMAGE;
    }

    @Override
    public void cancel() {
        IMPACTDAMAGE = 0;
        SLASHDAMAGE = 0;
        PUNCTUREDAMAGE = 0;
    }


    @Override
    public boolean useful() {
        return IMPACTDAMAGE != 0 || SLASHDAMAGE != 0 || PUNCTUREDAMAGE != 0;
    }

    @Override
    public void multiplie(double number) {
        IMPACTDAMAGE *= number;
        SLASHDAMAGE *= number;
        PUNCTUREDAMAGE *= number;
    }

    public boolean IsImpact() {
        return IMPACTDAMAGE > 0;
    }

    public boolean IsSlagh() {
        return SLASHDAMAGE > 0;
    }

    public boolean IsPuncture() {
        return PUNCTUREDAMAGE > 0;
    }

    public void remix(PhysicalDamage physicalDamage) {
        this.IMPACTDAMAGE += physicalDamage.IMPACTDAMAGE;
        this.PUNCTUREDAMAGE += physicalDamage.PUNCTUREDAMAGE;
        this.SLASHDAMAGE += physicalDamage.SLASHDAMAGE;
    }
}
