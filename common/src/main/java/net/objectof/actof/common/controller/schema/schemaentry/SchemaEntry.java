package net.objectof.actof.common.controller.schema.schemaentry;

import java.util.List;
import java.util.Map;

import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.model.Stereotype;

import org.w3c.dom.Element;

public interface SchemaEntry {

	boolean isRoot();

	String getPath();

	String getName();

	void setName(String name);

	Stereotype getStereotype();

	void setStereotype(Stereotype stereotype);

	String getFullname();

	Element getElement();

	Map<String, SchemaEntry> getChildren();

	SchemaEntry getChild(String path);

	List<AttributeEntry> getAttributes();

	AttributeEntry getAttribute(String name);

	void removeAttribute(String name);

	void removeAttribute(AttributeEntry attribute);

	/**
	 * Creates a new attribute. Returns null if creation failed
	 */
	AttributeEntry addAttribute(String name, String value);
	
	void removeChild(SchemaEntry child);
	
	SchemaEntry addChild(String name);
	
	SchemaEntry getParent();

}