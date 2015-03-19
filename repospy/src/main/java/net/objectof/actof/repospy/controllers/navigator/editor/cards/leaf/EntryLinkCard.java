package net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf;


import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class EntryLinkCard extends LeafCard {

    public EntryLinkCard(ILeafNode entry) {
        super(entry);
        Hyperlink node = new Hyperlink(getLeafTitle());
        node.setPadding(new Insets(0));
        node.setStyle("-fx-font-size: 13pt");
        entry.getTreeNode().getChildren();
        node.setOnAction(event -> {
            entry.getController().getChangeBus().broadcast(new ResourceSelectedChange(entry.getTreeNode()));
        });
        setTitle(node);

    }

}
