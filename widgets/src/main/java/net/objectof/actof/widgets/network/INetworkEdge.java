package net.objectof.actof.widgets.network;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;


public class INetworkEdge implements NetworkEdge {

    private ObjectProperty<NetworkVertex> sourceVertex, destinationVertex;

    private DoubleProperty sourceOffsetX = new SimpleDoubleProperty(0);
    private DoubleProperty sourceOffsetY = new SimpleDoubleProperty(0);
    private DoubleProperty destOffsetX = new SimpleDoubleProperty(0);
    private DoubleProperty destOffsetY = new SimpleDoubleProperty(0);

    private ObjectProperty<Color> color = new SimpleObjectProperty<Color>(Color.BLACK);
    private DoubleProperty width = new SimpleDoubleProperty(2);

    public INetworkEdge(NetworkVertex source, NetworkVertex destination) {
        this.sourceVertex = new SimpleObjectProperty<>(source);
        this.destinationVertex = new SimpleObjectProperty<>(destination);
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

    @Override
    public final ObjectProperty<Color> colorProperty() {
        return this.color;
    }

    @Override
    public final Color getColor() {
        return this.colorProperty().get();
    }

    @Override
    public final void setColor(final Color color) {
        this.colorProperty().set(color);
    }

    @Override
    public final DoubleProperty widthProperty() {
        return this.width;
    }

    @Override
    public final double getWidth() {
        return this.widthProperty().get();
    }

    @Override
    public final void setWidth(final double width) {
        this.widthProperty().set(width);
    }

    @Override
    public final ObjectProperty<NetworkVertex> sourceVertexProperty() {
        return this.sourceVertex;
    }

    @Override
    public final NetworkVertex getSourceVertex() {
        return this.sourceVertexProperty().get();
    }

    @Override
    public final void setSourceVertex(final NetworkVertex sourceVertex) {
        this.sourceVertexProperty().set(sourceVertex);
    }

    @Override
    public final ObjectProperty<NetworkVertex> destinationVertexProperty() {
        return this.destinationVertex;
    }

    @Override
    public final NetworkVertex getDestinationVertex() {
        return this.destinationVertexProperty().get();
    }

    @Override
    public final void setDestinationVertex(final NetworkVertex destinationVertex) {
        this.destinationVertexProperty().set(destinationVertex);
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
