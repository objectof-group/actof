package net.objectof.actof.repospy.migration.rulecomponents.impl;


import net.objectof.actof.repospy.migration.rulecomponents.Transformer;
import net.objectof.model.Kind;


public class ReplaceTransformer implements Transformer {

    private Object replace;

    public ReplaceTransformer(Object replace) {
        this.replace = replace;
    }

    @Override
    public Object apply(Object t, Object u, Kind<?> v) {
        return replace;
    }

    public String toString() {
        return "Replace with: '" + replace + "'";
    }

}
