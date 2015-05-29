package net.objectof.actof.porter.rules;


import java.util.List;

import net.objectof.actof.porter.visitor.PorterContext;


public interface Rule {

    boolean match(PorterContext context);

    Object transformKey(PorterContext context);

    Object transformValue(PorterContext context);

    void onPort(PorterContext source, PorterContext destination);

    boolean modifiesKey(PorterContext context);

    boolean modifiesValue(PorterContext context);

    static PorterContext transformKey(List<Rule> rules, PorterContext context) {
        PorterContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setKey(rule.transformKey(modContext));
            }
        }
        return modContext;
    }

    static PorterContext transformValue(List<Rule> rules, PorterContext context) {
        PorterContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setValue(rule.transformValue(modContext));
            }
        }
        return modContext;
    }

    static void onPort(List<Rule> rules, PorterContext before, PorterContext after) {
        for (Rule rule : rules) {
            PorterContext modBefore = before.copy();
            PorterContext modAfter = after.copy();
            if (rule.match(before)) {
                rule.onPort(modBefore, modAfter);
            }
        }
    }

}
