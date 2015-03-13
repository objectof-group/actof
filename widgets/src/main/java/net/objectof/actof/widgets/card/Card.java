package net.objectof.actof.widgets.card;


import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class Card extends AnchorPane {

    protected BorderPane top, content;
    protected HBox contentBox, titleContentBox;
    protected AnchorPane titleBox, descriptionBox;

    protected int radius = 5;
    protected String colour = "#ffffff";
    private String shadowColour = "#777";
    private int shadowRadius = 8;

    protected Node node;

    public Card() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Card.class.getResource("Card.fxml"));

        try {

            top = (BorderPane) loader.load();
            content = (BorderPane) loader.getNamespace().get("content");
            contentBox = (HBox) loader.getNamespace().get("contentBox");
            titleContentBox = (HBox) loader.getNamespace().get("titleContentBox");

            titleBox = (AnchorPane) loader.getNamespace().get("titleBox");
            descriptionBox = (AnchorPane) loader.getNamespace().get("descriptionBox");
            setTitle("");
            setDescription("");

            content.setStyle("-fx-background-radius: 5px; -fx-background-color: #ffffff; -fx-effect: dropshadow(gaussian, #777, 8, -2, 0, 1)");

            AnchorPane.setTopAnchor(top, 0d);
            AnchorPane.setBottomAnchor(top, 0d);
            AnchorPane.setLeftAnchor(top, 0d);
            AnchorPane.setRightAnchor(top, 0d);
            setPadding(new Insets(6));

            getChildren().add(top);

            focusedProperty().addListener(change -> {
                if (isFocused()) {
                    setShadowColour("#444");
                    setShadowRadius(10);
                } else {
                    setShadowColour("#777");
                    setShadowRadius(8);
                }
            });

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String titleString) {
        if (titleString == null) {
            titleString = "";
        }
        Label label = new Label(titleString);
        label.setStyle("-fx-font-size: 13pt;");
        setTitle(label);

        fixPadding();
    }

    public void setTitle(Node titleNode) {
        titleBox.getChildren().clear();
        if (titleNode == null) { return; }
        AnchorPane.setTopAnchor(titleNode, 0d);
        AnchorPane.setBottomAnchor(titleNode, 0d);
        AnchorPane.setLeftAnchor(titleNode, 0d);
        AnchorPane.setRightAnchor(titleNode, 0d);
        titleBox.getChildren().add(titleNode);
    }

    public Node getTitle() {
        return titleBox.getChildren().get(0);
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        Label label = new Label(description);
        label.setStyle("-fx-text-fill: #777777;");
        setDescription(label);

    }

    public void setDescription(Node descriptionNode) {
        descriptionBox.getChildren().clear();
        if (descriptionNode == null) { return; }
        AnchorPane.setTopAnchor(descriptionNode, 0d);
        AnchorPane.setBottomAnchor(descriptionNode, 0d);
        AnchorPane.setLeftAnchor(descriptionNode, 0d);
        AnchorPane.setRightAnchor(descriptionNode, 0d);
        descriptionBox.getChildren().add(descriptionNode);

        fixPadding();

    }

    public Node getDescription() {
        return descriptionBox.getChildren().get(0);
    }

    public Node getContent() {
        return node;
    }

    public void setContent(Node node) {
        setContent(node, true);
    }

    public void setContent(Node node, boolean expanding) {
        contentBox.getChildren().clear();
        if (node == null) {
            node = null;
            return;
        }
        this.node = node;

        BorderPane.setAlignment(node, Pos.CENTER_LEFT);
        if (expanding) {
            HBox.setHgrow(node, Priority.ALWAYS);
        } else {
            HBox.setHgrow(node, Priority.NEVER);
        }
        contentBox.getChildren().setAll(node);

        fixPadding();

    }

    public void setTitleContent(Node node) {
        titleContentBox.getChildren().clear();
        if (node == null) { return; }
        titleContentBox.getChildren().add(node);
    }

    public Node getTitleContent() {
        return titleContentBox.getChildren().get(0);
    }

    private void fixPadding() {

        if (contentBox.getChildren().size() == 0) {
            titleBox.setPadding(new Insets(0, 10, 0, 0));
            descriptionBox.setPadding(new Insets(0, 0, 0, 0));
            titleContentBox.setPadding(new Insets(0, 0, 0, 0));
        } else {
            titleBox.setPadding(new Insets(0, 10, 6, 0));
            descriptionBox.setPadding(new Insets(0, 0, 6, 0));
            titleContentBox.setPadding(new Insets(0, 0, 6, 0));
        }

    }

    public void setRadius(int radius) {
        this.radius = radius;
        buildStyle();
    }

    public int getRadius() {
        return radius;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
        buildStyle();
    }

    protected void setShadowColour(String colour) {
        this.shadowColour = colour;
        buildStyle();
    }

    protected void setShadowRadius(int rad) {
        this.shadowRadius = rad;
        buildStyle();
    }

    private void buildStyle() {
        String rad = "-fx-background-radius: " + radius + "px; ";
        String col = "-fx-background-color: " + colour + "; ";
        content.setStyle(rad + col + " -fx-effect: dropshadow(gaussian, " + shadowColour + " , " + shadowRadius
                + ", -2, 0, 1)");
    }
}
