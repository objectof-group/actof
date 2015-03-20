package net.objectof.actof.repospy.changes;


import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.common.util.RepoUtils.PrintStyle;
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
        return "Changed " + getQualifiedName() + " from '" + RepoUtils.prettyPrint(oldValue) + "' to '"
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
        return leafnode.getKind();
    }

    @Override
    public String getQualifiedName() {
        return leafnode.getName();
    }

    public ILeafNode getLeafNode() {
        return leafnode;
    }

    @Override
    public String getName() {
        return RepoUtils.prettyPrint(leafnode.getParent(), PrintStyle.LONG) + RepoUtils.SEPARATOR
                + leafnode.getKey().toString();
    }

    public void undo() {
        getLeafNode().setFieldValue(oldValue());
    }

}
