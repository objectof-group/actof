package net.objectof.actof.common.component.resource;


import javafx.collections.ObservableList;
import net.objectof.actof.common.component.editor.ResourceEditor;


public interface Resource {

    ResourceEditor getEditor() throws Exception;

    ResourceEditor createEditor() throws Exception;

    String getTitle();

    ObservableList<Action> getActions();

}
