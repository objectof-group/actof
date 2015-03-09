package net.objectof.actof.repospy.controllers.navigator;


import javafx.scene.control.TreeItem;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.repospy.controllers.navigator.kind.RepoTreeEntry;


public class ResourceSelectedChange extends Change {

    private TreeItem<RepoTreeEntry> entry;

    public ResourceSelectedChange(TreeItem<RepoTreeEntry> entry) {
        this.entry = entry;
    }

    public TreeItem<RepoTreeEntry> getEntry() {
        return entry;
    }

}
