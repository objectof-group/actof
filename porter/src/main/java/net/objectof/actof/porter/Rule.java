package net.objectof.actof.porter;


import java.util.List;


public interface Rule {

    boolean match(PorterContext context);

    Object transformKey(PorterContext context);

    Object transformValue(PorterContext context);

    void onPort(PorterContext source, PorterContext destination);

    boolean modifiesKey(PorterContext context);

    boolean modifiesValue(PorterContext context);

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

    static void onPort(List<Rule> rules, PorterContext before, PorterContext after) {
        for (Rule rule : rules) {
            PorterContext modBefore = before.copy();
            PorterContext modAfter = after.copy();
            if (rule.match(before)) {
                rule.onPort(modBefore, modAfter);
            }
        }
    }

    static boolean modifiesKey(List<Rule> rules, PorterContext context) {
        for (Rule rule : rules) {
            if (rule.modifiesKey(context)) { return true; }
        }
        return false;
    }

    static boolean modifiesValue(List<Rule> rules, PorterContext context) {
        for (Rule rule : rules) {
            if (rule.modifiesValue(context)) { return true; }
        }
        return false;
    }

}
