package net.objectof.actof.porter.rules;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.objectof.actof.porter.rules.impl.Listener;
import net.objectof.actof.porter.rules.impl.Matcher;
import net.objectof.actof.porter.rules.impl.Transformer;
import net.objectof.actof.porter.visitor.IPorterContext;


public class IRule implements Rule {

    private List<Predicate<IPorterContext>> matchers = new ArrayList<>();
    private List<Transformer> keyTransformers = new ArrayList<>();
    private List<Transformer> valueTransformers = new ArrayList<>();
    private List<Listener> afterTransformListeners = new ArrayList<>();
    private List<Listener> beforeTransformListeners = new ArrayList<>();

    public IRule() {}

    public IRule(Matcher matcher, Transformer keyTransformer, Transformer valueTransformer) {
        if (matcher != null) {
            matchers.add(matcher);
        }
        if (keyTransformer != null) {
            keyTransformers.add(keyTransformer);
        }
        if (valueTransformer != null) {
            valueTransformers.add(valueTransformer);
        }
    }

    @Override
    public boolean match(IPorterContext context) {
        for (Predicate<IPorterContext> matcher : matchers) {
            if (matcher.test(context)) { return true; }
        }
        return false;
    }

    @Override
    public Object transformKey(IPorterContext source, IPorterContext destination) {
        for (Transformer keyTransformer : keyTransformers) {
            source.setKey(keyTransformer.apply(source, destination));
        }
        return source.getKey();
    }

    @Override
    public Object transformValue(IPorterContext source, IPorterContext destination) {
        for (Transformer valueTransformer : valueTransformers) {
            source.setValue(valueTransformer.apply(source, destination));
        }
        return source.getValue();
    }

    public List<Predicate<IPorterContext>> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<Predicate<IPorterContext>> matchers) {
        this.matchers = matchers;
    }

    public List<Transformer> getKeyTransformers() {
        return keyTransformers;
    }

    public void setKeyTransformers(List<Transformer> keyTransformers) {
        this.keyTransformers = keyTransformers;
    }

    public List<Transformer> getValueTransformers() {
        return valueTransformers;
    }

    public void setValueTransformers(List<Transformer> valueTransformers) {
        this.valueTransformers = valueTransformers;
    }

    public List<Listener> getAfterTransformListeners() {
        return afterTransformListeners;
    }

    public void setAfterTransformListeners(List<Listener> afterTransformListeners) {
        this.afterTransformListeners = afterTransformListeners;
    }

    public List<Listener> getBeforeTransformListeners() {
        return beforeTransformListeners;
    }

    public void setBeforeTransformListeners(List<Listener> beforeTransformListeners) {
        this.beforeTransformListeners = beforeTransformListeners;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Rule matching [");
        sb.append(matchers.stream().map(Predicate::toString).reduce((a, b) -> a + ", " + b).orElse(""));

        sb.append("]\n     transforming keys via [");
        sb.append(keyTransformers.stream().map(Object::toString).reduce((a, b) -> a + ", " + b).orElse(""));

        sb.append("]\n     transforming values via [");
        sb.append(valueTransformers.stream().map(Object::toString).reduce((a, b) -> a + ", " + b).orElse(""));

        sb.append("]");

        return sb.toString();
    }

    @Override
    public void beforeTransform(IPorterContext source, IPorterContext destination) {
        for (Listener listener : beforeTransformListeners) {
            listener.accept(source, destination);
        }
    }

    @Override
    public void afterTransform(IPorterContext source, IPorterContext destination) {
        for (BiConsumer<IPorterContext, IPorterContext> listener : afterTransformListeners) {
            listener.accept(source, destination);
        }
    }

}
