package net.objectof.actof.porter.rules.impl;


import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.regex.Matcher;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IPrettyPrintMatcher implements Predicate<IPorterContext> {

    private Predicate<IPorterContext> backer;

    public IPrettyPrintMatcher(Predicate<IPorterContext> backer) {
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
