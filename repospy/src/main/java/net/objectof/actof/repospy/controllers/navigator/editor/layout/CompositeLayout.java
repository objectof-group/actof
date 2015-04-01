package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.masonry.MasonryPane;


public class CompositeLayout extends AbstractLayout {

    private IAggregateNode entry;

    public CompositeLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy, 400);
        cards.setLayout(MasonryPane.LAYOUT_ROUND_ROBIN);
        this.entry = (IAggregateNode) treeitem.getValue();
        updateUI();
    }

    @Override
    protected void updateUI() {

        cards.getChildren().clear();

        for (ILeafNode leaf : entry.getLeaves()) {
            LeafCard editor = LeafCard.createEditor(leaf);
            cards.getChildren().add(editor);
        }
    }
}
