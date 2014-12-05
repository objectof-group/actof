package net.objectof.actof.common.controller.schema.changes;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.schema.AttributeEntry;


public class AttributeCreationChange extends Change {

    AttributeEntry attr;

    public AttributeCreationChange(AttributeEntry attr) {
        this.attr = attr;
    }


    public AttributeEntry getAttribute() {
        return attr;
    }

}
