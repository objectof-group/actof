package net.objectof.actof.repospy.controllers.navigator.composite.editors;


import net.objectof.actof.repospy.controllers.navigator.kind.ILeafNode;


public abstract class AbstractEditor implements Editor {

    private ILeafNode entry;

    public AbstractEditor(ILeafNode entry) {
        this.entry = entry;
    }

    protected ILeafNode getEntry() {
        return entry;
    }

    protected abstract boolean validate(String input);

}
