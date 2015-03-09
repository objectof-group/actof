package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;


public abstract class AbstractEditor implements Editor {

    private CompositeEntry entry;

    public AbstractEditor(CompositeEntry entry) {
        this.entry = entry;
    }

    protected CompositeEntry getEntry() {
        return entry;
    }

    protected abstract boolean validate(String input);

}
