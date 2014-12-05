package net.objectof.actof.common.controller.schema.changes;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.schema.AttributeEntry;


public class AttributeValueChange extends Change {

    private AttributeEntry entry;

    public AttributeValueChange(AttributeEntry entry) {
        this.entry = entry;
    }

    public AttributeEntry getEntry() {
        return entry;
    }



}
