package net.objectof.actof.common.component.editor.impl;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;


public abstract class AbstractEditor implements Editor {

    private Stage displayStage;
    private ChangeController changeBus = new IChangeController();

    private ObservableList<Action> actions = FXCollections.observableArrayList();

    @Override
    public ChangeController getChangeBus() {
        return changeBus;
    }

    @Override
    public void setChangeBus(ChangeController bus) {
        this.changeBus = bus;
    }

    @Override
    public Stage getDisplayStage() {
        return displayStage;
    }

    @Override
    public void setDisplayStage(Stage stage) {
        this.displayStage = stage;
    }

    @Override
    public ObservableList<Action> getActions() {
        return actions;
    }

}
