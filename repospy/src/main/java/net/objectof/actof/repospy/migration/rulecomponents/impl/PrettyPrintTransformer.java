package net.objectof.actof.repospy.migration.rulecomponents.impl;


import java.lang.reflect.Modifier;

import net.objectof.actof.repospy.migration.PorterContext;
import net.objectof.actof.repospy.migration.rulecomponents.Transformer;


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
