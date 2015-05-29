package net.objectof.actof.porter.rules.components.impl;


import net.objectof.actof.porter.rules.components.Matcher;
import net.objectof.actof.porter.visitor.PorterContext;


public class KindNameMatcher implements Matcher {

    String kind;

    public KindNameMatcher(String kind) {
        this.kind = kind;
    }

    @Override
    public boolean test(PorterContext t) {
        return t.getKind().getComponentName().equals(kind);
    }

    public String toString() {
        return "kind = '" + kind + "'";
    }
}
