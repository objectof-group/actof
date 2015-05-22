package net.objectof.actof.repospy.migration.rulecomponents.impl;


import net.objectof.actof.repospy.migration.rulecomponents.RuleContext;
import net.objectof.actof.repospy.migration.rulecomponents.Transformer;


public class ReplaceTransformer implements Transformer {

    private Object replace;

    public ReplaceTransformer(Object replace) {
        this.replace = replace;
    }

    @Override
    public Object apply(RuleContext context) {
        return replace;
    }

    public String toString() {
        return "Replace with: '" + replace + "'";
    }

}
