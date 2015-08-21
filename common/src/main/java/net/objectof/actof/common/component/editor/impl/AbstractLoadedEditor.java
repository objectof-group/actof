package net.objectof.actof.common.component.editor.impl;


import javafx.scene.Node;
import net.objectof.actof.common.component.feature.FXLoaded;
import net.objectof.actof.common.component.feature.FXNoded;


public abstract class AbstractLoadedEditor extends AbstractEditor implements FXNoded, FXLoaded {

    private Node node;

    @Override
    public void setFXNode(Node node) {
        this.node = node;
    }

    @Override
    public Node getFXNode() {
        return node;
    }

}
