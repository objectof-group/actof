package net.objectof.actof.repospy.changes;


import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;


public class FieldChange extends EditingChange {

    private Object oldValue, newValue;
    private String name;
    private Stereotype stereotype;
    private Kind<?> kind;

    public FieldChange(Object oldValue, Object newValue, ILeafNode entry) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.name = entry.getName();
        this.stereotype = entry.getStereotype();
        this.kind = entry.kind;
    }

    public FieldChange(Object oldValue, Object newValue, String name, Stereotype stereotype, Kind<?> kind) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.name = name;
        this.stereotype = stereotype;
        this.kind = kind;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {

        String oldName = oldValue == null ? "null" : oldValue.toString();
        String newName = newValue == null ? "null" : newValue.toString();

        return "Changed " + getName() + " from '" + oldName + "' to '" + newName + "'";
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
        return stereotype;
    }

    @Override
    public Kind<?> getKind() {
        return kind;
    }

}
