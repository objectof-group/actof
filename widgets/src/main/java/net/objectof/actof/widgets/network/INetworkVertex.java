package net.objectof.actof.widgets.network;


import java.util.HashSet;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;


public class INetworkVertex extends AnchorPane implements NetworkVertex {

    private DoubleProperty xPos = new SimpleDoubleProperty(0);
    private DoubleProperty yPos = new SimpleDoubleProperty(0);

    private ObservableSet<NetworkEdge> edges = FXCollections.observableSet(new HashSet<>());
    private Point2D mouseDown = null;

    public INetworkVertex() {
        this(false);
    }

    public INetworkVertex(boolean draggable) {

        setFocusTraversable(true);
        focusedProperty().addListener(change -> {
            if (isFocused()) {
                requestFocus();
                toFront();
            }
        });

        setOnMousePressed(event -> {
            requestFocus();
        });

        if (draggable) {
            makeHandle(this);
        }
    }

    public void makeHandle(Node node) {

        node.setOnMousePressed(event -> {
            requestFocus();
            mouseDown = new Point2D(event.getX(), event.getY());
        });

        node.setOnMouseReleased(event -> {
            mouseDown = null;
        });

        node.setOnMouseDragged(event -> {
            if (mouseDown == null) { return; }
            double dx = xPos.get() - (mouseDown.getX() - event.getX());
            double dy = yPos.get() - (mouseDown.getY() - event.getY());
            xPos.set(dx);
            yPos.set(dy);
        });
    }

    public void setContent(Node node) {
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        getChildren().clear();
        getChildren().add(node);
    }

    public ObservableSet<NetworkEdge> getEdges() {
        return edges;
    }

    @Override
    public Node getFXNode() {
        return this;
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
