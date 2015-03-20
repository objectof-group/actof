package net.objectof.actof.repospy.controllers.history;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.repository.RepositoryReplacedChange;
import net.objectof.actof.repospy.changes.EditingChange;
import net.objectof.actof.repospy.changes.HistoryChange;


public class HistoryController {

    private Map<String, EditingChange> keyset = new HashMap<>();
    private ObservableList<EditingChange> history = FXCollections.observableArrayList();

    ChangeController changes;

    public HistoryController(ChangeController changes) {
        this.changes = changes;
        changes.listen(RepositoryReplacedChange.class, this::clear);
        changes.listen(EditingChange.class, this::edit);
    }

    public void clear() {
        history.clear();
        keyset.clear();
        changes.broadcast(new HistoryChange());
    }

    private void edit(EditingChange edit) {
        String key = edit.getQualifiedName();
        if (keyset.containsKey(key)) {
            Change oldChange = keyset.get(key);
            keyset.remove(key);
            history.remove(oldChange);
        }

        keyset.put(key, edit);
        history.add(edit);

        changes.broadcast(new HistoryChange());
    }

    public List<EditingChange> getChanges() {
        return history.stream().filter(edit -> edit.isChanged()).collect(Collectors.toList());
    }

}
