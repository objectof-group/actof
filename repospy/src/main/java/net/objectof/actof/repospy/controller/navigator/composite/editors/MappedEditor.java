package net.objectof.actof.repospy.controller.navigator.composite.editors;

import java.util.Set;

import net.objectof.actof.repospy.controller.navigator.composite.CompositeEntry;
import net.objectof.aggr.Mapping;

public class MappedEditor extends AbstractAggregateEditor {
	
	public MappedEditor(CompositeEntry entry) {
		super(entry, true);
	}

	
	@Override
	protected Set<String> getElements() {
		return asMap().keySet();
	}

	@Override
	protected void doRemove(String key) {
		if (asMap().keySet().contains(key)) { modified(); }
		asMap().remove(key);
		modified();
	}
	
	@Override
	protected void doAdd(String key) {
		if(asMap().keySet().contains(key)) { return; }
		asMap().put(key, null);
		modified();
	}
	
	@Override
	protected final void addListeners() {

		field.getEditor().textProperty().addListener(change -> {
			String text = field.getEditor().getText();
			boolean contains = getElements().contains(text);
			add.setDisable(contains);
			remove.setDisable(!contains);
		});
		
	}
	
	@SuppressWarnings("unchecked")
	private Mapping<String, Object> asMap() {
		Object value = getEntry().getFieldValue();
		if (value == null) {
			value = getEntry().createFromNull();
		}
		return (Mapping<String, Object>) value;
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
