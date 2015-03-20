package net.objectof.actof.repospy.controllers.navigator.treemodel;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public interface TreeNode {

    boolean hasChildren();

    default boolean hasLeaves() {
        return false;
    }

    default ObservableList<ILeafNode> getLeaves() {
        return FXCollections.observableArrayList();
    }

    ObservableList<TreeItem<TreeNode>> getChildren();

    /**
     * Updates the child tree nodes and leaf entries. This should be called when
     * direct modification of the model has caused it to become out of sync with
     * the TreeNode's representation.
     * 
     * @param repospy
     */
    void refreshNode();

    String getEntityKind();

    Stereotype getStereotype();

    Resource<?> getRes();

    RepoSpyController getRepospy();

    TreeNode getParent();

}
