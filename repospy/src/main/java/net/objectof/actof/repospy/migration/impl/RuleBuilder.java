package net.objectof.actof.repospy.migration.impl;


import net.objectof.actof.repospy.migration.Rule;
import net.objectof.actof.repospy.migration.rulecomponents.Matcher;
import net.objectof.actof.repospy.migration.rulecomponents.Transformer;
import net.objectof.actof.repospy.migration.rulecomponents.impl.KeyMatcher;
import net.objectof.actof.repospy.migration.rulecomponents.impl.ReplaceTransformer;
import net.objectof.actof.repospy.migration.rulecomponents.impl.StereotypeMatcher;
import net.objectof.model.Stereotype;


public class RuleBuilder {

    IRule rule = new IRule();

    private RuleBuilder() {}

    public static RuleBuilder start() {
        return new RuleBuilder();
    }

    public Rule build() {
        return rule;
    }

    public RuleBuilder forKey(Object... key) {
        rule.getMatchers().add(new KeyMatcher(key));
        return this;
    }

    public RuleBuilder forStereotype(Stereotype... stereotype) {
        rule.getMatchers().add(new StereotypeMatcher(stereotype));
        return this;
    }

    public RuleBuilder setKey(Object newKey) {
        rule.getKeyTransformers().add(new ReplaceTransformer(newKey));
        return this;
    }

    public RuleBuilder setValue(Object newKey) {
        rule.getValueTransformers().add(new ReplaceTransformer(newKey));
        return this;
    }

    // /////////////////////////////
    // Generic
    // /////////////////////////////

    public RuleBuilder match(Matcher matcher) {
        rule.getMatchers().add(matcher);
        return this;
    }

    public RuleBuilder keyTransform(Transformer robotInDisguise) {
        rule.getKeyTransformers().add(robotInDisguise);
        return this;
    }

    public RuleBuilder valueTransform(Transformer robotInDisguise) {
        rule.getValueTransformers().add(robotInDisguise);
        return this;
    }

}
