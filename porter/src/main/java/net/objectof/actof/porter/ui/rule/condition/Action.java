package net.objectof.actof.porter.ui.rule.condition;


import java.util.function.BiConsumer;

import net.objectof.actof.porter.rules.RuleBuilder;


public class Action {

    public enum Input {
        NONE, FIELD, CODE;
    }

    public String name;
    public BiConsumer<RuleBuilder, String> act;
    public Input input;
    public String defaultText;
    public Stage stage;

    public Action(Stage stage, String name, Input input, BiConsumer<RuleBuilder, String> act) {
        this(stage, name, input, act, "");
    }

    public Action(Stage stage, String name, Input input, BiConsumer<RuleBuilder, String> act, String defaultText) {
        this.stage = stage;
        this.name = name;
        this.act = act;
        this.input = input;
        this.defaultText = defaultText;
    }

    public String toString() {
        return name;
    }

}
