package net.objectof.actof.schemaspy.controller.cards.attributes;


import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ChoiceBox;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.schemaspy.SchemaSpyController;
import net.objectof.actof.schemaspy.controller.cards.schemaentry.StereotypeChooser;
import net.objectof.model.Stereotype;


public class StereotypeCard extends SchemaSpyCard {

    ChoiceBox<Stereotype> choice;

    @Override
    public List<AttributeEntry> attributes(List<AttributeEntry> unhandled) {
        return new ArrayList<>();
    }

    @Override
    public boolean appliesTo(SchemaEntry schemaEntry, List<AttributeEntry> unhandled) {
        return true;
    }

    @Override
    public void init(SchemaSpyController schemaspy, List<AttributeEntry> unhandled) {
        setTitleContent(new StereotypeChooser(getSchemaEntry()));
    }

    @Override
    protected String getName() {
        return "Stereotype";
    }

    protected boolean inline() {
        return true;
    }

}
