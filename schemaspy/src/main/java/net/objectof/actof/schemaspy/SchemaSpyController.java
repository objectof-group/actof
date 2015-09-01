package net.objectof.actof.schemaspy;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.editor.impl.AbstractEditor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.component.resource.impl.IAction;
import net.objectof.actof.common.controller.schema.ISchemaController;
import net.objectof.actof.common.controller.schema.SchemaController;
import net.objectof.actof.common.controller.schema.changes.SchemaReplacedChange;
import net.objectof.actof.common.window.ActofWindow;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.schemaspy.controller.schemaview.SchemaViewController;
import net.objectof.actof.schemaspy.resource.SchemaFileResource;
import net.objectof.connector.Connector;


public class SchemaSpyController extends AbstractEditor implements ResourceEditor {

    SchemaViewController view;
    private SchemaController schema;

    private boolean forResource = false;
    private SchemaFileResource resource;

    private Action createJar = new IAction("Build Jar File", () -> view.onGenerate());
    private Action createRepo = new IAction("Create Repository", () -> view.onCreate());

    public SchemaSpyController() throws IOException, SAXException, ParserConfigurationException, XMLParseException

    {

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

        SchemaViewController controller = SchemaViewController.load();
        controller.setChangeBus(getChangeBus());
        controller.setTopController(this);
        controller.setDisplayStage(getDisplayStage());
        controller.construct();

        return controller;
    }

    public Connector showConnect() throws IOException {
        return ConnectionController.showDialog(getDisplayStage(), true);
    }

    public SchemaController getSchema() {
        return schema;
    }

    @Override
    public String getTitle() {
        return "SchemaSpy";
    }

    @Override
    public void construct() throws Exception {
        newSchema();
        view = showSchemaView();

        createJar.setEnabled(false);
        createRepo.setEnabled(false);
        getActions().add(createJar);
        getActions().add(createRepo);

        getChangeBus().listen(SchemaReplacedChange.class, () -> {
            createJar.setEnabled(true);
            createRepo.setEnabled(true);
        });

    }

    @Override
    public SchemaFileResource getResource() {
        return resource;
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = (SchemaFileResource) resource;
    }

    @Override
    public void loadResource() throws Exception {
        setSchema(resource.getSchemaFile());
    }

    @Override
    public boolean isForResource() {
        return forResource;
    }

    @Override
    public void setForResource(boolean forResource) {
        this.forResource = forResource;
    }

    @Override
    public Display getDisplay() {
        return view;
    }

    public static File chooseSchemaFile(File lastschemadir, Stage parent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Schema File");
        if (lastschemadir != null) {
            chooser.setInitialDirectory(lastschemadir);
        }
        ExtensionFilter filter = new ExtensionFilter("Schema Files", "*.xml");
        chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showOpenDialog(parent);
        if (file == null) { return null; }
        return file;

    }

    @Override
    protected void onResourceAdded(Resource res) throws Exception {

        ResourceEditor editor = res.getEditor();
        editor.setForResource(true);
        editor.construct();

        editor.setResource(res);
        editor.loadResource();

        ActofWindow window = new ActofWindow(editor);
        window.show();

    }

}
