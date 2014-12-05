package net.objectof.actof.common.controller.schema;


import net.objectof.actof.common.controller.IActofController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.schema.changes.AttributeValueChange;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;


public class AttributeEntry extends IActofController {

    private Attr attribute;
    private Document doc;
    private SchemaEntry schemaEntry;

    public AttributeEntry(ChangeController changes, Attr attr, SchemaEntry schemaEntry, Document doc) {
        super(changes);
        this.attribute = attr;
        this.doc = doc;
        this.schemaEntry = schemaEntry;
    }

    @Override
    public String toString() {
        return attribute.toString();
    }

    public SchemaEntry getSchemaEntry() {
        return schemaEntry;
    }

    public String getQualifiedName() {
        return attribute.getNodeName();
    }

    public String getName() {
        String name = attribute.getNodeName();
        if (!name.contains(":")) { return name; }
        String[] parts = name.split(":", 2);
        return parts[1];
    }

    public void setName(String name) {
        setNodeName(getNamespace(), name);
    }

    public String getNamespace() {
        String name = attribute.getNodeName();
        if (!name.contains(":")) { return null; }
        String[] parts = name.split(":", 2);
        return parts[0];
    }

    public void setNamespace(String namespace) {
        setNodeName(namespace, getName());
    }

    private void setNodeName(String namespace, String name) {
        String newname = "";
        if (namespace != null) {
            newname += namespace + ":";
        }
        newname += name;

        doc.renameNode(attribute, null, newname);
    }


    public String getValue() {
        return attribute.getNodeValue();
    }

    public void setValue(String value) {
        attribute.setNodeValue(value);
        getChangeBus().broadcast(new AttributeValueChange(this));
    }

    public Attr getAttr() {
        return attribute;
    }

}
