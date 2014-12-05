package net.objectof.actof.minion.components.handlers.ui;


import java.io.IOException;

import javafx.scene.control.ListCell;
import net.objectof.actof.minion.components.handlers.HandlerClass;


public class HandlerCell extends ListCell<HandlerClass> {

    HandlerUI ui;


    public HandlerCell() {
        try {
            ui = HandlerUI.load();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(HandlerClass item, boolean empty) {
        super.updateItem(item, empty);
        setText(null); // No text in label of super class
        if (empty) {
            setGraphic(null);
        } else {
            // TODO: Remove this
            String name = item.getName();
            String[] parts = name.split("-");
            name = parts[0];
            name = name.substring(0, 1).toUpperCase() + name.substring(1);

            ui.setText(item != null ? name : "<null>");
            ui.setIcon(item.getIcon());
            setGraphic(ui.getNode());
        }
    }

}
