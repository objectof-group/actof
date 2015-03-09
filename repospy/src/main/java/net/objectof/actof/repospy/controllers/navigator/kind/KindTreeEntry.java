package net.objectof.actof.repospy.controllers.navigator.kind;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.model.Resource;
import net.objectof.model.Transaction;


public class KindTreeEntry implements RepoTreeEntry {

    private String entityKind;

    public KindTreeEntry(String entityKind) {
        this.entityKind = entityKind;
    }

    @Override
    public String toString() {
        return entityKind;
    }

    public String getEntityKind() {
        return entityKind;
    }

    public boolean hasChildren() {
        return true;
    }

    public Resource<?> getRes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RepoTreeEntry> getChildren(RepositoryController repository, SearchController search) {

        Transaction tx = repository.getStagingTx();
        String kind = getEntityKind();
        Iterable<Resource<?>> iter;
        List<RepoTreeEntry> newlist = new ArrayList<>();

        if (search.isValid() && kind.equals(search.getKind())) {
            iter = tx.query(kind, search.getQuery());
        } else if (search.isValid()) {
            return FXCollections.emptyObservableList();
        } else {
            iter = tx.enumerate(kind);
        }

        // persistent entities
        for (Resource<?> res : iter) {
            newlist.add(new ResourceTreeEntry(res));
        }

        // transient entities
        for (Resource<?> res : repository.getTransientsForKind(kind)) {
            newlist.add(new ResourceTreeEntry(res));
        }

        return newlist;

    }

}
