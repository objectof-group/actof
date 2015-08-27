package net.objectof.actof.ide.resource;


import java.util.HashMap;
import java.util.Map;

import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;


public class ProjectResource extends AbstractResource {

    @Override
    public ResourceEditor createEditor() throws Exception {
        return null;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public Map<String, Object> toSerializableForm() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @Override
    public void fromSerializableForm(Map<String, Object> data) {}

}
