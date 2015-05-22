package net.objectof.actof.repospy.migration;


import java.util.List;

import net.objectof.actof.repospy.migration.rulecomponents.RuleContext;


public interface Rule {

    boolean match(RuleContext context);

    Object transformKey(RuleContext context);

    Object transformValue(RuleContext context);

    static Object transformKey(List<Rule> rules, RuleContext context) {
        RuleContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setKey(rule.transformKey(modContext));
            }
        }
        return modContext.getKey();
    }

    static Object transformValue(List<Rule> rules, RuleContext context) {
        RuleContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setValue(rule.transformValue(modContext));
            }
        }
        return modContext.getValue();
    }

}
