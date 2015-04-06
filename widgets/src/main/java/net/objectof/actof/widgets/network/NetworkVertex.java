package net.objectof.actof.widgets.network;


import java.util.Set;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;


public interface NetworkVertex {

    Set<NetworkEdge> getEdges();

    Node getFXNode();

    double getX();

    void setX(double x);

    DoubleProperty xProperty();

    double getY();

    void setY(double y);

    DoubleProperty yProperty();

}
