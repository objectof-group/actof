package net.objectof.actof.repospy.controllers.navigator.composite.viewers;


import javafx.scene.Node;
import javafx.scene.control.Label;
import net.objectof.actof.repospy.controllers.navigator.composite.CompositeEntry;


public class SimpleViewer implements Viewer {

    private CompositeEntry entry;

    public SimpleViewer(CompositeEntry entry) {
        this.entry = entry;
    }

    @Override
    public Node getNode() {
        return new Label(entry.toString());
    }

    protected CompositeEntry getEntry() {
        return entry;
    }

}
