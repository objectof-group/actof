package net.objectof.actof.common.controller.schema.schemaentry;


import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.changes.AttributeCreationChange;
import net.objectof.actof.common.controller.schema.changes.SchemaStereotypeChange;
import net.objectof.model.Stereotype;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ISchemaEntry extends AbstractSchemaEntry {

    private SchemaEntry parent;

    ISchemaEntry(ChangeController changes, SchemaEntry parent, Element element, Document document) {
        super(changes, element, document);
        this.parent = parent;
    }

    @Override
    public boolean isRoot() {
        return false;
    }


    @Override
    public String getPath() {
        return parent.getFullname();
    }

    @Override
    public String getName() {
        return element.getAttribute("selector");
    }

    @Override
    public void setName(String name) {
        element.setAttribute("selector", name);
    }

    @Override
    public Stereotype getStereotype() {
        return Stereotype.valueOf(element.getNodeName().toUpperCase());
    }

    @Override
    public void setStereotype(Stereotype stereotype) {
        document.renameNode(element, null, stereotype.name().toLowerCase());
        getChangeBus().broadcast(new SchemaStereotypeChange(this));
        // if this is not a ref, remove any ref-specific attributes
        if (stereotype != Stereotype.REF) {
            removeAttribute("m:href");
        }
    }

    @Override
    public String getFullname() {
        if (parent.isRoot()) {
            return getName();
        } else {
            return parent.getFullname() + getName();
        }
    }


    @Override
    public AttributeEntry addAttribute(String name, String value) {

        if (element.getAttributeNode(name) != null) { return null; }

        element.setAttribute(name, value);
        Attr attr = element.getAttributeNode(name);
        AttributeEntry attrEntry = new AttributeEntry(getChangeBus(), attr, this, document);
        attributes.add(attrEntry);
        getChangeBus().broadcast(new AttributeCreationChange(attrEntry));
        return attrEntry;
    }

    @Override
    public SchemaEntry getParent() {
        return parent;
    }


}
