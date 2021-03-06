package net.objectof.actof.repospy.controllers.navigator.treemodel.nodes;


import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class IRootNode extends AbstractTreeNode {

    private String packageName = "Not Connected";
    private RepoSpyController repospy;

    public IRootNode(RepoSpyController repospy, TreeNode parent) {
        super(parent);
        this.repospy = repospy;
    }

    @Override
    public RepoSpyController getRepospy() {
        return repospy;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public ObservableList<TreeItem<TreeNode>> getChildren() {

        ObservableList<TreeItem<TreeNode>> children = FXCollections.observableArrayList();
        if (repospy == null) { return children; }
        if (repospy.repository == null) { return children; }
        List<Kind<?>> entities = repospy.repository.getEntities();

        for (Kind<?> kind : entities) {

            String kindname = kind.getComponentName();
            if (repospy.search.isValid() && !repospy.search.getKind().equals(kindname)) {
                continue;
            }

            RepoSpyTreeItem item = new RepoSpyTreeItem(new IKindNode(this, kind), repospy);
            item.setExpanded(repospy.search.isValid());
            children.add(item);
        }

        return children;

    }

    @Override
    public String getEntityKind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stereotype getStereotype() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource<?> getRes() {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return packageName;
    }

    public void setPackageName(String name) {
        this.packageName = name;
    }

    @Override
    public void refreshNode() {
        // NO-OP: This should never need to be refreshed
    }
}
