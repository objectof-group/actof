package net.objectof.actof.porter.rules;


import java.util.function.Predicate;

import net.objectof.actof.porter.rules.impl.Listener;
import net.objectof.actof.porter.rules.impl.Matchers;
import net.objectof.actof.porter.rules.impl.Transformer;
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

    public RuleBuilder matchKey(Object... keys) {
        for (Object key : keys) {
            matchKey(key);
        }
        return this;
    }

    public RuleBuilder matchKey(Object key) {
        rule.getMatchers().add(Matchers.matchKey(key));
        return this;
    }

    public RuleBuilder matchKind(String... kinds) {
        for (String kind : kinds) {
            matchKind(kind);
        }
        return this;
    }

    public RuleBuilder matchKind(String kind) {
        rule.getMatchers().add(Matchers.matchKind(kind));
        return this;
    }

    public RuleBuilder matchKind(Kind<?>... kinds) {
        for (Kind<?> kind : kinds) {
            matchKind(kind);
        }
        return this;
    }

    public RuleBuilder matchKind(Kind<?> kind) {
        rule.getMatchers().add(Matchers.matchKind(kind));
        return this;
    }

    public RuleBuilder matchStereotypes(Stereotype... stereotypes) {
        for (Stereotype stereotype : stereotypes) {
            matchStereotype(stereotype);
        }
        return this;
    }

    public RuleBuilder matchStereotype(Stereotype stereotype) {
        rule.getMatchers().add(Matchers.matchStereotype(stereotype));
        return this;
    }

    public RuleBuilder drop() {
        rule.getBeforeTransformListeners().add((source, destination) -> {
            source.setDropped(true);
        });
        return this;
    }

    public RuleBuilder setKey(Object newKey) {
        rule.getKeyTransformers().add((source, destination) -> newKey);
        return this;
    }

    public RuleBuilder setValue(Object newValue) {
        rule.getValueTransformers().add((source, destination) -> newValue);
        return this;
    }

    // /////////////////////////////
    // Generic
    // /////////////////////////////

    public RuleBuilder match(Predicate<IPorterContext> matcher) {
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

    public RuleBuilder beforeTransform(Listener listener) {
        rule.getBeforeTransformListeners().add(listener);
        return this;
    }

    public RuleBuilder afterTransform(Listener listener) {
        rule.getAfterTransformListeners().add(listener);
        return this;
    }

}
