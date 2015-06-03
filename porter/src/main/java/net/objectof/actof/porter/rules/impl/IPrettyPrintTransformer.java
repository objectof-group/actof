package net.objectof.actof.porter.rules.impl;


import java.lang.reflect.Modifier;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IPrettyPrintTransformer implements Transformer {

    private Transformer backer;

    public IPrettyPrintTransformer(Transformer backer) {
        this.backer = backer;
    }

    @Override
    public Object apply(IPorterContext source, IPorterContext destination) {
        return backer.apply(source, destination);
    }

    @Override
    public String toString() {
        if (!Modifier.isStatic(backer.getClass().getModifiers())) { return "Custom Transformation"; }
        return backer.toString();
    }

}
