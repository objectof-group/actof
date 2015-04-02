package net.objectof.actof.widgets.network;


import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;


public class NetworkNode extends AnchorPane {

    private ObservableSet<NetworkEdge<NetworkNode>> edges = FXCollections.observableSet(new HashSet<>());
    private Coordinate position = new Coordinate(0, 0);
    private Coordinate mouseDown = null;

    public NetworkNode() {

        setFocusTraversable(true);
        focusedProperty().addListener(change -> {
            if (isFocused()) {
                requestFocus();
                toFront();
            }
        });

        setStyle("-fx-effect: dropshadow(gaussian, #000000, 5, -2, 0, 1); -fx-background-color: -fx-background; -fx-background-radius: 5px;");

        setOnMousePressed(event -> {
            requestFocus();
            mouseDown = new Coordinate(event.getX(), event.getY());
        });

        setOnMouseReleased(event -> {
            mouseDown = null;
        });

        setOnMouseDragged(event -> {
            if (mouseDown == null) { return; }
            double dx = position.getX() - (mouseDown.getX() - event.getX());
            double dy = position.getY() - (mouseDown.getY() - event.getY());
            position.setX(dx);
            position.setY(dy);
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

    public Coordinate getPosition() {
        return position;
    }

    public ObservableSet<NetworkEdge<NetworkNode>> getEdges() {
        return edges;
    }

}
