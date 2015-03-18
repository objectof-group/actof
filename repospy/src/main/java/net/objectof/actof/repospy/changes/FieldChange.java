package net.objectof.actof.repospy.changes;


import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;


public class FieldChange extends EditingChange {

    private Object oldValue, newValue;
    private ILeafNode leafnode;

    public FieldChange(Object oldValue, Object newValue, ILeafNode entry) {
        this.leafnode = entry;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return "Changed " + getName() + " from '" + RepoUtils.prettyPrint(oldValue) + "' to '"
                + RepoUtils.prettyPrint(newValue) + "'";
    }

    @Override
    public Object oldValue() {
        return oldValue;
    }

    @Override
    public Object newValue() {
        return newValue;
    }

    @Override
    public Stereotype getStereotype() {
        return leafnode.getStereotype();
    }

    @Override
    public Kind<?> getKind() {
        return leafnode.kind;
    }

    @Override
    public String getName() {
        return leafnode.getName();
    }

    public ILeafNode getLeafnode() {
        return leafnode;
    }

}
