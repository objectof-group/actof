package net.objectof.actof.porter.impl;


import net.objectof.actof.porter.Rule;
import net.objectof.actof.porter.rulecomponents.Matcher;
import net.objectof.actof.porter.rulecomponents.Transformer;
import net.objectof.actof.porter.rulecomponents.impl.KeyMatcher;
import net.objectof.actof.porter.rulecomponents.impl.PrettyPrintMatcher;
import net.objectof.actof.porter.rulecomponents.impl.PrettyPrintTransformer;
import net.objectof.actof.porter.rulecomponents.impl.ReplaceTransformer;
import net.objectof.actof.porter.rulecomponents.impl.StereotypeMatcher;
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
        rule.getMatchers().add(new PrettyPrintMatcher(matcher));
        return this;
    }

    public RuleBuilder keyTransform(Transformer robotInDisguise) {
        rule.getKeyTransformers().add(new PrettyPrintTransformer(robotInDisguise));
        return this;
    }

    public RuleBuilder valueTransform(Transformer robotInDisguise) {
        rule.getValueTransformers().add(new PrettyPrintTransformer(robotInDisguise));
        return this;
    }

}
