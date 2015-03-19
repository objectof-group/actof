package net.objectof.actof.repospy.controllers.navigator.treemodel;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.common.controller.DynamicTreeItem;
import net.objectof.actof.common.util.AlphaNumericComparitor;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.IAggregateNode;


public class RepoSpyTreeItem extends DynamicTreeItem<TreeNode> {

    RepoSpyController controller;

    public RepoSpyTreeItem(TreeNode kv, RepoSpyController controller) {
        super(kv);
        this.controller = controller;
        if (kv instanceof IAggregateNode) {
            getChildren();
        }
    }

    @Override
    protected ObservableList<TreeItem<TreeNode>> createChildList(TreeItem<TreeNode> treeItem) {

        TreeNode data = treeItem.getValue();
        if (!data.hasChildren()) { return FXCollections.emptyObservableList(); }

        ObservableList<TreeItem<TreeNode>> childEntries = data.getChildren();

        // sort them all
        AlphaNumericComparitor comparitor = new AlphaNumericComparitor();
        childEntries.sort((a, b) -> comparitor.compare(RepoUtils.resToString(a), RepoUtils.resToString(b)));

        return childEntries;

    }

    @Override
    public boolean isLeafNode(TreeNode t) {
        return !t.hasChildren();
    }

}