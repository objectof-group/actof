package net.objectof.actof.repospy.changes;


import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.common.util.RepoUtils.PrintStyle;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class EntityDeletedChange extends EditingChange {

    Resource<?> value;

    public EntityDeletedChange(Resource<?> res) {
        value = res;
    }

    @Override
    public String toString() {
        return "Deleted " + value.getUniqueName();
    }

    @Override
    public String getQualifiedName() {
        return value.getUniqueName();
    }

    public Resource<?> getValue() {
        return value;
    }

    @Override
    public Stereotype getStereotype() {
        return value.id().kind().getStereotype();
    }

    @Override
    public Kind<?> getKind() {
        return value.id().kind();
    }

    @Override
    public Object oldValue() {
        return value;
    }

    @Override
    public Object newValue() {
        return null;
    }

    @Override
    public String getName() {
        return "Deleted " + RepoUtils.prettyPrint(value, PrintStyle.LONG);
    }

    public boolean isChanged() {
        if (super.isChanged() == false) { return false; }
        if (newValue() == null && RepoUtils.isResourceTransient(value)) { return false; }
        return true;
    }
}
