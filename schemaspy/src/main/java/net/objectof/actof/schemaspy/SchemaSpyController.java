package net.objectof.actof.schemaspy;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.scene.Node;
import net.objectof.actof.common.component.AbstractDisplay;
import net.objectof.actof.common.controller.schema.ISchemaController;
import net.objectof.actof.common.controller.schema.SchemaController;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.schemaspy.controller.schemaview.SchemaViewController;
import net.objectof.connector.Connector;


public class SchemaSpyController extends AbstractDisplay {

    SchemaViewController view;
    private SchemaController schema;

    public SchemaSpyController() throws IOException, SAXException, ParserConfigurationException, XMLParseException {

    }

    public void newSchema() throws SAXException, IOException, ParserConfigurationException, XMLParseException {
        setSchema(new ISchemaController(getChangeBus()));
    }

    public void setSchema(ISchemaController controller)
            throws SAXException, IOException, ParserConfigurationException, XMLParseException {
        schema = controller;
    }

    public void setSchema(InputStream inputStream)
            throws SAXException, IOException, ParserConfigurationException, XMLParseException {
        setSchema(new ISchemaController(getChangeBus(), inputStream));
    }

    public void setSchema(File file)
            throws FileNotFoundException, SAXException, IOException, ParserConfigurationException, XMLParseException {
        setSchema(new FileInputStream(file));
    }

    public SchemaViewController showSchemaView()
            throws IOException, XMLParseException, SAXException, ParserConfigurationException {

        SchemaViewController controller = SchemaViewController.load(getChangeBus());
        controller.setTopController(this);

        return controller;
    }

    public Connector showConnect() throws IOException {
        return ConnectionController.showDialog(getDisplayStage(), true);
    }

    public SchemaController getSchema() {
        return schema;
    }

    @Override
    public void onShow() {}

    @Override
    public Node getDisplayNode() {
        return view.getNode();
    }

    @Override
    public String getTitle() {
        return "SchemaSpy";
    }

    @Override
    public void initialize() throws Exception {
        newSchema();
        view = showSchemaView();
    }

}
