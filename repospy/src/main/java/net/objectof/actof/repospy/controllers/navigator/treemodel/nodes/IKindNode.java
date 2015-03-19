package net.objectof.actof.repospy.controllers.navigator.treemodel.nodes;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class IKindNode extends AbstractTreeNode {

    private Kind<?> kind;
    private ObservableList<TreeItem<TreeNode>> children = FXCollections.observableArrayList();

    public IKindNode(TreeNode parent, Kind<?> kind) {
        super(parent);
        this.kind = kind;
    }

    @Override
    public String toString() {
        return getEntityKind();
    }

    public String getEntityKind() {
        return kind.getComponentName();
    }

    public Kind<?> getKind() {
        return kind;
    }

    public boolean hasChildren() {
        return true;
    }

    public Resource<?> getRes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObservableList<TreeItem<TreeNode>> getChildren() {

        Transaction tx = getRepospy().repository.getStagingTx();
        String kind = getEntityKind();
        Iterable<Resource<?>> iter;

        System.out.println("Cleared Children");

        children.clear();

        if (getRepospy().search.isValid() && kind.equals(getRepospy().search.getKind())) {
            iter = tx.query(kind, getRepospy().search.getQuery());
        } else if (getRepospy().search.isValid()) {
            return FXCollections.emptyObservableList();
        } else {
            iter = tx.enumerate(kind);
        }

        // persistent entities
        for (Resource<?> res : iter) {
            if (RepoUtils.isEmptyAggregate(res)) {
                continue;
            }
            children.add(new RepoSpyTreeItem(new IAggregateNode(this, res), getRepospy()));
        }

        // transient entities
        for (Resource<?> res : getRepospy().repository.getTransientsForKind(kind)) {
            children.add(new RepoSpyTreeItem(new IAggregateNode(this, res), getRepospy()));
        }

        return children;

    }

    @Override
    public Stereotype getStereotype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refreshNode() {
        getChildren();
    }

}
