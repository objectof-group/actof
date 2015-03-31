package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.card.CardsPane.Layout;


public class CompositeLayout extends AbstractLayout {

    private IAggregateNode entry;

    public CompositeLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy, 400);
        cards.setLayout(Layout.ROUND_ROBIN);
        this.entry = (IAggregateNode) treeitem.getValue();
        updateUI();
    }

    @Override
    protected void updateUI() {

        cards.getChildren().clear();

        for (ILeafNode leaf : entry.getLeaves()) {
            LeafCard editor = LeafCard.createEditor(leaf);
            // editor.setColour("derive(-fx-background, -3%)");
            editor.setHasShadow(true);
            editor.setShadowRadius(3);
            editor.setShadowColour("derive(-fx-background, -15%)");
            // editor.setColour("#ffffff");
            editor.setColour("linear-gradient(from 0px 0px to 0px 8px, derive(-fx-background, 50%), -fx-background)");
            cards.getChildren().add(editor);
        }
    }
}
