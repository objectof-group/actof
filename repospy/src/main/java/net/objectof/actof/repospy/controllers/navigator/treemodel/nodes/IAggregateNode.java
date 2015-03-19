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

    @SuppressWarnings("unchecked")
    public Aggregate<Object, Resource<?>> getAggregate() {
        return (Aggregate<Object, Resource<?>>) getRes();
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

    /**
     * Updates the child tree nodes and leaf entries. This should be called when
     * direct modification of the Aggregate object has caused it to become out
     * of sync with the IAggregateNode's model.
     * 
     * @param repospy
     */
    public void refreshNode(RepoSpyController repospy) {
        getLeafEntries(repospy);
    }

    @Override
    public Stereotype getStereotype() {
        return res.id().kind().getStereotype();
    }

    private void getLeafEntries(RepoSpyController controller) {
        initalized = true;
        if (this.getStereotype() == Stereotype.COMPOSED) {
            leafEntriesForComposite(controller);
        } else {
            leafEntriesForAggregate(controller);
        }

    }

    private void leafEntriesForAggregate(RepoSpyController controller) {

        @SuppressWarnings("unchecked")
        Aggregate<Object, Resource<?>> agg = getAggregate();
        Set<Object> keys = agg.keySet();

        Kind<?> kind = getRes().id().kind().getParts().get(0);

        leaves = new ArrayList<>();
        subresources.clear();
        for (Object key : keys) {

            ILeafNode entry = new ILeafNode(getRes().id(), controller, kind, key);
            if (entry.getFieldValue() == null) {
                if (RepoUtils.isAggregateStereotype(entry.getKind().getStereotype())) {
                    entry.createFromNull();
                }
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                RepoSpyTreeItem subentry = new RepoSpyTreeItem(new IAggregateNode((Resource<?>) entry.getFieldValue()),
                        controller);
                subresources.add(subentry);
                entry.setTreeNode(subentry);
            }

            leaves.add(entry);
        }

    }

    private void leafEntriesForComposite(RepoSpyController controller) {

        leaves = new ArrayList<>();
        subresources.clear();
        for (Kind<?> kind : getRes().id().kind().getParts()) {
            IKind<?> ikind = (IKind<?>) kind;
            Object key = ikind.getSelector();
            ILeafNode entry = new ILeafNode(getRes().id(), controller, kind, key);
            if (entry.getFieldValue() == null && RepoUtils.isAggregateStereotype(kind.getStereotype())) {
                entry.createFromNull();
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                RepoSpyTreeItem subentry = new RepoSpyTreeItem(new IAggregateNode((Resource<?>) entry.getFieldValue()),
                        controller);
                subresources.add(subentry);
                entry.setTreeNode(subentry);
            }

            leaves.add(entry);
        }

    }

}
