package net.objectof.actof.minion.components.handlers.graph;


import java.beans.PropertyDescriptor;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import net.objectof.actof.minion.common.minionhandler.MinionHandler;
import net.objectof.actof.minion.common.minionhandler.MinionHandlerColor;
import net.objectof.actof.widgets.KeyValuePane;
import net.objectof.actof.widgets.network.INetworkEdge;
import net.objectof.actof.widgets.network.NetworkEdge;
import net.objectof.actof.widgets.network.NetworkPane;


public class HandlerBody extends BorderPane {

    HBox titleBox;
    NetworkEdge edge;

    public HandlerBody(NetworkPane<MinionHandler> parent, MinionHandler handler) {

        edge = new INetworkEdge(handler, null);
        handler.getEdges().add(edge);

        String cssurl = HandlerBody.class.getResource("style.css").toExternalForm();
        getStylesheets().add(cssurl);

        BorderPane borderpane = new BorderPane();

        KeyValuePane panel = new KeyValuePane();
        panel.put("ref", refBox(parent, handler));
        for (PropertyDescriptor property : handler.getProperties()) {
            panel.put(property.getName(), refBox(parent, handler));
        }
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
        closeButton.setOnAction(event -> parent.getVertices().remove(handler));
        AnchorPane closeAnchor = anchor(closeButton);

        MenuButton colorButton = new MenuButton("\u25BE");
        colorButton.getStyleClass().add("menu-button");
        colorMenu(colorButton, handler);
        AnchorPane colorAnchor = anchor(colorButton);

        AnchorPane titleAnchor = anchor(title);
        HBox.setHgrow(titleAnchor, Priority.ALWAYS);
        titleBox = new HBox(titleAnchor, colorAnchor, closeAnchor);
        title.setPadding(new Insets(0, 0, 0, 28));

        handler.colorProperty().addListener(change -> setColor(handler.getColor()));
        setColor(MinionHandlerColor.BLUE.toFXColor());
        titleBox.setPrefHeight(24);
        borderpane.setTop(titleBox);

        setContent(borderpane);
        setStyle("-fx-effect: dropshadow(gaussian, #000000C0, 4, 0, 0, 1); -fx-background-color: #ffffff; -fx-background-radius: 3px;");

    }

    private ComboBox<MinionHandler> refBox(NetworkPane<MinionHandler> parent, MinionHandler handler) {

        ComboBox<MinionHandler> box = new ComboBox<>(parent.getVertices());

        box.getSelectionModel().selectedItemProperty().addListener(change -> {
            handler.getEdges().remove(edge);
            edge = new INetworkEdge(handler, box.getSelectionModel().getSelectedItem());
            handler.getEdges().add(edge);
        });
        return box;
    }

    private void colorMenu(MenuButton menu, MinionHandler handler) {
        for (MinionHandlerColor color : MinionHandlerColor.values()) {
            Label graphic = new Label("");
            graphic.setBackground(new Background(new BackgroundFill(color.toFXColor(), null, null)));
            graphic.setPrefWidth(24);
            MenuItem item = new MenuItem(color.prettyName(), graphic);
            item.setOnAction(event -> {
                handler.setColor(color.toFXColor());
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
        BackgroundFill fill = new BackgroundFill(color, new CornerRadii(3, 3, 0, 0, false), null);
        titleBox.setBackground(new Background(fill));
    }
}
