package net.objectof.actof.schemaspy;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.ParserConfigurationException;

import net.objectof.actof.schemaspy.controller.SchemaSpyController;

import org.xml.sax.SAXException;

public class SchemaSpy extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException, SAXException, ParserConfigurationException, XMLParseException {
		new SchemaSpyController(primaryStage).initUI();
	}
	
	public static void main(String[] args) {
		System.setProperty("prism.lcdtext", "false");
		launch(args);
	}

}
