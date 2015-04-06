package net.objectof.actof.widgets.network;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class INetworkEdge implements NetworkEdge {

    private NetworkVertex from, to;

    private DoubleProperty sourceOffsetX = new SimpleDoubleProperty(0);
    private DoubleProperty sourceOffsetY = new SimpleDoubleProperty(0);
    private DoubleProperty destOffsetX = new SimpleDoubleProperty(0);
    private DoubleProperty destOffsetY = new SimpleDoubleProperty(0);

    public INetworkEdge(NetworkVertex from, NetworkVertex to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public NetworkVertex sourceVertex() {
        return from;
    }

    @Override
    public NetworkVertex destVertex() {
        return to;
    }

    @Override
    public final DoubleProperty sourceOffsetXProperty() {
        return this.sourceOffsetX;
    }

    @Override
    public final double getSourceOffsetX() {
        return this.sourceOffsetXProperty().get();
    }

    @Override
    public final void setSourceOffsetX(final double sourceOffsetX) {
        this.sourceOffsetXProperty().set(sourceOffsetX);
    }

    @Override
    public final DoubleProperty sourceOffsetYProperty() {
        return this.sourceOffsetY;
    }

    @Override
    public final double getSourceOffsetY() {
        return this.sourceOffsetYProperty().get();
    }

    @Override
    public final void setSourceOffsetY(final double sourceOffsetY) {
        this.sourceOffsetYProperty().set(sourceOffsetY);
    }

    @Override
    public final DoubleProperty destOffsetXProperty() {
        return this.destOffsetX;
    }

    @Override
    public final double getDestOffsetX() {
        return this.destOffsetXProperty().get();
    }

    @Override
    public final void setDestOffsetX(final double destOffsetX) {
        this.destOffsetXProperty().set(destOffsetX);
    }

    @Override
    public final DoubleProperty destOffsetYProperty() {
        return this.destOffsetY;
    }

    @Override
    public final double getDestOffsetY() {
        return this.destOffsetYProperty().get();
    }

    @Override
    public final void setDestOffsetY(final double destOffsetY) {
        this.destOffsetYProperty().set(destOffsetY);
    }

    // @Override
    // public boolean equals(Object o) {
    // if (!(o instanceof NetworkEdge)) { return false; }
    // NetworkEdge other = (NetworkEdge) o;
    // return from.equals(other.from()) && to.equals(other.to());
    // }
    //
    // @Override
    // public int hashCode() {
    // return from.hashCode() + to.hashCode();
    // }
}
