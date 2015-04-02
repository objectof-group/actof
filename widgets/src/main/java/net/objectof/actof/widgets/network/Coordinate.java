package net.objectof.actof.widgets.network;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class Coordinate {

    private DoubleProperty x = new SimpleDoubleProperty(0);
    private DoubleProperty y = new SimpleDoubleProperty(0);

    public Coordinate() {}

    public Coordinate(double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }

    public void set(double newX, double newY) {
        x.set(newX);
        y.set(newY);
    }

    public void setX(double newX) {
        x.set(newX);
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setY(double newY) {
        y.set(newY);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

}
