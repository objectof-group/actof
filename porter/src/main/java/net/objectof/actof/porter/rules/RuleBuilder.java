package net.objectof.actof.porter.rules;


import java.util.function.BiConsumer;

import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.rules.components.Matcher;
import net.objectof.actof.porter.rules.components.Transformer;
import net.objectof.actof.porter.rules.components.impl.KeyMatcher;
import net.objectof.actof.porter.rules.components.impl.PrettyPrintMatcher;
import net.objectof.actof.porter.rules.components.impl.PrettyPrintTransformer;
import net.objectof.actof.porter.rules.components.impl.StereotypeMatcher;
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

    public RuleBuilder drop() {
        rule.getKeyTransformers().add(context -> {
            context.setDropped(true);
            return null;
        });
        return this;
    }

    public RuleBuilder setKey(Object newKey) {
        rule.getKeyTransformers().add(context -> newKey);
        return this;
    }

    public RuleBuilder setValue(Object newValue) {
        rule.getValueTransformers().add(context -> newValue);
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

    public RuleBuilder onPort(BiConsumer<PorterContext, PorterContext> listener) {
        rule.getOnPortListeners().add(listener);
        return this;
    }

}
