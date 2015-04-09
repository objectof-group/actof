package net.objectof.actof.widgets.network;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;


public interface NetworkEdge {

    default void setDestOffset(final double destOffsetX, final double destOffsetY) {
        setDestOffsetX(destOffsetX);
        setDestOffsetY(destOffsetY);
    }

    default void setSourceOffset(final double sourceOffsetX, final double sourceOffsetY) {
        setSourceOffsetX(sourceOffsetX);
        setSourceOffsetY(sourceOffsetY);
    }

    void setDestOffsetY(final double destOffsetY);

    double getDestOffsetY();

    DoubleProperty destOffsetYProperty();

    void setDestOffsetX(final double destOffsetX);

    double getDestOffsetX();

    DoubleProperty destOffsetXProperty();

    void setSourceOffsetY(final double sourceOffsetY);

    double getSourceOffsetY();

    DoubleProperty sourceOffsetYProperty();

    void setSourceOffsetX(final double sourceOffsetX);

    double getSourceOffsetX();

    DoubleProperty sourceOffsetXProperty();

    void setColor(final Color color);

    Color getColor();

    ObjectProperty<Color> colorProperty();

    void setWidth(final double width);

    double getWidth();

    DoubleProperty widthProperty();

    NetworkVertex getDestinationVertex();

    NetworkVertex getSourceVertex();

}
