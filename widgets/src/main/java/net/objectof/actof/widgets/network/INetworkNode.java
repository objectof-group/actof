package net.objectof.actof.widgets.network;


import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;


public class INetworkNode extends AnchorPane {

    private Point2D mouseDown = null;
    private NetworkVertex backer = null;

    public INetworkNode(NetworkVertex vertex) {
        this(vertex, false);
    }

    public INetworkNode(NetworkVertex vertex, boolean draggable) {
        backer = vertex;
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
            double dx = backer.getX() - (mouseDown.getX() - event.getX());
            double dy = backer.getY() - (mouseDown.getY() - event.getY());
            backer.setX(dx);
            backer.setY(dy);
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

}
