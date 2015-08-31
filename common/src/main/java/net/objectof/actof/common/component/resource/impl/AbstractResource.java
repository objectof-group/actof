package net.objectof.actof.common.component.resource.impl;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;


public abstract class AbstractResource implements Resource {

    protected ResourceEditor editor;

    protected String title;

    private ObservableList<Action> actions = FXCollections.observableArrayList();

    @Override
    public ResourceEditor getEditor() {
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
