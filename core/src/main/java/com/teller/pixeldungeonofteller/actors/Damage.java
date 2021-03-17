package com.teller.pixeldungeonofteller.actors;
// This is where I started my work
// Without it,it's impossible to do a lot of things

public class Damage {

    public int effictivehpdamage = 0;
    public int effictiveslddamage = 0;
    public int effictivearmordamage = 0;

    public boolean tamahaawk = false;//for now we need to consult the actual hp damage to decide how much bleeding buff will be,it means we need to do this thing after the damage is dealt instead befor.
                                    // lucky thing is that we only need to have a mark to let damage consult progress note this and use effective-hp-dmg to do the thing
                                    //  writing things in damage would help if any further things like this is needed

    /*public enum Type {
        PHYSICAL,
        MAGICAL,
        ABSOLUTE
    }

    public static class physical
    {
        public static final int NONE = 0x0000;
        public static  final int  IMPACT = 0x0000;
        public static  final int  SLASH = 0x0000;
        public static  final int  PUNCTURE = 0x0000;
        public static final int PHYSICAL_COUNT = 3;

        public static int all() {
            int a = 0;
            for (int i = 0; i < PHYSICAL_COUNT; ++i)
                a |= 0x01 << i;
            return a;
        }
    }

    public static class magical {
        public static final int NONE = 0x0000;
        public static final int FIRE = 0x0001;
        public static final int ICE = 0x0004;
        public static final int HOLY = 0x0040;
        public static final int SHADOW = 0x0010;
        public static final int LIGHT = 0x0008;
        public static final int EARTH = 0x0012;//need to change
        public static final int ARCANE = 0x0018;//need to change

        public static final int MAGICAL_COUNT = 7;

        public static int all() {
            int a = 0;
            for (int i = 0; i < MAGICAL_COUNT; ++i)
                a |= 0x01 << i;
            return a;
        }
    }

    public static class Feature {
        public static final int NONE = 0x0000;
        public static final int CRITICAL = 0x0001;
        public static final int ACCURATE = 0x0002;
        public static final int PURE = 0x0004;
        public static final int DEATH = 0x0008;
        public static final int RANGED = 0x0010;

        public static final int FEATURE_COUNT = 5;

        public static int all() {
            int a = 0;
            for (int i = 0; i < FEATURE_COUNT; ++i)
                a |= 0x01 << i;
            return a;
        }
    }*/

    // attributes
    //public int value = 0;
    //public Type type = Type.ABSOLUTE;
    //public int element = Element.NONE;
    //public int feature = Feature.NONE;
    public Object from = null;
    public Object to = null;

    //public Damage type(Type t) {
    //    type = t;
    //    return this;
    //}
    public boolean useful() {
        return false;
    }

    public void multiplie(double number) {
        return;
    }

    public void cancel() {
        return;
    }

    public float sum() {
        return 0;
    }

    //public abstract boolean hasabsolutedamage();

    //public abstract boolean consult();
}
