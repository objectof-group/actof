package net.objectof.actof.repospy.migration;


import java.util.List;

import net.objectof.actof.repospy.migration.rulecomponents.PorterContext;


public interface Rule {

    boolean match(PorterContext context);

    Object transformKey(PorterContext context);

    Object transformValue(PorterContext context);

    static Object transformKey(List<Rule> rules, PorterContext context) {
        PorterContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setKey(rule.transformKey(modContext));
            }
        }
        return modContext.getKey();
    }

    static Object transformValue(List<Rule> rules, PorterContext context) {
        PorterContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setValue(rule.transformValue(modContext));
            }
        }
        return modContext.getValue();
    }

}
