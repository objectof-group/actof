package net.objectof.actof.schemaspy.controller.cards;


import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.ISchemaEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
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

        Node desc = getDescription();
        AnchorPane.setTopAnchor(desc, 0d);
        AnchorPane.setBottomAnchor(desc, 0d);
        AnchorPane.setLeftAnchor(desc, 0d);
        AnchorPane.setRightAnchor(desc, 0d);
        desc = new AnchorPane(desc);
        Button rem = new Button("", ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
        rem.setOnAction(event -> {
            treeitem.getParent().getValue().removeChild(entry);
        });
        rem.getStyleClass().add("tool-bar-button");
        HBox box = new HBox(10, desc, rem);
        setDescription(box);

        Hyperlink node = new Hyperlink(entry.getName());
        node.setPadding(new Insets(0));
        node.setStyle("-fx-font-size: 13pt");
        node.setOnAction(event -> {
            tree.getSelectionModel().select(treeitem);
        });
        setTitle(node);

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
