package net.objectof.actof.repospy.controllers.navigator.editor.cards;


import javafx.scene.control.Hyperlink;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.ILeafNode;


public class EntryLinkCard extends LeafCard {

    public EntryLinkCard(ILeafNode entry, boolean capitalize) {
        super(entry, capitalize);
        Hyperlink node = new Hyperlink(getLeafTitle(capitalize));
        node.setStyle("-fx-font-size: 13pt");

        node.setOnAction(event -> {
            entry.getController().getChangeBus().broadcast(new ResourceSelectedChange(entry.treeNode));
        });

        setTitle(node);

    }

}
