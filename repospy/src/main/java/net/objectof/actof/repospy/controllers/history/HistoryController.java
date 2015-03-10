package net.objectof.actof.repospy.controllers.history;

import javafx.collections.ObservableList;
import net.objectof.actof.common.controller.change.Change;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.repository.RepositoryReplacedChange;
import net.objectof.actof.repospy.changes.EditingChange;

public class HistoryController {

	HistoryModel model = new HistoryModel();
	
	ChangeController changes;
	
	public HistoryController(ChangeController changes) {
		this.changes = changes;
		changes.listen(this::onChange);
	}
	
	private void onChange(Change change) {
		
		change.when(RepositoryReplacedChange.class, modelchange -> {
			clear(); //the repository model has been cleared/replaced. History no longer applies.
		});
		
		change.when(EditingChange.class, edit -> {
			String key = edit.getName();
			if (model.keyset.containsKey(key)) {
				Change oldChange = model.keyset.get(key);
				model.keyset.remove(key);
				model.history.remove(oldChange);
			}
			
			model.keyset.put(key, edit);
			model.history.add(edit);
		});

	}
	
	public void clear() {
		model.history.clear();
		model.keyset.clear();
	}
	
	public ObservableList<EditingChange> get() {
		return model.history;
	}
	
}
