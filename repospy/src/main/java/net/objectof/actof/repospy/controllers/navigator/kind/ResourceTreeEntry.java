package net.objectof.actof.repospy.controllers.navigator.kind;


import java.util.ArrayList;
import java.util.List;

import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.model.Kind;
import net.objectof.model.Resource;
import net.objectof.model.impl.aggr.IComposite;


public class ResourceTreeEntry implements RepoTreeEntry {

    private Resource<?> res;

    public ResourceTreeEntry(Resource<?> res) {
        this.res = res;
    }

    @Override
    public String toString() {

        String name = res.id().kind().getComponentName();
        if (res instanceof IComposite) {
            return name + " #" + res.id().label().toString();
        } else {
            String[] parts = name.split("\\.");
            name = parts[parts.length - 1];
            return name;
        }
    }

    public Resource<?> getRes() {
        return res;
    }

    public String getEntityKind() {
        throw new UnsupportedOperationException();
    }

    public boolean hasChildren() {
        for (Kind<?> kind : res.id().kind().getParts()) {
            switch (kind.getStereotype()) {

                case COMPOSED:
                case MAPPED:
                case INDEXED:
                case SET:
                    return true;
                default:
                    break;

            }
        }
        return false;
    }

    @Override
    public List<RepoTreeEntry> getChildren(RepositoryController repository, SearchController search) {
        List<RepoTreeEntry> children = new ArrayList<>();
        IComposite composite = (IComposite) res;

        for (Kind<?> kind : res.id().kind().getParts()) {

            System.out.println(kind);
            switch (kind.getStereotype()) {

                case COMPOSED:
                    break;
                case SET:
                case INDEXED:
                case MAPPED:
                    String[] keyParts = kind.getComponentName().split("\\.");
                    String key = keyParts[keyParts.length - 1];
                    Resource<?> map = (Resource<?>) composite.get(key);
                    children.add(new ResourceTreeEntry(map));

                default:
                    break;

            }
        }

        return children;

    }
}
