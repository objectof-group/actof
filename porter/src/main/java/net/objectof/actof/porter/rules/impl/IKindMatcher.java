package net.objectof.actof.porter.rules.impl;


import net.objectof.actof.porter.visitor.IPorterContext;
import net.objectof.model.Kind;


public class IKindMatcher implements Matcher {

    Kind<?> kind;

    public IKindMatcher(Kind<?> kind) {
        this.kind = kind;
    }

    @Override
    public boolean test(IPorterContext t) {
        return t.getKind().equals(kind);
    }

    public String toString() {
        return "kind = '" + kind + "'";
    }

}
