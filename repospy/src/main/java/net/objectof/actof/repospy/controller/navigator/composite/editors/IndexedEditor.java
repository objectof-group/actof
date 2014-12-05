package net.objectof.actof.repospy.controller.navigator.composite.editors;

import java.util.HashSet;
import java.util.Set;

import net.objectof.actof.repospy.controller.navigator.composite.CompositeEntry;
import net.objectof.aggr.Listing;

public class IndexedEditor extends AbstractAggregateEditor {
	
	public IndexedEditor(CompositeEntry entry) {
		super(entry, false);
	}

	
	@Override
	protected Set<String> getElements() {
		Set<String> indexes = new HashSet<>();
		int count = 0;
		for (@SuppressWarnings("unused") Object o : asList()) {
			indexes.add("" + count++);
		}
		return indexes;
	}

	@Override
	protected void doRemove(String key) {
		
		try {
			int i = Integer.parseInt(key);
			asList().remove(i);
			modified();
		} catch (NumberFormatException e) {
			
		}
	}
	
	@Override
	protected void doAdd(String key) {
		asList().add(null);
		modified();
	}
	
	
	@Override
	protected final void addListeners() {
		
		field.valueProperty().addListener(change -> {
			String value = field.getValue();
			boolean contains = getElements().contains(value);			
			remove.setDisable(!contains);
		});
		
	}
	
	
	@SuppressWarnings("unchecked")
	private Listing<Object> asList() {
		Object value = getEntry().getFieldValue();
		if (value == null) {
			value = getEntry().createFromNull();
		}
		return (Listing<Object>) value;
	}


	@Override
	protected String defaultValue() {
		
		//determine what the current value should be
		if (field.getItems().size() > 0) {
			return field.getItems().get(0);
		}
		return null;
		
	}
	
	@Override
	protected boolean validate(String input) {
		return true;
	}
	

}
