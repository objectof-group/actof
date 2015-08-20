package net.objectof.actof.common.component.display.impl;


import javafx.scene.Node;
import net.objectof.actof.common.component.display.LoadedDisplay;


public abstract class AbstractLoadedDisplay extends AbstractDisplay implements LoadedDisplay {

    private Node displayNode;

    @Override
    public Node getDisplayNode() {
        return displayNode;
    }

    @Override
    public void setDisplayNode(Node node) {
        this.displayNode = node;
    }

}
