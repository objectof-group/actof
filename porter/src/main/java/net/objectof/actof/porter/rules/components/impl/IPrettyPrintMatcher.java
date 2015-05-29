package net.objectof.actof.porter.rules.components.impl;


import java.lang.reflect.Modifier;

import net.objectof.actof.porter.rules.components.Matcher;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IPrettyPrintMatcher implements Matcher {

    private Matcher backer;

    public IPrettyPrintMatcher(Matcher backer) {
        this.backer = backer;
    }

    @Override
    public boolean test(IPorterContext context) {
        return backer.test(context);
    }

    @Override
    public String toString() {
        if (!Modifier.isStatic(backer.getClass().getModifiers())) { return "Custom Matcher"; }
        return backer.toString();
    }
}
