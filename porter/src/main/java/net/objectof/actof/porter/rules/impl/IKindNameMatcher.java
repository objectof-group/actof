package net.objectof.actof.porter.rules.impl;


import net.objectof.actof.porter.visitor.IPorterContext;


public class IKindNameMatcher implements Matcher {

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
