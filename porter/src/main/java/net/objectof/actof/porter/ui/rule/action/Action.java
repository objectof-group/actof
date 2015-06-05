package net.objectof.actof.porter.ui.rule.action;


import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.ui.rule.condition.Stage;


public interface Action {

    public void accept(Stage stage, RuleBuilder builder, String field);

}
