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


public class KindTreeItem extends DynamicTreeItem<RepoTreeEntry> {

    RepoSpyController controller;

    public KindTreeItem(Resource<?> res, RepoSpyController controller) {
        this(new ResourceTreeEntry(res), controller);
    }

    public KindTreeItem(String entityKind, RepoSpyController controller) {
        this(new KindTreeEntry(entityKind), controller);
    }

    public KindTreeItem(RepoTreeEntry kv, RepoSpyController controller) {
        super(kv);
        this.controller = controller;
    }

    @Override
    protected ObservableList<TreeItem<RepoTreeEntry>> buildChildren(TreeItem<RepoTreeEntry> treeItem) {

        RepoTreeEntry data = treeItem.getValue();
        if (!data.hasChildren()) { return FXCollections.emptyObservableList(); }

        List<RepoTreeEntry> childEntries = data.getChildren(controller.repository, controller.search);
        ObservableList<TreeItem<RepoTreeEntry>> newlist = FXCollections.observableArrayList();
        for (RepoTreeEntry child : childEntries) {
            newlist.add(new KindTreeItem(child, controller));
        }

        // sort them all
        AlphaNumericComparitor comparitor = new AlphaNumericComparitor();
        newlist.sort((a, b) -> comparitor.compare(RepoUtils.resToString(a), RepoUtils.resToString(b)));

        return newlist;

    }

    @Override
    public boolean isLeafNode(RepoTreeEntry t) {
        return !t.hasChildren();
    }

}