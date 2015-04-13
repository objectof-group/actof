package net.objectof.actof.schemaspy.controller.cards.schemaentry;


import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import net.objectof.actof.common.controller.schema.schemaentry.SchemaEntry;


public class ChildEntryLine extends ChildEntryBase {

    public ChildEntryLine(TreeTableView<SchemaEntry> tree, TreeItem<SchemaEntry> treeitem) {
        super(tree, treeitem);

        setHasShadow(false);
        setPadding(new Insets(0, 10, 0, 10));
        setColour("transparent");
        setInnerPadding(new Insets(0));

        Separator sep = getSeparator();
        sep.setVisible(true);
        sep.getStyleClass().add("entry-separator");

        rem.setVisible(false);
        setOnMouseEntered(event -> rem.setVisible(true));
        setOnMouseExited(event -> rem.setVisible(false));

    }
}
