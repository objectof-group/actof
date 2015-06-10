package net.objectof.actof.porter.ui.action;


import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.ui.condition.Condition.Stage;


public interface Action {

    public void accept(Stage stage, RuleBuilder builder, String field);

}
