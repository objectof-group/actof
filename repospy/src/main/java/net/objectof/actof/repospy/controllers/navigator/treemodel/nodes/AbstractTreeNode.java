package net.objectof.actof.repospy.controllers.navigator.treemodel.nodes;


import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.TreeNode;


public abstract class AbstractTreeNode implements TreeNode {

    private TreeNode parent;

    public AbstractTreeNode(TreeNode parent) {
        this.parent = parent;
    }

    public RepoSpyController getRepospy() {
        return parent.getRepospy();
    }

    public TreeNode getParent() {
        return parent;
    }

}
