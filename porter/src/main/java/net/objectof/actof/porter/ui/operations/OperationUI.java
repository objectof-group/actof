package net.objectof.actof.porter.ui.operations;


import java.io.IOException;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.Icon;
import net.objectof.actof.common.icons.Size;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.porter.ui.action.JsAction;
import net.objectof.actof.porter.ui.condition.Condition;
import net.objectof.actof.porter.ui.condition.Condition.Input;
import net.objectof.actof.porter.ui.condition.Condition.Stage;


public class OperationUI extends IActofUIController {

    @FXML
    private Button add, remove, save;

    @FXML
    private ListView<Operation> operations;

    @FXML
    private ChoiceBox<Stage> stage;

    @FXML
    private TextField name;

    @FXML
    private ChoiceBox<Input> input;

    @FXML
    private TextArea defaultText, hint, code;

    FilteredList<Operation> filtered;

    @Override
    public void ready() {
        add.setGraphic(ActofIcons.getIconView(Icon.ADD, Size.BUTTON));
        remove.setGraphic(ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
        save.setGraphic(ActofIcons.getCustomIcon(OperationUI.class, "../icons/document-save.png"));

        stage.getItems().addAll(Stage.values());
        input.getItems().addAll(Input.values());

        filtered = Operations.getOperations().filtered(op -> op.getAction() instanceof JsAction);
        operations.setItems(filtered);

        operations.getSelectionModel().selectedItemProperty().addListener(event -> {
            populate(getSelectedOp());
        });
        populate(getSelectedOp());

        stage.getSelectionModel().selectedItemProperty().addListener(event -> {
            getSelectedOp().getCondition().setStage(stage.getSelectionModel().getSelectedItem());
            getSelectedOp().setTitle(getSelectedOp().toString());
        });

        name.textProperty().addListener(event -> {
            getSelectedOp().getCondition().setName(name.getText());
            getSelectedOp().setTitle(getSelectedOp().toString());
        });

        input.getSelectionModel().selectedItemProperty().addListener(event -> {
            getSelectedOp().getCondition().setInput(input.getSelectionModel().getSelectedItem());
        });

        defaultText.textProperty().addListener(event -> {
            getSelectedOp().getCondition().setDefaultText(defaultText.getText());
        });

        hint.textProperty().addListener(event -> {
            getSelectedOp().getCondition().setHint(hint.getText());
        });

        code.textProperty().addListener(event -> {
            JsAction action = (JsAction) getSelectedOp().getAction();
            action.setCode(code.getText());
        });

    }

    private Operation getSelectedOp() {
        return operations.getSelectionModel().getSelectedItem();
    }

    private void populate(Operation op) {
        if (op == null) {

            stage.getSelectionModel().select(null);
            name.setText(null);
            input.getSelectionModel().select(null);
            defaultText.setText(null);
            hint.setText(null);
            code.setText(null);

            stage.setDisable(true);
            name.setDisable(true);
            input.setDisable(true);
            defaultText.setDisable(true);
            hint.setDisable(true);
            code.setDisable(true);

            return;
        } else {
            stage.setDisable(false);
            name.setDisable(false);
            input.setDisable(false);
            defaultText.setDisable(false);
            hint.setDisable(false);
            code.setDisable(false);
        }

        Condition cond = op.getCondition();
        JsAction action = (JsAction) op.getAction();
        stage.getSelectionModel().select(cond.getStage());
        name.setText(cond.getName());
        input.getSelectionModel().select(cond.getInput());
        defaultText.setText(cond.getDefaultText());
        hint.setText(cond.getHint());
        code.setText(action.getCode());
    }

    @Override
    protected void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    public void onAdd() {
        Operation op = new Operation();
        op.setCondition(new Condition());
        op.setAction(new JsAction());
        Operations.getOperations().add(op);
    }

    public void onRemove() {
        Operations.getOperations().remove(getSelectedOp());
    }

    public void onSave() throws IOException {
        JsOperations.save(filtered);
    }

    public static OperationUI load(ChangeController changes) throws IOException {
        return FXUtil.load(OperationUI.class, "Operation.fxml", changes);
    }

}
