package net.objectof.actof.porter.rules.components.impl;


import java.util.function.Predicate;

import net.objectof.actof.porter.visitor.IPorterContext;
import net.objectof.model.Kind;


public class IKindMatcher implements Predicate<IPorterContext> {

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
