package net.objectof.actof.widgets.network;


import java.util.HashSet;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;


public class INetworkVertex implements NetworkVertex {

    private ObservableSet<NetworkEdge> edges = FXCollections.observableSet(new HashSet<>());

    private DoubleProperty xPos = new SimpleDoubleProperty(0);
    private DoubleProperty yPos = new SimpleDoubleProperty(0);

    public INetworkVertex() {}

    public ObservableSet<NetworkEdge> getEdges() {
        return edges;
    }

    @Override
    public double getX() {
        return xPos.get();
    }

    @Override
    public void setX(double x) {
        xPos.set(x);
    }

    @Override
    public DoubleProperty xProperty() {
        return xPos;
    }

    @Override
    public double getY() {
        return yPos.get();
    }

    @Override
    public void setY(double y) {
        yPos.set(y);
    }

    @Override
    public DoubleProperty yProperty() {
        return yPos;
    }

}
