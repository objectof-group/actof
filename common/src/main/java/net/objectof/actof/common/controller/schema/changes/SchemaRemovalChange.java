package net.objectof.actof.common.controller.schema.changes;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;


public class SchemaRemovalChange extends Change {

    SchemaEntry entry;

    public SchemaRemovalChange(SchemaEntry entry) {
        this.entry = entry;
    }


    public SchemaEntry getEntry() {
        return entry;
    }

}
