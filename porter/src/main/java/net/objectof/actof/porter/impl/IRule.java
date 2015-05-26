package net.objectof.actof.porter.impl;


import java.util.ArrayList;
import java.util.List;

import net.objectof.actof.porter.PorterContext;
import net.objectof.actof.porter.Rule;
import net.objectof.actof.porter.rulecomponents.Matcher;
import net.objectof.actof.porter.rulecomponents.Transformer;


public class IRule implements Rule {

    private List<Matcher> matchers = new ArrayList<>();
    private List<Transformer> keyTransformers = new ArrayList<>();
    private List<Transformer> valueTransformers = new ArrayList<>();

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
    public boolean match(PorterContext context) {
        for (Matcher matcher : matchers) {
            if (matcher.test(context)) { return true; }
        }
        return false;
    }

    @Override
    public Object transformKey(PorterContext context) {
        PorterContext modContext = context.copy();
        for (Transformer keyTransformer : keyTransformers) {
            modContext.setKey(keyTransformer.apply(modContext));
        }
        return modContext.getKey();
    }

    @Override
    public Object transformValue(PorterContext context) {
        PorterContext modContext = new PorterContext(context);
        for (Transformer valueTransformer : valueTransformers) {
            modContext.setValue(valueTransformer.apply(modContext));
        }
        return modContext.getValue();
    }

    public List<Matcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(List<Matcher> matchers) {
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

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Rule matching [");
        sb.append(matchers.stream().map(Matcher::toString).reduce((a, b) -> a + ", " + b).orElse(""));

        sb.append("]\n     transforming keys via [");
        sb.append(keyTransformers.stream().map(Transformer::toString).reduce((a, b) -> a + ", " + b).orElse(""));

        sb.append("]\n     transforming values via [");
        sb.append(valueTransformers.stream().map(Transformer::toString).reduce((a, b) -> a + ", " + b).orElse(""));

        sb.append("]");

        return sb.toString();
    }

    @Override
    public boolean modifiesKey(PorterContext context) {
        return match(context) && keyTransformers.size() > 0;
    }

    @Override
    public boolean modifiesValue(PorterContext context) {
        return match(context) && valueTransformers.size() > 0;
    }

}
