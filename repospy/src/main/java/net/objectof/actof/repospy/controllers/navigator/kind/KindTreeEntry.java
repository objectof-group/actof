package net.objectof.actof.repospy.controllers.navigator.kind;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.model.Resource;
import net.objectof.model.Stereotype;
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
    public List<ResourceTreeEntry> getChildren(RepoSpyController repospy) {

        Transaction tx = repospy.repository.getStagingTx();
        String kind = getEntityKind();
        Iterable<Resource<?>> iter;
        List<ResourceTreeEntry> newlist = new ArrayList<>();

        if (repospy.search.isValid() && kind.equals(repospy.search.getKind())) {
            iter = tx.query(kind, repospy.search.getQuery());
        } else if (repospy.search.isValid()) {
            return FXCollections.emptyObservableList();
        } else {
            iter = tx.enumerate(kind);
        }

        // persistent entities
        for (Resource<?> res : iter) {
            newlist.add(new ResourceTreeEntry(res));
        }

        // transient entities
        for (Resource<?> res : repospy.repository.getTransientsForKind(kind)) {
            newlist.add(new ResourceTreeEntry(res));
        }

        return newlist;

    }

    @Override
    public Stereotype getStereotype() {
        throw new UnsupportedOperationException();
    }

}
