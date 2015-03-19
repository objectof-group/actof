package net.objectof.actof.common.controller;


import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;


// adapted from
// http://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TreeItem.html
public abstract class DynamicTreeItem<T> extends TreeItem<T> {

    private boolean notYetLoadedChildren = true;

    private ObservableList<TreeItem<T>> realChildList = null;

    public DynamicTreeItem(T kv) {
        super(kv);
    }

    @Override
    public ObservableList<TreeItem<T>> getChildren() {
        if (notYetLoadedChildren) {
            notYetLoadedChildren = false;
            updateChildren();
        }
        return super.getChildren();
    }

    public void updateChildren() {
        // only update child nodes if they've already been looked up
        if (!notYetLoadedChildren) {
            realChildList = createChildList(this);
            updateChildItems();
            realChildList.addListener(new ListChangeListener<TreeItem<T>>() {

                @Override
                public void onChanged(javafx.collections.ListChangeListener.Change<? extends TreeItem<T>> c) {
                    updateChildItems();
                }
            });
        }
    }

    private void updateChildItems() {
        super.getChildren().setAll(realChildList);
    }

    @Override
    public boolean isLeaf() {
        T kv = getValue();
        return isLeafNode(kv);
    }

    public abstract boolean isLeafNode(T t);

    protected abstract ObservableList<TreeItem<T>> createChildList(TreeItem<T> TreeItem);

}
