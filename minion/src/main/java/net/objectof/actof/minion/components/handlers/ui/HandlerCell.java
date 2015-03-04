package net.objectof.actof.minion.components.handlers.ui;


import java.io.IOException;

import javafx.scene.control.TableCell;
import net.objectof.actof.minion.classpath.MinionHandler;


public class HandlerCell extends TableCell<MinionHandler, MinionHandler> {

    HandlerUI ui;

    public HandlerCell() {
        try {
            ui = HandlerUI.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(MinionHandler item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            ui.setText(item.getTitle());
            ui.setIcon(item.getIcon());
            setGraphic(ui.getNode());
        }
    }

}