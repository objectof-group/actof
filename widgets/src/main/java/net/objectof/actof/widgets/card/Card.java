package net.objectof.actof.widgets.card;


import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class Card extends BlankCard {

    private HBox titleContentBox;
    private AnchorPane titleBox, descriptionBox;

    protected Node node;

    public Card() {

        titleContentBox = new HBox();
        titleBox = new AnchorPane();
        descriptionBox = new AnchorPane();
        Separator sep = new Separator(Orientation.HORIZONTAL);

        HBox.setHgrow(sep, Priority.ALWAYS);
        sep.setVisible(false);
        HBox top = new HBox(titleBox, titleContentBox, sep, descriptionBox);
        card.setTop(top);

        setTitle("");
        setDescription("");

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
        card.setCenter(null);
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
        card.setCenter(node);

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

        if (card.getCenter() == null) {
            titleBox.setPadding(new Insets(0, 10, 0, 0));
            descriptionBox.setPadding(new Insets(0, 0, 0, 0));
            titleContentBox.setPadding(new Insets(0, 0, 0, 0));
        } else {
            titleBox.setPadding(new Insets(0, 10, 6, 0));
            descriptionBox.setPadding(new Insets(0, 0, 6, 0));
            titleContentBox.setPadding(new Insets(0, 0, 6, 0));
        }

    }
}
