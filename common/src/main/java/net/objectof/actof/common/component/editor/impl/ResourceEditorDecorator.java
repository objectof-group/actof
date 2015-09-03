package net.objectof.actof.common.component.editor.impl;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.controller.change.ChangeController;


public class ResourceEditorDecorator implements ResourceEditor {

    private Editor editor;

    public ResourceEditorDecorator(Editor editor) {
        this.editor = editor;
    }

    @Override
    public Display getDisplay() {
        return editor.getDisplay();
    }

    @Override
    public ObservableList<Action> getActions() {
        return editor.getActions();
    }

    @Override
    public ObservableList<Resource> getResources() {
        return editor.getResources();
    }

    @Override
    public ObservableList<Node> getToolbars() {
        return editor.getToolbars();
    }

    @Override
    public ObservableList<Panel> getPanels() {
        return editor.getPanels();
    }

    @Override
    public StringProperty titleProperty() {
        return editor.titleProperty();
    }

    @Override
    public ChangeController getChangeBus() {
        return editor.getChangeBus();
    }

    @Override
    public void setChangeBus(ChangeController bus) {
        editor.setChangeBus(bus);
    }

    @Override
    public Stage getDisplayStage() {
        return editor.getDisplayStage();
    }

    @Override
    public void setDisplayStage(Stage stage) {
        editor.setDisplayStage(stage);
    }

    @Override
    public void construct() throws Exception {
        editor.construct();
    }

    @Override
    public BooleanProperty dismissibleProperty() {
        return editor.dismissibleProperty();
    }

    @Override
    public BooleanProperty dismissedProperty() {
        return editor.dismissedProperty();
    }

    @Override
    public boolean isForResource() {
        return false;
    }

    @Override
    public void setForResource(boolean forResource) {}

    @Override
    public Resource getTargetResource() {
        return null;
    }

    @Override
    public void setTargetResource(Resource resource) {}

    @Override
    public void loadResource() throws Exception {}

}