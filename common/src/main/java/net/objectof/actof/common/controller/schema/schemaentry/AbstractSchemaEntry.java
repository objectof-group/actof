package net.objectof.actof.common.controller.schema.schemaentry;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.objectof.actof.common.controller.IActofController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.changes.AttributeRemovalChange;
import net.objectof.actof.common.controller.schema.changes.SchemaInsertChange;
import net.objectof.actof.common.controller.schema.changes.SchemaRemovalChange;
import net.objectof.model.Stereotype;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class AbstractSchemaEntry extends IActofController implements SchemaEntry {

    protected Element element;
    protected Document document;
    protected Map<String, SchemaEntry> children;
    protected List<AttributeEntry> attributes;

    public AbstractSchemaEntry(ChangeController changes, Element element, Document document) {
        super(changes);
        this.element = element;
        this.document = document;
        buildData();
    }

    @Override
    public Element getElement() {
        return element;
    }

    @Override
    public SchemaEntry getChild(String path) {
        if (path.contains(".")) {
            List<String> parts = Arrays.asList(path.split("\\."));
            if (parts.size() == 0) { return null; }
            String first = parts.remove(0);
            return children.get(first).getChild(String.join(".", parts));
        } else {
            if (!children.containsKey(path)) { return null; }
            return children.get(path);
        }
    }


    @Override
    public Map<String, SchemaEntry> getChildren() {
        return new HashMap<>(children);
    }



    @Override
    public List<AttributeEntry> getAttributes() {
        return new ArrayList<>(attributes);
    }

    @Override
    public AttributeEntry getAttribute(String name) {
        for (AttributeEntry entry : getAttributes()) {
            if (name.equals(entry.getQualifiedName())) { return entry; }
        }
        return null;
    }


    @Override
    public void removeAttribute(String name) {
        removeAttribute(getAttribute(name));
    }

    @Override
    public void removeAttribute(AttributeEntry attribute) {
        if (attribute == null) { return; }
        // element.getAttributeNode(attribute)
        element.removeAttributeNode(attribute.getAttr());
        attributes.remove(attribute);
        getChangeBus().broadcast(new AttributeRemovalChange(attribute));
    }


    @Override
    public void removeChild(SchemaEntry child) {
        if (!children.containsValue(child)) { return; }
        children.remove(child.getName());
        element.removeChild(child.getElement());
        getChangeBus().broadcast(new SchemaRemovalChange(child));
    }

    @Override
    public SchemaEntry addChild(String name) {

        // modify dom
        Element childElement = document.createElement(Stereotype.COMPOSED.toString().toLowerCase());
        getElement().appendChild(childElement);

        // modify our data structure
        SchemaEntry child = new ISchemaEntry(getChangeBus(), this, childElement, document);
        child.setName(name);
        children.put(child.getName(), child);

        getChangeBus().broadcast(new SchemaInsertChange(child));
        return child;
    }


    protected void buildData() {
        children = new HashMap<>();
        for (Element e : childElements()) {
            ISchemaEntry entry = new ISchemaEntry(getChangeBus(), this, e, document);
            children.put(entry.getName(), entry);
        }

        attributes = new ArrayList<>();
        for (Node node : getAttributeNodes()) {
            AttributeEntry attrEntry = new AttributeEntry(getChangeBus(), (Attr) node, this, document);
            if (attrEntry.getNamespace() != null) {
                attributes.add(attrEntry);
            }
        }
    }



    private List<Element> childElements() {
        NodeList nodelist = element.getChildNodes();
        List<Element> nodes = new ArrayList<>();
        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            if (node instanceof Element) {
                nodes.add((Element) node);
            }
        }
        return nodes;
    }

    private List<Node> getAttributeNodes() {
        NamedNodeMap map = element.getAttributes();
        List<Node> attributes = new ArrayList<>();
        for (int i = 0; i < map.getLength(); i++) {
            attributes.add(map.item(i));
        }
        return attributes;
    }
}
