package net.objectof.actof.repospy.controller.navigator.composite.editors;

import java.util.function.Consumer;

import javafx.scene.Node;

public interface Editor {
	
	void focus();

	/**
	 * Callback for when editing is cancelled
	 */
	void setOnCancel(Runnable onCancel);
	
	/**
	 * Callback for when editing is complete
	 */
	void setOnComplete(Consumer<String> onComplete);

	/**
	 * Callback for when the editor itself modifies the object
	 * @param onModify
	 */
	void setOnModify(Consumer<Object> onModify);
	
	Node getNode();

	void modified();
	boolean isModified();

}
