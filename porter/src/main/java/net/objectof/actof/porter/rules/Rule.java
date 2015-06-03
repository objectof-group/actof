package net.objectof.actof.porter.rules;


import java.util.List;

import net.objectof.actof.porter.visitor.IPorterContext;


public interface Rule {

    boolean match(IPorterContext source);

    Object transformKey(IPorterContext source, IPorterContext destination);

    Object transformValue(IPorterContext source, IPorterContext destination);

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

    static IPorterContext transformKey(List<Rule> rules, IPorterContext source, IPorterContext destination) {
        IPorterContext modSource = source.copy();
        for (Rule rule : rules) {
            if (rule.match(modSource)) {
                modSource.setKey(rule.transformKey(modSource, destination));
            }
        }
        return modSource;
    }

    static IPorterContext transformValue(List<Rule> rules, IPorterContext source, IPorterContext destination) {
        IPorterContext modSource = source.copy();
        for (Rule rule : rules) {
            if (rule.match(modSource)) {
                modSource.setValue(rule.transformValue(modSource, destination));
            }
        }
        return modSource;
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
