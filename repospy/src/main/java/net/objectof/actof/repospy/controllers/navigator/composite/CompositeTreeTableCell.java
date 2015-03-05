package net.objectof.actof.repospy.controllers.navigator.composite;


import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.VBox;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.Editor;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.EditorUtils;
import net.objectof.actof.repospy.controllers.navigator.composite.viewers.Viewer;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;


/**
 * Lifted from TextFieldTreeTableCell so that it can handle different
 * stereotypes
 *
 */
public class CompositeTreeTableCell extends TreeTableCell<CompositeEntry, CompositeEntry> {

    private Viewer viewer;
    private Editor editor;

    private Node parent;
    private PopOver popover;
    private VBox popoverBox;

    public CompositeTreeTableCell(Node parent) {
        super();
        this.parent = parent;
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getTreeTableView().isEditable() || !getTableColumn().isEditable()) { return; }

        super.startEdit();
        setText(null);
        showWrite();

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        showRead();
    }

    @Override
    public void updateItem(CompositeEntry item, boolean empty) {

        super.updateItem(item, empty);

        setText(null);

        if (empty) {
            setGraphic(null);
            editor = null;
            viewer = null;
            popover(false);
        } else {
            if (isEditing()) {
                // item for this cell was updated while we were editing --
                // cancel edit
                cancelEdit();
            } else {
                editor = null;
                viewer = null;
                showRead();
            }
        }

    }

    private void showRead() {
        if (getItem() == null) {
            setGraphic(new Label("null"));
        }
        popover(false);
        setGraphic(getViewer().getNode());
    }

    private void showWrite() {
        if (getEditor().isPopOver()) {
            showPopOver();
        } else {
            showWriteNode();
        }
    }

    private void showWriteNode() {
        popover(false);
        setGraphic(getEditor().getNode());
        getEditor().focus();
    }

    private void showPopOver() {
        popover(true);
        setGraphic(getViewer().getNode());
        getEditor().focus();
    }

    private void popover(boolean show) {

        if (popover == null) {
            popoverBox = new VBox();
            popoverBox.setPadding(new Insets(6));
            popover = new PopOver(popoverBox);
            popover.setArrowLocation(ArrowLocation.TOP_CENTER);
            popover.setAutoHide(false);
            popover.setHideOnEscape(false);
            popover.setOpacity(1);
        }

        if (show) {
            Node node = getEditor().getNode();
            popoverBox.getChildren().clear();
            popoverBox.getChildren().add(node);
            Bounds bounds = this.localToScreen(this.getBoundsInLocal());
            popover.show(parent, bounds.getMinX() + bounds.getWidth() / 2, bounds.getMinY() + bounds.getHeight() - 4);
        } else {
            popover.hide();
        }
    }

    private Editor getEditor() {
        if (editor == null) {
            editor = EditorUtils.createConfiguredEditor(this);
        }
        return editor;
    }

    private Viewer getViewer() {
        if (viewer == null) {
            viewer = Viewer.createViewer(this.getItem());
        }
        return viewer;
    }
}
