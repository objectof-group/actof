package net.objectof.actof.repospy.controllers.navigator.composite.editors;

import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;

public class RealEditor extends TextEditor {

	public RealEditor(CompositeEntry entry) {
		super(entry);
		
		field.textProperty().addListener((obs, oldval, newval) -> {
			if (!validate(newval)) {
				 field.setText(oldval);
			}
		}); 
		
	}
	
	protected boolean validate(String text) {
		 try {
			 Double.parseDouble(text);
			 return true;
		 } catch (NumberFormatException e) {
			return false;
		 }
	}

}
