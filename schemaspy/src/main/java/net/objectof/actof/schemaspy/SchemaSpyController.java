package net.objectof.actof.schemaspy;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.ParserConfigurationException;

import net.objectof.actof.common.controller.ITopController;
import net.objectof.actof.common.controller.schema.ISchemaController;
import net.objectof.actof.common.controller.schema.SchemaController;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.schemaspy.controller.schemaview.SchemaViewController;
import net.objectof.connector.Connector;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.xml.sax.SAXException;


public class SchemaSpyController extends ITopController {

    public Stage primaryStage;
    // private BorderPane rootLayout;

    SchemaViewController view;
    private SchemaController schema;

    public SchemaSpyController(Stage stage) throws IOException, SAXException, ParserConfigurationException,
            XMLParseException {
        primaryStage = stage;
        newSchema();

    }

    public void initUI() throws IOException, XMLParseException, SAXException, ParserConfigurationException {
        primaryStage.setTitle("ObjectOf SchemaSpy");
        view = showSchemaView();
    }

    public void newSchema() throws SAXException, IOException, ParserConfigurationException, XMLParseException {
        setSchema(new ISchemaController(getChangeBus()));
    }

    public void setSchema(ISchemaController controller) throws SAXException, IOException, ParserConfigurationException,
            XMLParseException {
        schema = controller;
    }

    public void setSchema(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException,
            XMLParseException {
        setSchema(new ISchemaController(getChangeBus(), inputStream));
    }

    public void setSchema(File file) throws FileNotFoundException, SAXException, IOException,
            ParserConfigurationException, XMLParseException {
        setSchema(new FileInputStream(file));
    }

    public SchemaViewController showSchemaView() throws IOException, XMLParseException, SAXException,
            ParserConfigurationException {

        SchemaViewController controller = SchemaViewController.load(getChangeBus());
        controller.setTopController(this);

        Scene scene = new Scene((Parent) controller.getNode());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(SchemaSpy.class.getResource("view/icons/SchemaSpy.png").openStream()));
        primaryStage.setOnCloseRequest(event -> {
            if (!view.modified) { return; }

            Action reallyquit = Dialogs.create().title("Exit SchemaSpy")
                    .message("Exit SchemaSpy with unsaved changes?").masthead("You have unsaved changes")
                    .actions(Dialog.ACTION_YES, Dialog.ACTION_NO).showConfirm();

            if (reallyquit != Dialog.ACTION_YES) {
                event.consume();
            }

        });
        primaryStage.show();

        return controller;
    }

    public Connector showConnect() throws IOException {

        return ConnectionController.showDialog(primaryStage, true);

    }

    public SchemaController getSchema() {
        return schema;
    }

}
