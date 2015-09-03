package net.objectof.actof.common.component.resource.impl;


import java.util.Map;

import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.editor.impl.ResourceEditorDecorator;


public class TransientResource extends AbstractResource {

    private ResourceEditor editor;

    public TransientResource(Editor editor) {
        this.editor = new ResourceEditorDecorator(editor);
        titleProperty().bind(this.editor.titleProperty());
    }

    @Override
    public ResourceEditor createEditor() throws Exception {
        return editor;
    }

    @Override
    public Map<String, Object> toSerializableForm() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void fromSerializableForm(Map<String, Object> data) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isTransient() {
        return true;
    }

}
