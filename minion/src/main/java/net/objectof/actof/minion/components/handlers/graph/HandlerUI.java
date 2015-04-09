package net.objectof.actof.minion.components.handlers.graph;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import net.objectof.actof.minion.classpath.MinionHandler;
import net.objectof.actof.minion.components.handlers.style.HandlerColor;
import net.objectof.actof.widgets.KeyValuePane;


public class HandlerUI extends BorderPane {

    HBox titleBox;

    public HandlerUI(HandlerNode node, MinionHandler handler) {

        String cssurl = HandlerUI.class.getResource("style.css").toExternalForm();
        getStylesheets().add(cssurl);

        BorderPane borderpane = new BorderPane();

        KeyValuePane panel = new KeyValuePane();
        panel.put("name", new TextField("Name"));
        panel.put("date", new TextField("Date"));
        panel.put("", new Button("OK"));
        panel.setHgap(3);
        panel.setVgap(3);
        panel.setPadding(new Insets(6));
        borderpane.setCenter(panel);

        TextField title = new TextField(handler.getTitle());
        title.setStyle("-fx-border: none; -fx-background-color: null; -fx-text-fill: white;");
        title.setPadding(new Insets(0));
        title.textProperty().addListener(change -> handler.setTitle(title.getText()));

        Button closeButton = new Button("\u2716");
        closeButton.setStyle("-fx-padding: 3px 6px 3px 6px; -fx-text-fill: #ffffff; -fx-background-color: null");
        closeButton.setOnAction(event -> node.remove());
        AnchorPane closeAnchor = anchor(closeButton);

        MenuButton colorButton = new MenuButton("\u25BE");
        colorButton.getStyleClass().add("menu-button");
        colorMenu(colorButton, node);
        AnchorPane colorAnchor = anchor(colorButton);

        AnchorPane titleAnchor = anchor(title);
        HBox.setHgrow(titleAnchor, Priority.ALWAYS);
        titleBox = new HBox(titleAnchor, colorAnchor, closeAnchor);
        title.setPadding(new Insets(0, 0, 0, 28));

        setColor(HandlerColor.BLUE.toColor());
        titleBox.setPrefHeight(24);
        borderpane.setTop(titleBox);

        setContent(borderpane);
        setStyle("-fx-effect: dropshadow(gaussian, #000000C0, 4, 0, 0, 1); -fx-background-color: #ffffff; -fx-background-radius: 3px;");

    }

    private void colorMenu(MenuButton menu, HandlerNode node) {
        for (HandlerColor color : HandlerColor.values()) {
            Label graphic = new Label("");
            graphic.setBackground(new Background(new BackgroundFill(color.toColor(), null, null)));
            graphic.setPrefWidth(24);
            MenuItem item = new MenuItem(color.prettyName(), graphic);
            item.setOnAction(event -> {
                node.setColor(color.toColor());
            });
            menu.getItems().add(item);
        }
    }

    private AnchorPane anchor(Node node) {

        AnchorPane titleAnchor = new AnchorPane();
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        titleAnchor.getChildren().add(node);
        return titleAnchor;
    }

    public void setContent(Node node) {
        setCenter(node);
    }

    public void setColor(Color color) {
        titleBox.setBackground(new Background(new BackgroundFill(color, new CornerRadii(3, 3, 0, 0, false), null)));
    }
}
