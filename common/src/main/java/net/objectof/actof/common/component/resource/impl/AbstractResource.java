package net.objectof.actof.common.component.resource.impl;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;


public abstract class AbstractResource implements Resource {

    protected ResourceEditor display;

    private ObservableList<Action> actions = FXCollections.observableArrayList();

    @Override
    public ResourceEditor getEditor() throws Exception {
        if (display == null) {
            display = createDisplay();
        }
        return display;
    }

    @Override
    public ObservableList<Action> getActions() {
        return actions;
    }

}
