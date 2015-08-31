package net.objectof.actof.common.component.resource;


import java.util.Map;

import javafx.collections.ObservableList;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.feature.Titled;


public interface Resource extends Titled {

    ResourceEditor getEditor();

    ResourceEditor createEditor() throws Exception;

    ObservableList<Action> getActions();

    Map<String, Object> toSerializableForm();

    void fromSerializableForm(Map<String, Object> data);

}
