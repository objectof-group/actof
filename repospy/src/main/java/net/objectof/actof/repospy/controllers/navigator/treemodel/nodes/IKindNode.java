package net.objectof.actof.repospy.controllers.navigator.treemodel.nodes;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
import net.objectof.model.Transaction;


public class IKindNode implements TreeNode {

    private Kind<?> kind;

    public IKindNode(Kind<?> kind) {
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
    public ObservableList<TreeItem<TreeNode>> getChildren(RepoSpyController repospy) {

        Transaction tx = repospy.repository.getStagingTx();
        String kind = getEntityKind();
        Iterable<Resource<?>> iter;
        ObservableList<TreeItem<TreeNode>> newlist = FXCollections.observableArrayList();

        if (repospy.search.isValid() && kind.equals(repospy.search.getKind())) {
            iter = tx.query(kind, repospy.search.getQuery());
        } else if (repospy.search.isValid()) {
            return FXCollections.emptyObservableList();
        } else {
            iter = tx.enumerate(kind);
        }

        // persistent entities
        for (Resource<?> res : iter) {
            newlist.add(new RepoSpyTreeItem(new IAggregateNode(res), repospy));
        }

        // transient entities
        for (Resource<?> res : repospy.repository.getTransientsForKind(kind)) {
            newlist.add(new RepoSpyTreeItem(new IAggregateNode(res), repospy));
        }

        return newlist;

    }

    @Override
    public Stereotype getStereotype() {
        throw new UnsupportedOperationException();
    }

}
