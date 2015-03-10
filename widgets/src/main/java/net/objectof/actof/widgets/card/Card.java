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

    private BorderPane top, content;
    private HBox contentBox, titleContentBox;
    private AnchorPane titleBox, descriptionBox;

    private Node node;

    public Card() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Card.class.getResource("Card.fxml"));

        try {

            top = (BorderPane) loader.load();
            content = (BorderPane) loader.getNamespace().get("content");
            contentBox = (HBox) loader.getNamespace().get("contentBox");
            titleContentBox = (HBox) loader.getNamespace().get("titleContentBox");

            titleBox = (AnchorPane) loader.getNamespace().get("titleBox");
            setTitle("");

            descriptionBox = (AnchorPane) loader.getNamespace().get("descriptionBox");
            setDescription("");

            content.setStyle("-fx-background-radius: 5px; -fx-background-color: #ffffff; -fx-effect: dropshadow(gaussian, #777, 8, -2, 0, 1)");

            AnchorPane.setTopAnchor(top, 0d);
            AnchorPane.setBottomAnchor(top, 0d);
            AnchorPane.setLeftAnchor(top, 0d);
            AnchorPane.setRightAnchor(top, 0d);

            getChildren().add(top);

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
        AnchorPane.setTopAnchor(titleNode, 0d);
        AnchorPane.setBottomAnchor(titleNode, 0d);
        AnchorPane.setLeftAnchor(titleNode, 0d);
        AnchorPane.setRightAnchor(titleNode, 0d);
        titleBox.getChildren().add(titleNode);
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        Label label = new Label(description);
        label.setStyle("-fx-text-fill: #777777;");
        setDescription(label);

        fixPadding();
    }

    public void setDescription(Node descriptionNode) {
        descriptionBox.getChildren().clear();
        AnchorPane.setTopAnchor(descriptionNode, 0d);
        AnchorPane.setBottomAnchor(descriptionNode, 0d);
        AnchorPane.setLeftAnchor(descriptionNode, 0d);
        AnchorPane.setRightAnchor(descriptionNode, 0d);
        descriptionBox.getChildren().add(descriptionNode);
    }

    public Node getContent() {
        return node;
    }

    public void setContent(Node node) {
        setContent(node, true);
    }

    public void setContent(Node node, boolean expanding) {
        if (node == null) {
            contentBox.getChildren().clear();
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
        titleContentBox.getChildren().add(node);
    }

    private void fixPadding() {
        if (contentBox.getChildren().size() == 0) {
            titleBox.setPadding(new Insets(0, 10, 0, 0));
        } else {
            titleBox.setPadding(new Insets(0, 10, 6, 0));
        }
    }

}
