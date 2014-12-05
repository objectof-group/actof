package net.objectof.actof.common.controller.schema;


import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.objectof.actof.common.controller.IActofController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.schema.changes.SchemaReplacedChange;
import net.objectof.actof.common.controller.schema.schemaentry.RootSchemaEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ISchemaController extends IActofController implements SchemaController {

    Map<String, String> namespaces = new HashMap<>();
    private final String XMLNS = "xmlns:";

    private Document document;
    private SchemaEntry rootEntry;

    public ISchemaController(ChangeController changes) throws SAXException, IOException, ParserConfigurationException,
            XMLParseException {
        super(changes);
        setModel(ISchemaController.getBlankSchema());

    }

    public ISchemaController(ChangeController changes, InputStream aStream) throws SAXException, IOException,
            ParserConfigurationException, XMLParseException {
        super(changes);
        setModel(aStream);
    }

    public ISchemaController(ChangeController changes, Document aDocument) throws XMLParseException {
        super(changes);
        setModel(aDocument);
    }


    private void setModel(InputStream aStream) throws XMLParseException, SAXException, IOException,
            ParserConfigurationException {
        setModel(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(aStream));
    }

    private void setModel(Document doc) throws XMLParseException {
        this.document = doc;
        rootEntry = new RootSchemaEntry(getChangeBus(), getModelNode(), document);
        populateNamespaces();
        getChangeBus().broadcast(new SchemaReplacedChange());
    }



    @Override
    public SchemaEntry getRoot() {
        return rootEntry;
    }

    @Override
    public SchemaEntry getEntry(String path) {
        return rootEntry.getChild(path);
    }

    @Override
    public Collection<SchemaEntry> getEntities() {
        return rootEntry.getChildren().values();
    }

    @Override
    public Map<String, String> getNamespaces() {
        return namespaces;
    }


    @Override
    public String toXML() throws TransformerFactoryConfigurationError, TransformerException {

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();

    }




    void renameElement(Element e, String name) {
        document.renameNode(e, null, name);
    }

    private Element getSchemaNode() {
        return document.getDocumentElement();
    }

    private Element getModelNode() throws XMLParseException {
        NodeList list = getSchemaNode().getElementsByTagName("model");
        if (list.getLength() != 1) { throw new XMLParseException("There must be exactly one <model> tag"); }
        return (Element) list.item(0);
    }

    private void populateNamespaces() {

        NamedNodeMap attrMap = getSchemaNode().getAttributes();
        for (int i = 0; i < attrMap.getLength(); i++) {
            Node node = attrMap.item(i);
            if (!(node instanceof Attr)) {
                continue;
            }

            Attr attr = (Attr) node;
            String name = attr.getName();
            if (!name.startsWith(XMLNS)) {
                continue;
            }

            name = name.substring(XMLNS.length());
            namespaces.put(name, attr.getValue());
        }

    }

    @Override
    public Document getDocument() {
        return document;
    }

    private static InputStream getBlankSchema() {
        return ISchemaController.class.getResourceAsStream("blank.xml");
    }

    @Override
    public String getPackage() {
        return getSchemaNode().getAttribute("id");
    }

    @Override
    public String getPackageDomain() {
        return getPackage().split(":", 2)[0];
    }

    private String getPackageDetails() {
        return getPackage().split(":", 2)[1];
    }

    @Override
    public String getPackageVersion() {
        return getPackageDetails().split("/", 2)[0];
    }

    @Override
    public String getPackagePath() {
        return "/" + getPackageDetails().split("/", 2)[1];
    }

    private void setPackageName(String domain, String version, String path) {
        String name = domain + ":" + version + path;
        getSchemaNode().setAttribute("id", name);
    }


    @Override
    public void setPackageDomain(String domain) {
        setPackageName(domain, getPackageVersion(), getPackagePath());
    }

    @Override
    public void setPackageVersion(String version) {
        setPackageName(getPackageDomain(), version, getPackagePath());
    }

    @Override
    public void setPackagePath(String path) {
        setPackageName(getPackageDomain(), getPackageVersion(), path);
    }

}
