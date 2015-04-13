package net.objectof.actof.schemaspy.controller.cards.attributes;


import java.util.ArrayList;
import java.util.List;

import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.schemaspy.SchemaSpyController;
import net.objectof.actof.widgets.card.Card;


public abstract class SchemaSpyCard extends Card {

    private SchemaEntry schemaEntry;

    public static List<SchemaSpyCard> allCards() {
        List<SchemaSpyCard> cards = new ArrayList<>();
        cards.add(new StereotypeCard());
        cards.add(new ReferenceCard());
        cards.add(new AttributesCard());
        return cards;
    }

    public abstract List<AttributeEntry> attributes(List<AttributeEntry> unhandled);

    public abstract boolean appliesTo(SchemaEntry schemaEntry, List<AttributeEntry> unhandled);

    public void initialize(SchemaSpyController schemaspy, SchemaEntry entry, List<AttributeEntry> unhandled) {
        schemaEntry = entry;
        init(schemaspy, unhandled);
        setTitle(getName());
    }

    protected abstract void init(SchemaSpyController schemaspy, List<AttributeEntry> unhandled);

    protected abstract String getName();

    protected SchemaEntry getSchemaEntry() {
        return schemaEntry;
    }

}
