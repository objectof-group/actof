package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import net.objectof.actof.repospy.controllers.navigator.ResourceSelectedChange;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;
import net.objectof.actof.repospy.controllers.navigator.kind.ILeafNode;


public class EntryLinkEditor extends AbstractEditor {

    Hyperlink node;

    public EntryLinkEditor(ILeafNode entry) {
        super(entry);
        node = new Hyperlink("view");

        node.setOnAction(event -> {
            entry.getController().getChangeBus().broadcast(new ResourceSelectedChange(entry.treeNode));
        });

    }

    @Override
    public void focus() {
        // TODO Auto-generated method stub

    }

    @Override
    public Node getNode() {
        // TODO Auto-generated method stub
        return node;
    }

    public boolean inline() {
        return true;
    }

    @Override
    protected boolean validate(String input) {
        // TODO Auto-generated method stub
        return true;
    }

}
