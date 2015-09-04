package net.objectof.actof.common.component.editor.impl;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;


public abstract class AbstractEditor implements Editor {

    private StringProperty title = new SimpleStringProperty();
    private ObjectProperty<Stage> stageProperty = new SimpleObjectProperty<>(null);
    private ChangeController changeBus = new IChangeController();

    private BooleanProperty dismissible = new SimpleBooleanProperty(true);
    private BooleanProperty dismissed = new SimpleBooleanProperty(false);

    private ObservableList<Action> actions = FXCollections.observableArrayList();
    private ObservableList<Resource> resources = FXCollections.observableArrayList();
    private ObservableList<Node> toolbars = FXCollections.observableArrayList();
    private ObservableList<Panel> panels = FXCollections.observableArrayList();

    private ObjectProperty<Resource> resourceProperty = new SimpleObjectProperty<>(null);

    public AbstractEditor() {
        getResources().addListener((ListChangeListener.Change<? extends Resource> c) -> {
            while (c.next()) {
                if (!c.wasAdded()) { return; }
                for (Resource r : c.getAddedSubList()) {
                    try {
                        onResourceAdded(r);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected abstract void onResourceAdded(Resource res) throws Exception;

    @Override
    public ChangeController getChangeBus() {
        return changeBus;
    }

    @Override
    public void setChangeBus(ChangeController bus) {
        this.changeBus = bus;
    }

    public ObjectProperty<Resource> resourceProperty() {
        return resourceProperty;
    }

    @Override
    public ObjectProperty<Stage> stageProperty() {
        return stageProperty;
    }

    @Override
    public ObservableList<Action> getActions() {
        return actions;
    }

    @Override
    public ObservableList<Resource> getResources() {
        return resources;
    }

    @Override
    public ObservableList<Node> getToolbars() {
        return toolbars;
    }

    @Override
    public ObservableList<Panel> getPanels() {
        return panels;
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
