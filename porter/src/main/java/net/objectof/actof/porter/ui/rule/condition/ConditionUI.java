package net.objectof.actof.porter.ui.rule.condition;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.porter.rules.RuleBuilder;
import net.objectof.actof.porter.ui.rule.RuleUI;
import net.objectof.actof.porter.ui.rule.condition.Action.Input;


public class ConditionUI extends IActofUIController {

    @FXML
    private ChoiceBox<Stage> stageChoice;
    @FXML
    private ChoiceBox<Action> actionChoice;

    @FXML
    private BorderPane borderpane;

    @FXML
    private Separator sep;

    @FXML
    private HBox hbox;

    @FXML
    private Button remove, up, down;

    private TextInputControl textInput = new TextField();

    private RuleUI ruleui;

    /**
     * DO NOT CALL
     */
    public ConditionUI() {}

    @Override
    public void ready() {
        stageChoice.getItems().setAll(Stage.values());
        stageChoice.getSelectionModel().selectedItemProperty().addListener(change -> {
            Stage stage = stageChoice.getSelectionModel().getSelectedItem();
            List<Action> actions = Conditions.forStage(stage);
            actionChoice.getItems().setAll(actions);
            if (actions.size() == 1) {
                actionChoice.getSelectionModel().select(0);
            }
        });

        actionChoice.getSelectionModel().selectedItemProperty().addListener(change -> {
            Action action = actionChoice.getSelectionModel().getSelectedItem();

            borderpane.setCenter(null);
            hbox.getChildren().setAll(remove, stageChoice, actionChoice, sep, up, down);
            hbox.setPadding(new Insets(0, 0, 0, 0));

            if (action.input == Input.CODE) {
                textInput = new TextArea(textInput.getText());
                textInput.setStyle("-fx-font-family: monospace");
                borderpane.setCenter(textInput);
                hbox.setPadding(new Insets(0, 0, 6, 0));
            } else if (action.input == Input.FIELD) {
                textInput = new TextField(textInput.getText());
                hbox.getChildren().setAll(remove, stageChoice, actionChoice, textInput, sep, up, down);
            }

            if (textInput.getText().length() == 0) {
                textInput.setText(action.defaultText);
            }

        });

        ImageView remIcon = ActofIcons.getCustomIcon(RuleUI.class, "icons/remove-symbolic-12.png");
        remove.setGraphic(remIcon);
        remove.setOnAction(event -> {
            ruleui.getConditions().remove(this);
        });

        ImageView upIcon = ActofIcons.getCustomIcon(RuleUI.class, "icons/up-symbolic-12.png");
        ImageView downIcon = ActofIcons.getCustomIcon(RuleUI.class, "icons/down-symbolic-12.png");
        up.setGraphic(upIcon);
        down.setGraphic(downIcon);

        remove.setVisible(false);
        up.setVisible(false);
        down.setVisible(false);

        getNode().setOnMouseEntered(event -> {
            remove.setVisible(true);
            up.setVisible(true);
            down.setVisible(true);
        });

        getNode().setOnMouseExited(event -> {
            remove.setVisible(false);
            up.setVisible(false);
            down.setVisible(false);
        });

    }

    public void onUp() {
        int index = ruleui.getConditions().indexOf(this);
        index = Math.max(0, index - 1);
        ruleui.getConditions().remove(this);
        ruleui.getConditions().add(index, this);
    }

    public void onDown() {
        int index = ruleui.getConditions().indexOf(this);
        index = Math.min(ruleui.getConditions().size() - 1, index + 1);
        ruleui.getConditions().remove(this);
        ruleui.getConditions().add(index, this);
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

    public Map<String, Object> toMap() {
        return new HashMap<String, Object>() {

            {
                Stage stage = stageChoice.getSelectionModel().getSelectedItem();
                put("stage", stage == null ? "" : stage);

                Action action = getAction();
                put("action", action == null ? "" : action.name);
                put("text", textInput.getText());
            }
        };
    }

    public void fromMap(Map<String, Object> data) {

        String stageName = data.get("stage").toString();
        Stage stage = null;
        if (stageName.length() > 0) {
            stage = Stage.valueOf(stageName);
            stageChoice.getSelectionModel().select(stage);
        }

        String actionName = data.get("action").toString();
        if (actionName.length() > 0 && stage != null) {
            Action action = Conditions.forStage(stage).stream().filter(a -> a.name.equals(actionName)).findFirst()
                    .get();
            actionChoice.getSelectionModel().select(action);
        }

        textInput.setText(data.get("text").toString());

    }

    public static ConditionUI load(ChangeController changes, RuleUI ruleui) throws IOException {
        ConditionUI ui = FXUtil.load(ConditionUI.class, "ConditionUI.fxml", changes);
        ui.ruleui = ruleui;
        return ui;
    }

}
