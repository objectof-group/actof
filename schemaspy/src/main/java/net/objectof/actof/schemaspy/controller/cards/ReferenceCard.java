package net.objectof.actof.schemaspy.controller.cards;


import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.schemaspy.SchemaSpyController;
import net.objectof.model.Stereotype;


public class ReferenceCard extends SchemaSpyCard {

    ChoiceBox<String> choice;

    @Override
    public void init(SchemaSpyController schemaspy, List<AttributeEntry> unhandled) {

        choice = new ChoiceBox<>();

        for (SchemaEntry entity : schemaspy.getSchema().getEntities()) {
            choice.getItems().add(entity.getName());
        }

        AttributeEntry attr = getSchemaEntry().getAttribute("m:href");
        if (attr == null) {
            choice.getItems().add(0, "");
            choice.getSelectionModel().select(0);
        } else {
            choice.getSelectionModel().select(attr.getValue());
        }

        choice.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (attr == null) {
                getSchemaEntry().addAttribute("m:href", n);
            } else {
                attr.setValue(n);
            }
        });

    }

    @Override
    public List<AttributeEntry> attributes(List<AttributeEntry> unhandled) {
        List<AttributeEntry> attrs = new ArrayList<>();
        attrs.add(getSchemaEntry().getAttribute("m:href"));
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
