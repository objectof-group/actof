package net.objectof.actof.porter.rules.components.impl;


import java.lang.reflect.Modifier;

import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.rules.components.Transformer;


public class PrettyPrintTransformer implements Transformer {

    private Transformer backer;

    public PrettyPrintTransformer(Transformer backer) {
        this.backer = backer;
    }

    @Override
    public Object apply(PorterContext contect) {
        return backer.apply(contect);
    }

    @Override
    public String toString() {
        if (!Modifier.isStatic(backer.getClass().getModifiers())) { return "Custom Transformation"; }
        return backer.toString();
    }

}
