package net.objectof.actof.common.component.display.impl;


import javafx.scene.Node;
import net.objectof.actof.common.component.feature.FXLoaded;


public abstract class AbstractLoadedDisplay extends AbstractDisplay implements FXLoaded {

    private Node displayNode;

    @Override
    public Node getFXNode() {
        return displayNode;
    }

    @Override
    public void setFXNode(Node node) {
        this.displayNode = node;
    }

}
