package net.objectof.actof.porter.ui.operations;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.common.util.FXUtil;


public class OperationUI extends IActofUIController {

    @FXML
    private Button add, remove;

    @Override
    public void ready() {
        add.setGraphic(ActofIcons.getIconView(Icon.ADD, Size.BUTTON));
        remove.setGraphic(ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
    }

    @Override
    protected void initialize() throws Exception {
        // TODO Auto-generated method stub

    }

    public void onAdd() {

    }

    public void onRemove() {

    }

    public static OperationUI load(ChangeController changes) throws IOException {
        return FXUtil.load(OperationUI.class, "Operation.fxml", changes);
    }

}
