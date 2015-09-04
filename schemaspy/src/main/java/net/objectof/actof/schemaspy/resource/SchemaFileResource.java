package net.objectof.actof.schemaspy.resource;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.resource.impl.AbstractResource;
import net.objectof.actof.schemaspy.SchemaSpyController;


public class SchemaFileResource extends AbstractResource {

    private File schemaFile;

    @Override
    public Editor createEditor() throws Exception {
        return new SchemaSpyController();
    }

    public File getSchemaFile() {
        return schemaFile;
    }

    public void setSchemaFile(File schemaFile) {
        this.schemaFile = schemaFile;
    }

    @Override
    public String getTitle() {
        return schemaFile.getName();
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public Map<String, Object> toSerializableForm() {
        Map<String, Object> map = new HashMap<>();
        map.put("file", schemaFile.getAbsolutePath());
        return map;
    }

    @Override
    public void fromSerializableForm(Map<String, Object> data) {
        String filename = data.get("file").toString();
        schemaFile = new File(filename);
    }

}
