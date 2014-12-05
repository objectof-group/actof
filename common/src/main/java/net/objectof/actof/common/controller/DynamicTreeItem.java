package net.objectof.actof.common.controller;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

//adapted from http://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TreeItem.html
public abstract class DynamicTreeItem<T> extends TreeItem<T> {

	// We do the children and leaf testing only once, and then set these
	// booleans to false so that we do not check again during this
	// run. A more complete implementation may need to handle more
	// dynamic file system situations (such as where a folder has files
	// added after the TreeView is shown). Again, this is left as an
	// exercise for the reader.
	private boolean isFirstTimeChildren = true;

	//private ObservableList<TreeItem<T>> children = FXCollections.observableArrayList();

	public DynamicTreeItem(T kv) {
		super(kv);
	}

	@Override
	public ObservableList<TreeItem<T>> getChildren() {
		if (isFirstTimeChildren) {
			isFirstTimeChildren = false;
			updateChildren();
		}
		return super.getChildren();
	}

	public void updateChildren() {
		//only update child nodes if they've already been looked up
		if (!isFirstTimeChildren) {
			super.getChildren().setAll(buildChildren(this));
		}
	}

	@Override
	public boolean isLeaf() {
		T kv = getValue();
		return isLeafNode(kv);		
	}
	
	
	public abstract boolean isLeafNode(T t);

	protected abstract List<TreeItem<T>> buildChildren(TreeItem<T> TreeItem);

}
