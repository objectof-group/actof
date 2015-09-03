package net.objectof.actof.common.component.display.impl;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;


public abstract class AbstractDisplay implements Display {

    private StringProperty title = new SimpleStringProperty();
    private Stage displayStage;
    private ChangeController changeBus = new IChangeController();

    private BooleanProperty dismissible = new SimpleBooleanProperty(true);
    private BooleanProperty dismissed = new SimpleBooleanProperty(false);

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
    public StringProperty titleProperty() {
        return title;
    }

    @Override
    public BooleanProperty dismissibleProperty() {
        return dismissible;
    }

    @Override
    public BooleanProperty dismissedProperty() {
        return dismissed;
    }

}
