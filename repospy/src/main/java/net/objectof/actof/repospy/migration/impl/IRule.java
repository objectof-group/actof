package net.objectof.actof.repospy.migration.impl;


import java.util.ArrayList;
import java.util.List;

import net.objectof.actof.repospy.migration.Rule;
import net.objectof.actof.repospy.migration.rulecomponents.Matcher;
import net.objectof.actof.repospy.migration.rulecomponents.Transformer;
import net.objectof.model.Kind;


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
    public boolean match(Object key, Object value, Kind<?> kind) {
        for (Matcher matcher : matchers) {
            if (matcher.test(key, value, kind)) { return true; }
        }
        return false;
    }

    @Override
    public Object transformKey(Object key, Object value, Kind<?> kind) {
        for (Transformer keyTransformer : keyTransformers) {
            key = keyTransformer.apply(key, value, kind);
        }
        return key;
    }

    @Override
    public Object transformValue(Object key, Object value, Kind<?> kind) {
        for (Transformer valueTransformer : valueTransformers) {
            value = valueTransformer.apply(key, value, kind);
        }
        return value;
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
}
