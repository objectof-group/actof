package net.objectof.actof.repospy.controller.history;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objectof.actof.repospy.changes.EditingChange;

public class HistoryModel {

	public Map<String, EditingChange> keyset = new HashMap<>();
	public ObservableList<EditingChange> history = FXCollections.observableArrayList();
	
}
