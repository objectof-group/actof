package net.objectof.actof.ide.resource;


import java.io.File;

import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.component.Resource;
import net.objectof.actof.schemaspy.SchemaSpyController;


public class SchemaFileResource implements Resource {

    private File schemaFile;

    @Override
    public Display getDisplay() throws Exception {
        Display display = new SchemaSpyController();
        display.setResource(this);
        return display;
    }

    public File getSchemaFile() {
        return schemaFile;
    }

    public void setSchemaFile(File schemaFile) {
        this.schemaFile = schemaFile;
    }

}
