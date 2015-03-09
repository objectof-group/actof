package net.objectof.actof.repospy.controllers.navigator.kind;


import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.common.controller.DynamicTreeItem;
import net.objectof.actof.common.util.AlphaNumericComparitor;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Resource;


public class KindTreeItem extends DynamicTreeItem<TreeNode> {

    RepoSpyController controller;

    public KindTreeItem(Resource<?> res, RepoSpyController controller) {
        this(new IResourceNode(res), controller);
    }

    public KindTreeItem(String entityKind, RepoSpyController controller) {
        this(new IEntityNode(entityKind), controller);
    }

    public KindTreeItem(TreeNode kv, RepoSpyController controller) {
        super(kv);
        this.controller = controller;
    }

    @Override
    protected ObservableList<TreeItem<TreeNode>> buildChildren(TreeItem<TreeNode> treeItem) {

        TreeNode data = treeItem.getValue();
        if (!data.hasChildren()) { return FXCollections.emptyObservableList(); }

        List<KindTreeItem> childEntries = data.getChildren(controller);
        ObservableList<TreeItem<TreeNode>> newlist = FXCollections.observableArrayList(childEntries);

        // sort them all
        AlphaNumericComparitor comparitor = new AlphaNumericComparitor();
        newlist.sort((a, b) -> comparitor.compare(RepoUtils.resToString(a), RepoUtils.resToString(b)));

        return newlist;

    }

    @Override
    public boolean isLeafNode(TreeNode t) {
        return !t.hasChildren();
    }

}