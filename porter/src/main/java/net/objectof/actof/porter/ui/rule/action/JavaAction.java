package net.objectof.actof.porter.ui.rule.action;


import java.util.function.BiConsumer;
import java.util.function.Function;

import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.rules.impl.Listener;
import net.objectof.actof.porter.rules.impl.Matcher;
import net.objectof.actof.porter.rules.impl.Transformer;
import net.objectof.actof.porter.ui.rule.condition.Stage;


public class JavaAction implements Action {

    private BiConsumer<RuleBuilder, String> act;

    public JavaAction() {}

    @Override
    public void accept(Stage stage, RuleBuilder t, String u) {
        act.accept(t, u);
    }

    public JavaAction setBeforeAct(Function<String, Listener> forText) {
        act = (rb, text) -> rb.beforeTransform(forText.apply(text));
        return this;
    }

    public JavaAction setAfterAct(Function<String, Listener> forText) {
        act = (rb, text) -> rb.afterTransform(forText.apply(text));
        return this;
    }

    public JavaAction setKeyAct(Function<String, Transformer> forText) {
        act = (rb, text) -> rb.keyTransform(forText.apply(text));
        return this;
    }

    public JavaAction setValueAct(Function<String, Transformer> forText) {
        act = (rb, text) -> rb.valueTransform(forText.apply(text));
        return this;
    }

    public JavaAction setMatcherAct(Function<String, Matcher> forText) {
        act = (rb, text) -> rb.match(forText.apply(text));
        return this;
    }

}
