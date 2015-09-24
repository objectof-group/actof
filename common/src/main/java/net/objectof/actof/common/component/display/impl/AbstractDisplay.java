package net.objectof.actof.common.component.display.impl;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;


public abstract class AbstractDisplay implements Display {

    private StringProperty title = new SimpleStringProperty();
    private ObjectProperty<Stage> stageProperty = new SimpleObjectProperty<>();
    private ObjectProperty<ChangeController> changeBusProperty = new SimpleObjectProperty<>(new IChangeController());

    private BooleanProperty dismissible = new SimpleBooleanProperty(true);
    private BooleanProperty dismissed = new SimpleBooleanProperty(false);

    public ObjectProperty<Stage> stageProperty() {
        return stageProperty;
    }

    @Override
    public ObjectProperty<ChangeController> changeBusProperty() {
        return changeBusProperty;
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
