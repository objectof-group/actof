package net.objectof.actof.repospy.controllers.navigator.composite.editors.aggregate;


import javafx.scene.Node;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeView;
import net.objectof.actof.repospy.controllers.navigator.composite.editors.AbstractEditor;


public class CompositeEditor extends AbstractEditor {

    CompositeView node;

    public CompositeEditor(CompositeEntry entry) {
        super(entry);
        node = new CompositeView(entry.getController().getChangeBus(), entry.getController());
    }

    @Override
    public void focus() {}

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    protected boolean validate(String input) {
        return true;
    }

}
