package net.objectof.actof.repospy.controllers.navigator.composite;


import javafx.scene.Node;
import javafx.scene.control.TreeTableCell;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.Editor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.EditorUtils;


/**
 * Lifted from TextFieldTreeTableCell so that it can handle different
 * stereotypes
 *
 */
public class CompositeTreeTableCell extends TreeTableCell<CompositeEntry, CompositeEntry> {

    private Editor editor;

    public CompositeTreeTableCell() {
        super();
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getTreeTableView().isEditable() || !getTableColumn().isEditable()) { return; }

        super.startEdit();

        if (isEditing()) {
            setText(null);
            setGraphic(getEditor().getWriteNode());
            getEditor().focus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(getEditor().getReadNode());
    }

    @Override
    public void updateItem(CompositeEntry item, boolean empty) {

        super.updateItem(item, empty);

        setText(null);

        if (empty) {
            setGraphic(null);
            editor = null;
        } else {
            if (isEditing()) {
                // item for this cell was updated while we were editing --
                // cancel edit
                cancelEdit();
            } else {
                editor = null;
                Node node = getEditor().getReadNode();
                setGraphic(node);
            }
        }

    }

    private Editor getEditor() {
        if (editor == null) {
            editor = EditorUtils.createConfiguredEditor(this);
        }
        return editor;
    }

}
