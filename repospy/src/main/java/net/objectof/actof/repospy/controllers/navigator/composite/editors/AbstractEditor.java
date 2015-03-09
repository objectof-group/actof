package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import net.objectof.actof.repospy.controllers.navigator.kind.LeafEntry;


public abstract class AbstractEditor implements Editor {

    private LeafEntry entry;

    public AbstractEditor(LeafEntry entry) {
        this.entry = entry;
    }

    protected LeafEntry getEntry() {
        return entry;
    }

    protected abstract boolean validate(String input);

}
