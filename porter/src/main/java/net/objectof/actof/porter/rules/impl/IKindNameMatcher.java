package net.objectof.actof.porter.rules.impl;


import java.util.function.Predicate;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IKindNameMatcher implements Predicate<IPorterContext> {

    String kind;

    public IKindNameMatcher(String kind) {
        this.kind = kind;
    }

    @Override
    public boolean test(IPorterContext t) {
        return t.getKind().getComponentName().equals(kind);
    }

    public String toString() {
        return "kind = '" + kind + "'";
    }
}
