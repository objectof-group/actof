package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.TextField;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.actof.widgets.masonry.MasonryPane.Layout;
import net.objectof.aggr.Mapping;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;


public class MappedLayout extends AggregateLayout {

    private TextField keyField = new TextField();

    public MappedLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy);
        cards.setLayout(Layout.GRID);
        keyField.setPromptText("key");
        keyField.setOnAction(event -> onAdd());
        getControlCard().setTitleContent(keyField);
        updateUI();
    }

    @Override
    protected void onAdd() {
        String key = keyField.getText();
        Mapping<String, ?> map = (Mapping<String, ?>) getEntry().getRes();
        if (map.containsKey(key)) {
            Dialogs.create().message("This key already exists").title("Cannot Create Entry").showError();
            return;
        }
        if (key.length() == 0) {
            @SuppressWarnings("deprecation")
            Action a = Dialogs.create().message("Are you sure you want to create a mapping entry with an empty key?")
                    .title("Empty Key").showConfirm();
            if (a != Dialog.ACTION_YES) { return; }
        }
        map.put(key, null);

        // TODO: There has to be a better way than creating a dummy leaf node
        // just to set the hisotry?
        ILeafNode leaf = new ILeafNode(getEntry().getRes().id(), repospy, getEntry().getRes().id().kind().getParts()
                .get(0), key);
        leaf.addChangeHistory(null);

        getEntry().refreshNode();
    }

    @Override
    protected void onRemove(ILeafNode leaf) {
        Mapping<String, ?> map = (Mapping<String, ?>) getEntry().getRes();
        map.remove(leaf.getKey());
        leaf.addChangeHistory(null);
        getEntry().refreshNode();
    }

}
