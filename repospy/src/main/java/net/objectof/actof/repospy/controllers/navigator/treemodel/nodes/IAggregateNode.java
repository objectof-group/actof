package net.objectof.actof.repospy.controllers.navigator.treemodel.nodes;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;


public class IAggregateNode implements TreeNode {

    private Resource<?> res;

    private List<ILeafNode> leaves;
    private ObservableList<TreeItem<TreeNode>> subresources = FXCollections.observableArrayList();
    private boolean initalized = false;

    public IAggregateNode(Resource<?> res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return RepoUtils.prettyPrint(res);
    }

    public Resource<?> getRes() {
        return res;
    }

    public String getEntityKind() {
        throw new UnsupportedOperationException();
    }

    public boolean hasChildren() {
        for (Kind<?> kind : res.id().kind().getParts()) {
            switch (kind.getStereotype()) {

                case COMPOSED:
                case MAPPED:
                case INDEXED:
                case SET:
                    return true;
                default:
                    break;

            }
        }
        return false;
    }

    @Override
    public ObservableList<TreeItem<TreeNode>> getChildren(RepoSpyController repospy) {

        if (!initalized) {
            getLeafEntries(repospy);
        }

        return subresources;

    }

    public List<ILeafNode> getLeaves(RepoSpyController repospy) {

        if (!initalized) {
            getLeafEntries(repospy);
        }

        return leaves;
    }

    public void refreshLeaves(RepoSpyController repospy) {
        getLeafEntries(repospy);
    }

    @Override
    public Stereotype getStereotype() {
        return res.id().kind().getStereotype();
    }

    private void getLeafEntries(RepoSpyController controller) {
        initalized = true;
        if (this.getStereotype() == Stereotype.COMPOSED) {
            leafEntriesForComposite(this, controller);
        } else {
            leafEntriesForAggregate(this, controller);
        }

    }

    private static void leafEntriesForAggregate(IAggregateNode parent, RepoSpyController controller) {

        @SuppressWarnings("unchecked")
        Aggregate<?, Resource<?>> agg = (Aggregate<?, Resource<?>>) parent.getRes();
        Set<?> keys = agg.keySet();

        Kind<?> kind = parent.getRes().id().kind().getParts().get(0);

        parent.leaves = new ArrayList<>();
        parent.subresources.clear();
        for (Object key : keys) {

            ILeafNode entry = new ILeafNode(parent.getRes().id(), controller, kind, key);
            if (entry.getFieldValue() == null) {
                if (RepoUtils.isAggregateStereotype(entry.getKind().getStereotype())) {
                    entry.createFromNull();
                }
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                RepoSpyTreeItem subentry = new RepoSpyTreeItem(new IAggregateNode((Resource<?>) entry.getFieldValue()),
                        controller);
                parent.subresources.add(subentry);
                entry.setTreeNode(subentry);
            }

            parent.leaves.add(entry);
        }

    }

    private static void leafEntriesForComposite(IAggregateNode parent, RepoSpyController controller) {

        parent.leaves = new ArrayList<>();
        parent.subresources.clear();
        for (Kind<?> kind : parent.getRes().id().kind().getParts()) {
            IKind<?> ikind = (IKind<?>) kind;
            Object key = ikind.getSelector();
            ILeafNode entry = new ILeafNode(parent.getRes().id(), controller, kind, key);
            if (entry.getFieldValue() == null && RepoUtils.isAggregateStereotype(kind.getStereotype())) {
                entry.createFromNull();
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                RepoSpyTreeItem subentry = new RepoSpyTreeItem(new IAggregateNode((Resource<?>) entry.getFieldValue()),
                        controller);
                parent.subresources.add(subentry);
                entry.setTreeNode(subentry);
            }

            parent.leaves.add(entry);
        }

    }

}
