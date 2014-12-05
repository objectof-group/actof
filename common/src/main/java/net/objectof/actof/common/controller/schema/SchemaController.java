package net.objectof.actof.common.controller.schema;

import java.util.Collection;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;

import org.w3c.dom.Document;

public interface SchemaController {

	SchemaEntry getRoot();

	SchemaEntry getEntry(String path);

	Document getDocument();
	
	Collection<SchemaEntry> getEntities();

	Map<String, String> getNamespaces();
	
	String toXML() throws TransformerFactoryConfigurationError, TransformerException;
	

	void setPackagePath(String text);

	void setPackageDomain(String domain);

	void setPackageVersion(String version);

	
	String getPackageDomain();
	
	String getPackageVersion();
	
	String getPackagePath();

	String getPackage();
	
}