package net.objectof.actof.porter.rules;


import java.util.List;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Rule {

    boolean match(IPorterContext context);

    Object transformKey(IPorterContext context);

    Object transformValue(IPorterContext context);

    boolean dropCheck(IPorterContext context);

    void onPort(IPorterContext source, IPorterContext destination);

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

    static boolean dropCheck(List<Rule> rules, IPorterContext context) {
        IPorterContext modContext = context.copy();
        for (Rule rule : rules) {
            if (rule.match(modContext)) {
                boolean drop = rule.dropCheck(modContext);
                if (drop) { return true; }
            }
        }
        return false;
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
