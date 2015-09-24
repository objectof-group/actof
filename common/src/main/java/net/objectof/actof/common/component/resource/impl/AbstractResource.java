package net.objectof.actof.common.component.resource.impl;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;


public abstract class AbstractResource implements Resource {

    protected Editor editor;

    protected StringProperty title = new SimpleStringProperty("");

    private ObservableList<Action> actions = FXCollections.observableArrayList();

    @Override
    public Editor getEditor() {
        if (editor == null) {
            try {
                editor = createEditor();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return editor;
    }

    @Override
    public ObservableList<Action> getActions() {
        return actions;
    }

    public String toString() {
        return getTitle();
    }

    @Override
    public StringProperty titleProperty() {
        return title;
    }

}
