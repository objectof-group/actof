package net.objectof.actof.repospy.controllers.navigator.treemodel;


import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public interface TreeNode {

    public boolean hasChildren();

    public ObservableList<TreeItem<TreeNode>> getChildren();

    /**
     * Updates the child tree nodes and leaf entries. This should be called when
     * direct modification of the model has caused it to become out of sync with
     * the TreeNode's representation.
     * 
     * @param repospy
     */
    public void refreshNode();

    public String getEntityKind();

    public Stereotype getStereotype();

    public Resource<?> getRes();

    public RepoSpyController getRepospy();

    public TreeNode getParent();

}
