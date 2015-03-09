package net.objectof.actof.repospy.controllers.navigator.composite.editors.primitive;


import javafx.scene.Node;
import javafx.scene.layout.VBox;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;


public class UnsupportedEditor extends AbstractEditor {

    public UnsupportedEditor(CompositeEntry entry) {
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
