package net.objectof.actof.porter.rules;


import java.util.List;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Rule {

    boolean match(IPorterContext context);

    Object transformKey(IPorterContext context);

    Object transformValue(IPorterContext context);

    /**
     * Called before any transformation is performed on the given context,
     * modifications made to this context will be permanent
     * 
     * @param context
     *            a context to inspect and modify
     */
    void beforeTransform(IPorterContext source, IPorterContext destination);

    /**
     * Called after any transformation is performed on the given context,
     * modifications made to these contexts will be permanent, although
     * modifications of the before context will be of no consequence
     * 
     * @param source
     *            the original context
     * @param destination
     *            the transformed context
     */
    void afterTransform(IPorterContext source, IPorterContext destination);

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

    static void beforeTransform(List<Rule> rules, IPorterContext source, IPorterContext destination) {
        for (Rule rule : rules) {
            if (rule.match(source)) {
                rule.beforeTransform(source, destination);
            }
        }
    }

    static void afterTransform(List<Rule> rules, IPorterContext source, IPorterContext destination) {
        for (Rule rule : rules) {
            if (rule.match(source)) {
                rule.afterTransform(source, destination);
            }
        }
    }

}
