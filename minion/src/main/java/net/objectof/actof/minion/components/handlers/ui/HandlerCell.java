package net.objectof.actof.minion.components.handlers.ui;


import java.io.IOException;

import javafx.scene.control.ListCell;
import net.objectof.actof.minion.classpath.minionhandler.MinionHandler;
import net.objectof.actof.minion.classpath.minionhandler.MinionHandler.IconSize;
import net.objectof.actof.minion.classpath.minionhandler.MinionHandler.IconStyle;


public class HandlerCell extends ListCell<MinionHandler> {

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
            ui.setIcon(item.getIcon(IconStyle.BLACK, IconSize.SIZE_16));
            setGraphic(ui.getNode());
        }
    }

}