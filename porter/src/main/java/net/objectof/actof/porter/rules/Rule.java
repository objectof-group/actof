package net.objectof.actof.porter.rules;


import java.util.List;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Rule {

    boolean match(IPorterContext context);

    Object transformKey(IPorterContext context);

    Object transformValue(IPorterContext context);

    void onPort(IPorterContext source, IPorterContext destination);

    boolean modifiesKey(IPorterContext context);

    boolean modifiesValue(IPorterContext context);

    static IPorterContext transformKey(List<Rule> rules, IPorterContext context) {
        IPorterContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setKey(rule.transformKey(modContext));
            }
        }
        return modContext;
    }

    static IPorterContext transformValue(List<Rule> rules, IPorterContext context) {
        IPorterContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                modContext.setValue(rule.transformValue(modContext));
            }
        }
        return modContext;
    }

    static void onPort(List<Rule> rules, IPorterContext before, IPorterContext after) {
        for (Rule rule : rules) {
            IPorterContext modBefore = before.copy();
            IPorterContext modAfter = after.copy();
            if (rule.match(before)) {
                rule.onPort(modBefore, modAfter);
            }
        }
    }

}
