package net.objectof.actof.porter.ui.rule;


import java.io.IOException;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.porter.rules.Rule;
import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.ui.porter.PorterUIController;
import net.objectof.actof.porter.ui.rule.condition.ConditionUI;
import net.objectof.actof.widgets.card.Card;


public class RuleUI extends Card {

    private BorderPane contents = new BorderPane();
    private VBox conditionsBox = new VBox();
    private HBox buttonBox = new HBox();
    private RuleLabel title = new RuleLabel("Rule");
    private Button removeRule;
    private Button addCondition;

    private PorterUIController porterUI;

    private ObservableList<ConditionUI> conditions = FXCollections.observableArrayList();

    public RuleUI(ChangeController changes, PorterUIController porterUI) {
        this.porterUI = porterUI;
        conditions.addListener((Observable change) -> layoutConditions());

        try {
            conditions.add(ConditionUI.load(changes, this));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        addCondition = new Button(null, ActofIcons.getIconView(Icon.ADD, Size.BUTTON));
        addCondition.getStyleClass().add("tool-bar-button");
        addCondition.setOnAction(event -> {
            try {
                conditions.add(ConditionUI.load(changes, this));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonBox.getChildren().add(addCondition);
        buttonBox.setPadding(new Insets(6, 0, 0, 0));

        conditionsBox.setSpacing(6);

        contents.setCenter(conditionsBox);
        contents.setBottom(buttonBox);

        removeRule = new Button(null, ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
        removeRule.getStyleClass().add("tool-bar-button");
        removeRule.setOnAction(event -> {
            porterUI.getRules().remove(this);
        });

        setContent(contents);
        setTitle(title);
        setDescription(removeRule);

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
