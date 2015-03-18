package net.objectof.actof.repospy.controllers.navigator.editor.layout;


import javafx.scene.control.TextField;
import net.objectof.actof.repospy.RepoSpyController;
import net.objectof.actof.repospy.controllers.navigator.treemodel.RepoSpyTreeItem;
import net.objectof.actof.repospy.controllers.navigator.treemodel.nodes.ILeafNode;
import net.objectof.aggr.Mapping;

import org.controlsfx.dialog.Dialogs;


public class MappedLayout extends AggregateLayout {

    private TextField keyField = new TextField();

    public MappedLayout(RepoSpyTreeItem treeitem, RepoSpyController repospy) {
        super(treeitem, repospy);
        getControlCard().setTitleContent(keyField);
        capitalize = false;
        updateUI();
    }

    @Override
    protected void onAdd() {
        String key = keyField.getText();
        Mapping<String, ?> map = (Mapping<String, ?>) entry.getRes();
        if (map.containsKey(key)) {
            Dialogs.create().message("This key already exists").title("Cannot Create Entry").showError();
            return;
        }
        map.put(key, null);

        ILeafNode leaf = new ILeafNode(entry, repospy, entry.getRes().id().kind().getParts().get(0), key);
        leaf.addChangeHistory(null);

        entry.refreshLeaves(repospy);
        updateUI();
    }

    @Override
    protected void onRemove(ILeafNode leaf) {
        Mapping<String, ?> map = (Mapping<String, ?>) entry.getRes();
        map.remove(leaf.getKey());
        leaf.addChangeHistory(null);
        entry.refreshLeaves(repospy);
        updateUI();
    }

}
