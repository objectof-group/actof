package net.objectof.actof.porter.rulecomponents.impl;


import java.lang.reflect.Modifier;

import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.rulecomponents.Matcher;


public class PrettyPrintMatcher implements Matcher {

    private Matcher backer;

    public PrettyPrintMatcher(Matcher backer) {
        this.backer = backer;
    }

    @Override
    public boolean test(PorterContext context) {
        return backer.test(context);
    }

    @Override
    public String toString() {
        if (!Modifier.isStatic(backer.getClass().getModifiers())) { return "Custom Matcher"; }
        return backer.toString();
    }
}
