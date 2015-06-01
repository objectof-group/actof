package net.objectof.actof.porter.rules.impl;


import java.lang.reflect.Modifier;
import java.util.function.Function;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IPrettyPrintTransformer implements Function<IPorterContext, Object> {

    private Function<IPorterContext, Object> backer;

    public IPrettyPrintTransformer(Function<IPorterContext, Object> backer) {
        this.backer = backer;
    }

    @Override
    public Object apply(IPorterContext contect) {
        return backer.apply(contect);
    }

    @Override
    public String toString() {
        if (!Modifier.isStatic(backer.getClass().getModifiers())) { return "Custom Transformation"; }
        return backer.toString();
    }

}
