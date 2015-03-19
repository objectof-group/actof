package net.objectof.actof.repospy.controllers.navigator.treemodel;


import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public interface TreeNode {

    boolean hasChildren();

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

    default Image getImage() {
        return null;
    }

}
