package net.objectof.actof.schemaspy.controller.cards;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.schemaspy.SchemaSpyController;


public abstract class Card {

    private BorderPane top, content;

    public Card() {}

    public Node getUI() {

        if (top != null) { return top; }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CardContainer.fxml"));
        try {

            top = (BorderPane) loader.load();
            content = (BorderPane) loader.getNamespace().get("content");

            Label title = (Label) loader.getNamespace().get("title");
            title.setText(getName());

            Node center = getNode();
            BorderPane.setAlignment(center, Pos.CENTER_LEFT);
            content.setCenter(center);
            content.setStyle("-fx-background-color: #ffffff; -fx-effect: dropshadow(gaussian, #777, 8, -2, 0, 1)");

            return top;

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static List<Card> allCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new StereotypeCard());
        cards.add(new ReferenceCard());
        cards.add(new AttributesCard());
        return cards;
    }

    public abstract List<AttributeEntry> attributes(List<AttributeEntry> unhandled);

    public abstract boolean appliesTo(SchemaEntry schemaEntry, List<AttributeEntry> unhandled);

    public abstract void init(SchemaSpyController schemaspy, SchemaEntry entry, List<AttributeEntry> unhandled);

    protected abstract String getName();

    protected abstract Node getNode();

}
