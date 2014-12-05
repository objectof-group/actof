package net.objectof.actof.common.controller.schema.changes;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;


public class SchemaInsertChange extends Change {

    SchemaEntry entry;

    public SchemaInsertChange(SchemaEntry entry) {
        this.entry = entry;
    }


    public SchemaEntry getEntry() {
        return entry;
    }

}
