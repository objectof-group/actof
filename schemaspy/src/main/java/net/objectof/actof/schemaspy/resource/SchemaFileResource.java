package net.objectof.actof.schemaspy.resource;


import java.io.File;

import net.objectof.actof.common.component.display.ResourceDisplay;
import net.objectof.actof.common.component.display.impl.AbstractResource;
import net.objectof.actof.schemaspy.SchemaSpyController;


public class SchemaFileResource extends AbstractResource {

    private File schemaFile;

    @Override
    public ResourceDisplay createDisplay() throws Exception {
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

}
