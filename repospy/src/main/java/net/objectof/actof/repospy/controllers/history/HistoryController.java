package net.objectof.actof.repospy.controllers.history;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.repository.RepositoryReplacedChange;
import net.objectof.actof.repospy.changes.EditingChange;
import net.objectof.actof.repospy.changes.HistoryChange;


public class HistoryController {

    private Map<String, EditingChange> history = FXCollections.observableMap(new LinkedHashMap<>());

    ChangeController changes;

    public HistoryController(ChangeController changes) {
        this.changes = changes;
        changes.listen(RepositoryReplacedChange.class, this::clear);
        changes.listen(EditingChange.class, this::edit);
    }

    public void clear() {
        history.clear();
        changes.broadcast(new HistoryChange());
    }

    private void edit(EditingChange edit) {
        String key = edit.getQualifiedName();
        history.put(key, edit);
        System.out.println(this);
        changes.broadcast(new HistoryChange());
    }

    public List<EditingChange> getChanges() {
        return history.values().stream().filter(edit -> edit.isChanged()).collect(Collectors.toList());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HistoryController:\n");
        for (EditingChange change : history.values()) {
            sb.append("  " + change.toString() + "\n");
        }
        return sb.toString();
    }
}
