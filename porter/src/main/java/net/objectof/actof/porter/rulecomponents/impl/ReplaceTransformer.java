package net.objectof.actof.porter.rulecomponents.impl;


import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.rulecomponents.Transformer;


public class ReplaceTransformer implements Transformer {

    private Object replace;

    public ReplaceTransformer(Object replace) {
        this.replace = replace;
    }

    @Override
    public Object apply(PorterContext context) {
        return replace;
    }

    public String toString() {
        return "Replace with: '" + replace + "'";
    }

}
