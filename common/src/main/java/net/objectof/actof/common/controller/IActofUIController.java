package net.objectof.actof.common.controller;


import javafx.fxml.FXML;
import javafx.scene.Node;
import net.objectof.actof.common.controller.change.ChangeController;


public abstract class IActofUIController implements ActofUIController {

    private Node node;
    private ChangeController changes;

    @Override
    public final ChangeController getChangeBus() {
        return changes;
    }

    @Override
    public final void setChangeBus(ChangeController changes) {
        if (this.changes != null) { throw new IllegalArgumentException("Cannot reassign ChangeController"); }
        this.changes = changes;
    }

    @Override
    public final Node getNode() {
        return node;
    }

    @Override
    public final void setNode(Node node) {
        this.node = node;
    }

    @Override
    public abstract void ready();

    @FXML
    protected abstract void initialize() throws Exception;

}
