package net.objectof.actof.repospy.controllers.navigator;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.repospy.controllers.navigator.kind.KindTreeItem;


public class ResourceSelectedChange extends Change {

    private KindTreeItem entry;

    public ResourceSelectedChange(KindTreeItem entry) {
        this.entry = entry;
    }

    public KindTreeItem getEntry() {
        return entry;
    }

}
