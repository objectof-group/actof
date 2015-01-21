package net.objectof.actof.schemaspy.controller.cards;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.changes.AttributeValueChange;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.schemaspy.SchemaSpyController;
import net.objectof.model.Stereotype;

public class ReferenceCard extends Card {


	ChoiceBox<String> choice;
	SchemaEntry entry;
	

	@Override
	public void init(SchemaSpyController schemaspy, SchemaEntry entry, List<AttributeEntry> unhandled) {

		this.entry = entry;
		choice = new ChoiceBox<>();
		
		for (SchemaEntry entity : schemaspy.getSchema().getEntities()) {
			choice.getItems().add(entity.getName());
		}
		
		AttributeEntry attr = entry.getAttribute("m:href");
		if (attr == null) {
			choice.getItems().add(0, "");
			choice.getSelectionModel().select(0);
		} else {
			choice.getSelectionModel().select(attr.getValue());
		}
		
		choice.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
			if (attr == null) {
				entry.addAttribute("m:href", n);
			} else {
				attr.setValue(n);
			}
		});
		
	}

	@Override
	public List<AttributeEntry> attributes(List<AttributeEntry> unhandled) {
		List<AttributeEntry> attrs = new ArrayList<>();
		attrs.add(entry.getAttribute("m:href"));
		return attrs;
	}

	@Override
	public boolean appliesTo(SchemaEntry schemaEntry, List<AttributeEntry> unhandled) {
		return schemaEntry.getStereotype() == Stereotype.REF;
	}

	@Override
	public Node getNode() {
		return choice;
	}

	@Override
	protected String getName() {
		return "Reference Target";
	}
	

}
