package com.teller.pixeldungeonofteller.actors;

public class AbsoluteDamage extends Damage {
    float damage = 0f;

    public AbsoluteDamage(int v) {
        damage = v;
    }

    public AbsoluteDamage(int v, Object from, Object to) {
        damage = v;
        this.from = from;
        this.to = to;
    }

    @Override
    public void cancel() {
        damage = 0;
    }

    @Override
    public boolean useful() {
        return damage != 0;
    }

    @Override
    public void multiplie(double number) {
        damage *= number;
    }

    @Override
    public float sum() {
        return damage;
    }
}
