package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.leaf.LeafCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;


public class CompositeView extends AbstractView {

    private IAggregateNode entry;

    public CompositeView(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy);
        this.entry = (IAggregateNode) treeitem.getValue();
        updateUI();
    }

    @Override
    protected void updateUI() {

        cards.getChildren().clear();

        for (ILeafNode leaf : entry.getLeaves(repospy)) {
            LeafCard editor = LeafCard.createEditor(leaf);
            cards.getChildren().add(editor);
        }
    }
}
