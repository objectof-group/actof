package net.objectof.actof.porter.rules.impl;


import net.objectof.model.Kind;
import net.objectof.model.Stereotype;


public class Matchers {

    public static Matcher matchKey(Object key) {
        return c -> c.getKey().equals(key);
    }

    public static Matcher matchKind(Kind<?> kind) {
        return c -> c.getKind().equals(kind);
    }

    public static Matcher matchKind(String kind) {
        return c -> c.getKind().getComponentName().equals(kind);
    }

    public static Matcher matchStereotype(Stereotype st) {
        return c -> c.getKind().getStereotype().equals(st);
    }

    public static Matcher matchStereotype(String st) {
        return c -> c.getKind().getStereotype().toString().equals(st);
    }

}
