package net.objectof.actof.porter.rules.components.impl;


import net.objectof.actof.porter.rules.components.Matcher;
import net.objectof.actof.porter.visitor.PorterContext;
import net.objectof.model.Kind;


public class KindMatcher implements Matcher {

    Kind<?> kind;

    public KindMatcher(Kind<?> kind) {
        this.kind = kind;
    }

    @Override
    public boolean test(PorterContext t) {
        return t.getKind().equals(kind);
    }

    public String toString() {
        return "kind = '" + kind + "'";
    }

}
