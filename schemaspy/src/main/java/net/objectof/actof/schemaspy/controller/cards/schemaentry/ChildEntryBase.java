package net.objectof.actof.schemaspy.controller.cards.schemaentry;


import java.net.URL;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.objectof.actof.common.controller.schema.AttributeEntry;
import net.objectof.actof.common.controller.schema.schemaentry.ISchemaEntry;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.icons.ActofIcons.Icon;
import net.objectof.actof.common.icons.ActofIcons.Size;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.widgets.card.Card;
import net.objectof.model.Stereotype;


public class ChildEntryBase extends Card {

    protected Hyperlink titleNode;
    protected Button rem;
    protected HBox descriptionBox;

    public ChildEntryBase(TreeTableView<SchemaEntry> tree, TreeItem<SchemaEntry> treeitem) {

        URL css = ChildEntryBase.class.getResource("style.css");
        this.getStylesheets().add(css.toString());

        ISchemaEntry entry = (ISchemaEntry) treeitem.getValue();
        List<AttributeEntry> attrs = entry.getAttributes();

        setInnerPadding(new Insets(12));

        if (entry.getStereotype() == Stereotype.REF) {
            String st = RepoUtils.prettyPrint(entry.getStereotype());
            AttributeEntry href = entry.getAttribute("m:href");
            if (href != null) {
                attrs.remove(href);
                setDescription(st + " to " + href.getValue());
            } else {
                setDescription(st);
            }
        } else {
            setDescription(RepoUtils.prettyPrint(entry.getStereotype()));
        }

        Node desc = getDescription();
        AnchorPane.setTopAnchor(desc, 0d);
        AnchorPane.setBottomAnchor(desc, 0d);
        AnchorPane.setLeftAnchor(desc, 0d);
        AnchorPane.setRightAnchor(desc, 0d);
        desc = new AnchorPane(desc);
        rem = new Button("", ActofIcons.getIconView(Icon.REMOVE, Size.BUTTON));
        rem.setOnAction(event -> {
            treeitem.getParent().getValue().removeChild(entry);
        });
        rem.getStyleClass().add("tool-bar-button");
        descriptionBox = new HBox(2, desc, rem);
        descriptionBox.setPadding(new Insets(0, 0, 0, 10));
        setDescription(descriptionBox);

        titleNode = new Hyperlink(entry.getName());
        titleNode.setPadding(new Insets(0));
        titleNode.setOnAction(event -> {
            tree.getSelectionModel().select(treeitem);
        });
        setTitle(titleNode);

    }
}
