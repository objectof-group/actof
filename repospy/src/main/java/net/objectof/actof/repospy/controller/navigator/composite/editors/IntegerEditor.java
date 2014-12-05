package net.objectof.actof.repospy.controller.navigator.composite.editors;

import net.objectof.actof.repospy.controller.navigator.composite.CompositeEntry;

public class IntegerEditor extends TextEditor {

	public IntegerEditor(CompositeEntry entry) {
		super(entry);
		
		field.textProperty().addListener((obs, oldval, newval) -> {
			if (!validate(newval)) {
				 field.setText(oldval);
			}
		 }); 
		
	}
	
	protected boolean validate(String text) {
		 try {
			 Long.parseLong(text);
			 return true;
		 } catch (NumberFormatException e) {
			return false;
		 }
	}

}
