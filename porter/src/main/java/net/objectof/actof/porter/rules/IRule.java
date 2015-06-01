package net.objectof.actof.porter.rules;


import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import net.objectof.actof.porter.visitor.IPorterContext;


public class IRule implements Rule {

    private List<Predicate<IPorterContext>> matchers = new ArrayList<>();
    private List<Function<IPorterContext, Object>> keyTransformers = new ArrayList<>();
    private List<Function<IPorterContext, Object>> valueTransformers = new ArrayList<>();
    private List<BiConsumer<IPorterContext, IPorterContext>> afterTransformListeners = new ArrayList<>();
    private List<Consumer<IPorterContext>> beforeTransformListeners = new ArrayList<>();

    public IRule() {}

    public IRule(Predicate<IPorterContext> matcher, Function<IPorterContext, Object> keyTransformer,
            Function<IPorterContext, Object> valueTransformer) {
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
    public Object transformKey(IPorterContext context) {
        for (Function<IPorterContext, Object> keyTransformer : keyTransformers) {
            context.setKey(keyTransformer.apply(context));
        }
        return context.getKey();
    }

    @Override
    public Object transformValue(IPorterContext context) {
        for (Function<IPorterContext, Object> valueTransformer : valueTransformers) {
            context.setValue(valueTransformer.apply(context));
        }
        return context.getValue();
    }

    public List<Predicate<IPorterContext>> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<Predicate<IPorterContext>> matchers) {
        this.matchers = matchers;
    }

    public List<Function<IPorterContext, Object>> getKeyTransformers() {
        return keyTransformers;
    }

    public void setKeyTransformers(List<Function<IPorterContext, Object>> keyTransformers) {
        this.keyTransformers = keyTransformers;
    }

    public List<Function<IPorterContext, Object>> getValueTransformers() {
        return valueTransformers;
    }

    public void setValueTransformers(List<Function<IPorterContext, Object>> valueTransformers) {
        this.valueTransformers = valueTransformers;
    }

    public List<BiConsumer<IPorterContext, IPorterContext>> getAfterTransformListeners() {
        return afterTransformListeners;
    }

    public void setAfterTransformListeners(List<BiConsumer<IPorterContext, IPorterContext>> afterTransformListeners) {
        this.afterTransformListeners = afterTransformListeners;
    }

    public List<Consumer<IPorterContext>> getBeforeTransformListeners() {
        return beforeTransformListeners;
    }

    public void setBeforeTransformListeners(List<Consumer<IPorterContext>> beforeTransformListeners) {
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
    public void beforeTransform(IPorterContext context) {
        for (Consumer<IPorterContext> listener : beforeTransformListeners) {
            listener.accept(context);
        }
    }

    @Override
    public void afterTransform(IPorterContext source, IPorterContext destination) {
        for (BiConsumer<IPorterContext, IPorterContext> listener : afterTransformListeners) {
            listener.accept(source, destination);
        }
    }

}
