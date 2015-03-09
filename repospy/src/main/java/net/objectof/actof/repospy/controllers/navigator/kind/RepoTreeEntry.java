package net.objectof.actof.repospy.controllers.navigator.kind;


import java.util.List;

import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;


public interface RepoTreeEntry {

    public boolean hasChildren();

    public List<ResourceTreeEntry> getChildren(RepoSpyController repospy);

    public String getEntityKind();

    public Stereotype getStereotype();

    public Resource<?> getRes();

}
