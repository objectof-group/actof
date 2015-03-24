package net.objectof.actof.schemaspy.controller.cards;


import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.ColumnConstraints;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.ISchemaEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.actof.widgets.card.Card;
import net.objectof.model.Stereotype;


public class ChildEntryCard extends Card {

    public ChildEntryCard(TreeTableView<SchemaEntry> tree, TreeItem<SchemaEntry> treeitem) {

        ISchemaEntry entry = (ISchemaEntry) treeitem.getValue();

        setInnerPadding(new Insets(12));

        List<AttributeEntry> attrs = entry.getAttributes();

        if (entry.getStereotype() == Stereotype.REF) {
            String st = RepoUtils.prettyPrint(entry.getStereotype());
            AttributeEntry href = entry.getAttribute("m:href");
            attrs.remove(href);
            setDescription(st + " to " + href.getValue());
        } else {
            setDescription(RepoUtils.prettyPrint(entry.getStereotype()));
        }

        Hyperlink node = new Hyperlink(entry.getName());
        node.setPadding(new Insets(0));
        node.setStyle("-fx-font-size: 13pt");
        node.setOnAction(event -> {
            tree.getSelectionModel().select(treeitem);
        });
        setTitle(node);

        KeyValuePane fields = new KeyValuePane();
        fields.setPadding(new Insets(0, 0, 0, 20));
        ColumnConstraints constraints;
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        fields.getColumnConstraints().add(constraints);

        for (String keyString : entry.getChildren().keySet()) {
            SchemaEntry childEntry = entry.getChild(keyString);
            Stereotype st = childEntry.getStereotype();
            String valueString = RepoUtils.prettyPrint(st);

            if (st == Stereotype.REF) {
                AttributeEntry href = childEntry.getAttribute("m:href");
                attrs.remove(href);
                valueString += " to " + href.getValue();
            }
            fields.put(keyString, valueString);
        }

        for (AttributeEntry attr : attrs) {
            String keyString = attr.getQualifiedName();
            String valueString = attr.getValue();
            fields.put(keyString, valueString);
        }

        if (fields.keySet().size() > 0) {
            setContent(fields);
        }

    }
}
