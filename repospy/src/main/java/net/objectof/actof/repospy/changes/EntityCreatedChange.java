package net.objectof.actof.repospy.changes;


import net.objectof.actof.common.util.RepoUtils;
import net.objectof.actof.common.util.RepoUtils.PrintStyle;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public class EntityCreatedChange extends EditingChange {

    private Resource<?> value;

    public EntityCreatedChange(Resource<?> value) {
        this.value = value;
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
        return null;
    }

    @Override
    public Object newValue() {
        return value;
    }

    @Override
    public String getName() {
        return "Created " + RepoUtils.prettyPrint(value, PrintStyle.LONG);
    }

}
