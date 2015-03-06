package net.objectof.actof.widgets.card;


import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class Card extends AnchorPane {

    private BorderPane top, content;
    private Label title, description;
    private HBox contentBox;

    private Node node;

    public Card() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Card.class.getResource("Card.fxml"));

        try {

            top = (BorderPane) loader.load();
            content = (BorderPane) loader.getNamespace().get("content");
            contentBox = (HBox) loader.getNamespace().get("contentBox");

            title = (Label) loader.getNamespace().get("title");
            setTitle("");

            description = (Label) loader.getNamespace().get("stereotype");
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

    public String getTitle() {
        return title.getText();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public String getDescription() {
        return description.getText();
    }

    public void setDescription(String description) {
        this.description.setText(description);
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
    }

}
