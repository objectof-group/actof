package net.objectof.actof.repospy.controllers.navigator;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;


public class ResourceSelectedChange extends Change {

    private RepoSpyTreeItem entry;

    public ResourceSelectedChange(RepoSpyTreeItem entry) {
        this.entry = entry;
    }

    public RepoSpyTreeItem getEntry() {
        return entry;
    }

}
