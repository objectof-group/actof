package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class EntryLinkCard extends LeafCard {

    public EntryLinkCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);
        Hyperlink node = new Hyperlink(getLeafTitle(capitalize));
        node.setPadding(new Insets(0));
        node.setStyle("-fx-font-size: 13pt");
        entry.treeNode.getChildren();
        node.setOnAction(event -> {
            entry.getController().getChangeBus().broadcast(new ResourceSelectedChange(entry.treeNode));
        });
        setTitle(node);

    }

}
