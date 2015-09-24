package net.objectof.actof.common.component.resource;


import java.util.Map;

import javafx.collections.ObservableList;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.feature.Titled;


public interface Resource extends Titled {

    Editor getEditor();

    Editor createEditor() throws Exception;

    ObservableList<Action> getActions();

    Map<String, Object> toSerializableForm();

    void fromSerializableForm(Map<String, Object> data);

    /**
     * Indicates if the resource is permanent (ie should be displayed, saved,
     * etc), or transient (ie just used as a way to create an editor/window)
     * 
     * @return true if transient, false otherwise
     */
    default boolean isTransient() {
        return false;
    }

}
