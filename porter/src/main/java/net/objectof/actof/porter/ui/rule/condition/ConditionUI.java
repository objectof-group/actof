package net.objectof.actof.porter.ui.rule.condition;


import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.ui.rule.condition.Action.Input;


public class ConditionUI extends IActofUIController {

    @FXML
    private ChoiceBox<String> stageChoice;
    @FXML
    private ChoiceBox<Action> actionChoice;

    @FXML
    private BorderPane borderpane;

    @FXML
    private HBox hbox;

    private TextInputControl textInput = new TextField();

    @Override
    public void ready() {
        stageChoice.getItems().setAll(Conditions.conditions.keySet());
        stageChoice.getSelectionModel().selectedItemProperty().addListener(change -> {
            String stage = stageChoice.getSelectionModel().getSelectedItem();
            List<Action> actions = Conditions.conditions.get(stage);
            actionChoice.getItems().setAll(actions);
            if (actions.size() == 1) {
                actionChoice.getSelectionModel().select(0);
            }
        });

        actionChoice.getSelectionModel().selectedItemProperty().addListener(change -> {
            Action action = actionChoice.getSelectionModel().getSelectedItem();

            borderpane.setCenter(null);
            hbox.getChildren().setAll(stageChoice, actionChoice);
            hbox.setPadding(new Insets(0, 0, 0, 0));

            if (action.input == Input.LARGE) {
                textInput = new TextArea(textInput.getText());
                borderpane.setCenter(textInput);
                hbox.setPadding(new Insets(0, 0, 6, 0));
            } else if (action.input == Input.SMALL) {
                textInput = new TextField(textInput.getText());
                hbox.getChildren().setAll(stageChoice, actionChoice, textInput);
            }
        });

    }

    private Action getAction() {
        return actionChoice.getSelectionModel().getSelectedItem();
    }

    public void apply(RuleBuilder rb) {
        getAction().act.accept(rb, textInput.getText());
    }

    @Override
    protected void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    public static ConditionUI load(ChangeController changes) throws IOException {
        return FXUtil.load(ConditionUI.class, "ConditionUI.fxml", changes);
    }

}
