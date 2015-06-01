package net.objectof.actof.porter.rules;


import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import net.objectof.actof.porter.rules.impl.IKeyMatcher;
import net.objectof.actof.porter.rules.impl.IKindMatcher;
import net.objectof.actof.porter.rules.impl.IKindNameMatcher;
import net.objectof.actof.porter.rules.impl.IPrettyPrintMatcher;
import net.objectof.actof.porter.rules.impl.IPrettyPrintTransformer;
import net.objectof.actof.porter.rules.impl.IStereotypeMatcher;
import net.objectof.actof.porter.visitor.IPorterContext;
import net.objectof.model.Kind;
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

    public RuleBuilder matchKey(Object... key) {
        rule.getMatchers().add(new IKeyMatcher(key));
        return this;
    }

    public RuleBuilder matchKind(String kind) {
        rule.getMatchers().add(new IKindNameMatcher(kind));
        return this;
    }

    public RuleBuilder matchKind(Kind<?> kind) {
        rule.getMatchers().add(new IKindMatcher(kind));
        return this;
    }

    public RuleBuilder matchStereotype(Stereotype... stereotype) {
        rule.getMatchers().add(new IStereotypeMatcher(stereotype));
        return this;
    }

    public RuleBuilder drop() {
        rule.getBeforeTransformListeners().add(context -> {
            context.setDropped(true);
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

    public RuleBuilder match(Predicate<IPorterContext> matcher) {
        rule.getMatchers().add(new IPrettyPrintMatcher(matcher));
        return this;
    }

    public RuleBuilder keyTransform(Function<IPorterContext, Object> robotInDisguise) {
        rule.getKeyTransformers().add(new IPrettyPrintTransformer(robotInDisguise));
        return this;
    }

    public RuleBuilder valueTransform(Function<IPorterContext, Object> robotInDisguise) {
        rule.getValueTransformers().add(new IPrettyPrintTransformer(robotInDisguise));
        return this;
    }

    public RuleBuilder beforeTransform(Consumer<IPorterContext> listener) {
        rule.getBeforeTransformListeners().add(listener);
        return this;
    }

    public RuleBuilder afterTransform(BiConsumer<IPorterContext, IPorterContext> listener) {
        rule.getAfterTransformListeners().add(listener);
        return this;
    }

}
