package net.objectof.actof.repospy.controllers.navigator.treemodel.nodes;


import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.changes.EntityDeletedChange;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.aggr.Aggregate;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.impl.IKind;


public class IAggregateNode extends AbstractTreeNode {

    private Resource<?> res;

    private ObservableList<ILeafNode> leaves = FXCollections.observableArrayList();
    private ObservableList<TreeItem<TreeNode>> subresources = FXCollections.observableArrayList();
    private boolean initalized = false;

    public IAggregateNode(TreeNode parent, Resource<?> res) {
        super(parent);
        this.res = res;
    }

    @Override
    public Image getImage() {
        return new Image(IAggregateNode.class.getResourceAsStream("icons/" + getStereotype().toString().toLowerCase()
                + ".png"));
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
    public ObservableList<TreeItem<TreeNode>> getChildren() {

        if (!initalized) {
            getLeafEntries();
        }

        return subresources;

    }

    public List<ILeafNode> getLeaves(RepoSpyController repospy) {

        if (!initalized) {
            getLeafEntries();
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
    public void refreshNode() {
        getLeafEntries();
    }

    /**
     * Deletes this aggregate from the model
     */
    public void delete() {

        // clear the aggregate of any values
        Aggregate<Object, Resource<?>> agg = getAggregate();
        for (Object key : agg.keySet()) {
            agg.set(key, null);
        }

        // if this resource is a transient resources, remove it
        getRepospy().repository.removeTransientEntity(getRes());

        // remove this aggregate from the parent
        getParent().refreshNode();

        getRepospy().getChangeBus().broadcast(new EntityDeletedChange(getRes()));

    }

    @Override
    public Stereotype getStereotype() {
        return res.id().kind().getStereotype();
    }

    private void getLeafEntries() {
        initalized = true;
        if (this.getStereotype() == Stereotype.COMPOSED) {
            leafEntriesForComposite();
        } else {
            leafEntriesForAggregate();
        }

    }

    private void leafEntriesForAggregate() {

        Aggregate<Object, Resource<?>> agg = getAggregate();
        Set<Object> keys = agg.keySet();

        Kind<?> kind = getRes().id().kind().getParts().get(0);

        leaves.clear();
        subresources.clear();
        for (Object key : keys) {

            ILeafNode entry = new ILeafNode(getRes().id(), getRepospy(), kind, key);
            if (entry.getFieldValue() == null) {
                if (RepoUtils.isAggregateStereotype(entry.getKind().getStereotype())) {
                    entry.createFromNull();
                }
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                TreeNode subnode = new IAggregateNode(this, (Resource<?>) entry.getFieldValue());
                RepoSpyTreeItem subentry = new RepoSpyTreeItem(subnode, getRepospy());
                subresources.add(subentry);
                entry.setTreeNode(subentry);
            }

            leaves.add(entry);
        }

    }

    private void leafEntriesForComposite() {

        leaves.clear();
        subresources.clear();
        for (Kind<?> kind : getRes().id().kind().getParts()) {
            IKind<?> ikind = (IKind<?>) kind;
            Object key = ikind.getSelector();
            ILeafNode entry = new ILeafNode(getRes().id(), getRepospy(), kind, key);
            if (entry.getFieldValue() == null && RepoUtils.isAggregateStereotype(kind.getStereotype())) {
                entry.createFromNull();
            }

            if (RepoUtils.isAggregateStereotype(entry.getStereotype())) {
                TreeNode subnode = new IAggregateNode(this, (Resource<?>) entry.getFieldValue());
                RepoSpyTreeItem subentry = new RepoSpyTreeItem(subnode, getRepospy());
                subresources.add(subentry);
                entry.setTreeNode(subentry);
            }

            leaves.add(entry);
        }

    }

}
