package net.objectof.actof.repospy.changes;


import net.objectof.actof.repospy.controllers.navigator.kind.ILeafNode;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;


public class FieldChange extends EditingChange {

    private Object oldValue, newValue;
    private ILeafNode entry;

    public FieldChange(Object oldValue, Object newValue, ILeafNode entry) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.entry = entry;
    }


    @Override
    public String getKey() {
        return entry.getName();
    }

    @Override
    public String toString() {

        String oldName = oldValue == null ? "null" : oldValue.toString();
        String newName = newValue == null ? "null" : newValue.toString();

        return "Changed " + getKey() + " from '" + oldName + "' to '" + newName + "'";
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
        return entry.getStereotype();
    }

    @Override
    public Kind<?> getKind() {
        return entry.kind;
    }


}
