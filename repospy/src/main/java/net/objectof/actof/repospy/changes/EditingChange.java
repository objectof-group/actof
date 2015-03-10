package net.objectof.actof.repospy.changes;


import net.objectof.actof.common.controller.change.Change;
import net.objectof.model.Kind;
import net.objectof.model.Stereotype;


public abstract class EditingChange extends Change {

    public abstract String getName();

    public abstract Stereotype getStereotype();

    public abstract Kind<?> getKind();

    public abstract Object oldValue();

    public abstract Object newValue();

}
