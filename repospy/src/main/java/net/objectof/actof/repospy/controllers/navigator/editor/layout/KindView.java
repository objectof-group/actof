package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.TreeItem;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.editor.cards.EntityCard;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IKindNode;


public class KindView extends AbstractView {

    private IKindNode node;

    public KindView(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy);
        this.node = (IKindNode) treeitem.getValue();
        this.treeitem = treeitem;
        System.out.println(treeitem.getChildren());
        updateUI();
    }

    @Override
    protected void updateUI() {

        cards.getChildren().clear();

        for (TreeItem<TreeNode> child : treeitem.getChildren()) {
            IAggregateNode aggChild = (IAggregateNode) (child.getValue());
            EntityCard card = new EntityCard((RepoSpyTreeItem) child, repospy);
            cards.getChildren().add(card);
        }
    }
}
