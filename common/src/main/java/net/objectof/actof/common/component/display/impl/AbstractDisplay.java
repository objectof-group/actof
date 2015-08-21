package net.objectof.actof.common.component.display.impl;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;


public abstract class AbstractDisplay implements Display {

    private Stage displayStage;
    private ChangeController changeBus = new IChangeController();

    private ObservableList<Node> toolbars = FXCollections.observableArrayList();
    private ObservableList<Panel> panels = FXCollections.observableArrayList();

    public Stage getDisplayStage() {
        return displayStage;
    }

    public void setDisplayStage(Stage displayStage) {
        this.displayStage = displayStage;
    }

    public ChangeController getChangeBus() {
        return changeBus;
    }

    public void setChangeBus(ChangeController bus) {
        this.changeBus = bus;
    }

    @Override
    public ObservableList<Node> getToolbars() {
        return toolbars;
    }

    @Override
    public ObservableList<Panel> getPanels() {
        return panels;
    }

}
