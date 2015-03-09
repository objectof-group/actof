package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import javafx.scene.Node;
import javafx.scene.layout.VBox;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;
import net.objectof.actof.repospy.controllers.navigator.kind.ILeafNode;


public class UnsupportedEditor extends AbstractEditor {

    public UnsupportedEditor(ILeafNode entry) {
        super(entry);
    }

    @Override
    public void focus() {}

    @Override
    public Node getNode() {
        return new VBox();
    }

    @Override
    protected boolean validate(String input) {
        return true;
    }

}
