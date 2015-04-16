package net.objectof.actof.connectorui.parametereditor;


import javafx.scene.Node;
import net.objectof.connector.Parameter;


public interface ParameterEditor {

    Parameter getParameter();

    Node asNode();

    void setCreate(boolean create);

}
