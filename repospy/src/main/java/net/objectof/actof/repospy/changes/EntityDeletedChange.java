package net.objectof.actof.repospy.changes;


import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class EntityDeletedChange extends EditingChange {

    Resource<?> value;

    public EntityDeletedChange(RepoSpyController repospy, Resource<?> res) {
        value = res;
    }

    @Override
    public String toString() {
        return "Created " + value.getUniqueName();
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
        return RepoUtils.prettyPrint(value);
    }

    public boolean isChanged() {
        if (super.isChanged() == false) { return false; }
        if (newValue() == null && RepoUtils.isResourceTransient(value)) { return false; }
        return true;
    }
}
