package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.TreeItem;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.KindCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.actof.widgets.card.Card;


public class PackageView extends AbstractView {

    public PackageView(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy);
        updateUI();
    }

    @Override
    protected void updateUI() {
        cards.getChildren().clear();
        for (TreeItem<TreeNode> child : treeitem.getChildren()) {
            Card card = new KindCard((RepoSpyTreeItem) child, repospy);
            cards.getChildren().add(card);
        }
    }
}
