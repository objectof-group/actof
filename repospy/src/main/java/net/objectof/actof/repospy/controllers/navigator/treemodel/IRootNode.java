package net.objectof.actof.repospy.controllers.navigator.treemodel;


import java.util.List;

import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class IRootNode implements TreeNode {

    private String packageName = "Not Connected";

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public List<KindTreeItem> getChildren(RepoSpyController repospy) {
        throw new UnsupportedOperationException();
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
}
