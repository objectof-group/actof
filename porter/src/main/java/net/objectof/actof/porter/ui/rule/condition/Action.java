package net.objectof.actof.porter.ui.rule.condition;


import java.util.function.BiConsumer;

import net.objectof.actof.porter.rules.RuleBuilder;


public class Action {

    public enum Input {
        NONE, SMALL, LARGE;
    }

    public String name;
    public BiConsumer<RuleBuilder, String> act;
    public Input input;

    public Action(String name, Input input, BiConsumer<RuleBuilder, String> act) {
        this.name = name;
        this.act = act;
        this.input = input;
    }

    public String toString() {
        return name;
    }

}
