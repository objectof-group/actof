package net.objectof.actof.schemaspy.controller.cards.schemaentry;


import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.ISchemaEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.model.Stereotype;


public class ChildEntryCard extends ChildEntryBase {

    public ChildEntryCard(TreeTableView<SchemaEntry> tree, TreeItem<SchemaEntry> treeitem) {
        super(tree, treeitem);

        ISchemaEntry entry = (ISchemaEntry) treeitem.getValue();
        List<AttributeEntry> attrs = entry.getAttributes();

        titleNode.setStyle("-fx-font-size: 13pt");

        VBox panes = new VBox();

        // show child nodes in grid
        KeyValuePane childPane = new KeyValuePane();
        childPane.setPadding(new Insets(0, 0, 0, 20));
        ColumnConstraints constraints;
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        childPane.getColumnConstraints().add(constraints);

        for (String keyString : entry.getChildren().keySet()) {
            SchemaEntry childEntry = entry.getChild(keyString);
            Stereotype st = childEntry.getStereotype();
            String valueString = RepoUtils.prettyPrint(st);

            if (st == Stereotype.REF) {
                AttributeEntry href = childEntry.getAttribute("m:href");
                attrs.remove(href);
                valueString += " to " + href.getValue();
            }
            childPane.put(keyString, valueString);
        }

        if (childPane.keySet().size() > 0) {
            Label childLabel = new Label("Children");
            childLabel.setPadding(new Insets(5, 0, 5, 1));
            childLabel.styleProperty().bind(childPane.valueStyleProperty());
            panes.getChildren().add(childLabel);
            panes.getChildren().add(childPane);
        }

        // show attributes in grid
        KeyValuePane attrPane = new KeyValuePane();
        attrPane.setPadding(new Insets(0, 0, 0, 20));
        constraints = new ColumnConstraints();
        constraints.setMinWidth(100);
        attrPane.getColumnConstraints().add(constraints);

        for (AttributeEntry attr : attrs) {
            String keyString = attr.getQualifiedName();
            String valueString = attr.getValue();
            attrPane.put(keyString, valueString);
        }

        if (attrPane.keySet().size() > 0) {
            Label attrLabel = new Label("Attributes");
            attrLabel.setPadding(new Insets(5, 0, 5, 1));
            attrLabel.styleProperty().bind(attrPane.valueStyleProperty());
            panes.getChildren().add(attrLabel);
            panes.getChildren().add(attrPane);
        }

        // show this content if there are ANY entries
        if (attrPane.keySet().size() > 0 || childPane.keySet().size() > 0) {
            setContent(panes);
        }

    }

}
