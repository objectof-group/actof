package net.objectof.actof.porter.ui.rule;


import java.io.IOException;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.ui.rule.condition.ConditionUI;
import net.objectof.actof.widgets.card.Card;


public class RuleUI extends Card {

    private VBox conditionsBox = new VBox();
    private RuleLabel title = new RuleLabel("Rule");
    private Button remove;

    private ObservableList<ConditionUI> conditions = FXCollections.observableArrayList();

    public RuleUI(ChangeController changes) {
        conditions.addListener((Observable change) -> layoutConditions());

        try {
            conditions.add(ConditionUI.load(changes));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        remove = new Button(null, ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
        remove.getStyleClass().add("tool-bar-button");

        setContent(conditionsBox);
        setTitle(title);
        setDescription(remove);

        VBox.setVgrow(this, Priority.ALWAYS);
    }

    private void layoutConditions() {
        conditionsBox.getChildren().setAll(conditions.stream().map(ConditionUI::getNode).collect(Collectors.toList()));
    }

    public ObservableList<ConditionUI> getConditions() {
        return conditions;
    }

    public Rule generateRule() {
        RuleBuilder rb = RuleBuilder.start();
        conditions.stream().forEach(c -> c.apply(rb));
        return rb.build();
    }
}
