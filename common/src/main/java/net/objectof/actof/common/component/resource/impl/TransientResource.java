package net.objectof.actof.common.component.resource.impl;


import java.util.Map;

import net.objectof.actof.common.component.editor.Editor;


public class TransientResource extends AbstractResource {

    private Editor editor;

    public TransientResource(Editor editor) {
        this.editor = editor;
        titleProperty().bind(this.editor.titleProperty());
    }

    @Override
    public Editor createEditor() throws Exception {
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
