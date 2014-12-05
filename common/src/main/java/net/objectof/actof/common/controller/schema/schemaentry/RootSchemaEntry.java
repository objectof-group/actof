package net.objectof.actof.common.controller.schema.schemaentry;

import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.model.Stereotype;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RootSchemaEntry extends AbstractSchemaEntry {



	public RootSchemaEntry(ChangeController changes, Element element, Document document) {
		super(changes, element, document);
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public String getPath() {
		return "";
	}

	@Override
	public String getName() {
		return "Schema";
	}

	@Override
	public void setName(String name) {}

	@Override
	public Stereotype getStereotype() {
		return null;
	}

	@Override
	public void setStereotype(Stereotype stereotype) {}

	@Override
	public String getFullname() {
		return getName();
	}


	@Override
	public AttributeEntry addAttribute(String name, String value) { return null; }

	@Override
	public SchemaEntry getParent() {
		return null;
	}



}
