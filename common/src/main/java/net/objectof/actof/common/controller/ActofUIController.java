package net.objectof.actof.common.controller;


import javafx.scene.Node;
import net.objectof.actof.common.controller.change.ChangeController;


public interface ActofUIController extends ActofController {

    Node getNode();

    void setNode(Node node);

    void setChangeBus(ChangeController change);

    void ready();

}
