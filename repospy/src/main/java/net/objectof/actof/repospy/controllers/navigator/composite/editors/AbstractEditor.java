package net.objectof.actof.repospy.controllers.navigator.composite.editors;

import java.util.function.Consumer;

import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;

public abstract class AbstractEditor implements Editor {

	private CompositeEntry entry;
	protected Consumer<String> onComplete;
	protected Consumer<Object> onModify;
	protected Runnable onCancel;
	private boolean dirty = false;
	
	public AbstractEditor(CompositeEntry entry) {
		this.entry = entry;
	}
	
	@Override
	public void setOnComplete(Consumer<String> onComplete) {
		this.onComplete = onComplete;
	}

	@Override
	public void setOnModify(Consumer<Object> onModify) {
		this.onModify = onModify;
	}
	
	@Override
	public void setOnCancel(Runnable onCancel) {
		this.onCancel = onCancel;
	}
	
	protected CompositeEntry getEntry() {
		return entry;
	}
	
	@Override
	public void modified() {
		dirty = true;
		onModify.accept(entry.getFieldValue());
		entry.refreshFromModel();
	}
	
	@Override
	public boolean isModified() {
		return dirty;
	}
	
	protected abstract boolean validate(String input);
	
}
