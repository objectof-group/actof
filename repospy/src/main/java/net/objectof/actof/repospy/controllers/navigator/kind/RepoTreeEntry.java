package net.objectof.actof.repospy.controllers.navigator.kind;


import java.util.List;

import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.model.Resource;


public interface RepoTreeEntry {

    public boolean hasChildren();

    public List<RepoTreeEntry> getChildren(RepositoryController repository, SearchController search);

    public String getEntityKind();

    public Resource<?> getRes();

}
