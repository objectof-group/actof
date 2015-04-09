package net.objectof.actof.widgets.network;


import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableSet;


public interface NetworkVertex {

    ObservableSet<NetworkEdge> getEdges();

    double getX();

    void setX(double x);

    DoubleProperty xProperty();

    double getY();

    void setY(double y);

    DoubleProperty yProperty();

}
