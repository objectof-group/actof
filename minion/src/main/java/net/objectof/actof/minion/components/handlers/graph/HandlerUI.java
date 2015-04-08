package net.objectof.actof.minion.components.handlers.graph;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import net.objectof.actof.minion.classpath.MinionHandler;
import net.objectof.actof.widgets.KeyValuePane;


public class HandlerUI extends BorderPane {

    public HandlerUI(HandlerNode node, MinionHandler handler) {

        BorderPane borderpane = new BorderPane();

        KeyValuePane panel = new KeyValuePane();
        panel.put("name", new TextField("Name"));
        panel.put("date", new TextField("Date"));
        panel.put("", new Button("OK"));
        panel.setHgap(3);
        panel.setVgap(3);
        panel.setPadding(new Insets(18));
        borderpane.setCenter(panel);

        Label title = new Label(handler.getTitle());
        title.setTextFill(Color.WHITE);
        title.setPrefHeight(24);
        title.setPadding(new Insets(0, 0, 0, 28));
        AnchorPane titleAnchor = new AnchorPane(title);
        node.makeHandle(titleAnchor);
        titleAnchor.setBackground(new Background(new BackgroundFill(HandlerIconColor.BLUE.toColor(), new CornerRadii(3,
                3, 0, 0, false), new Insets(0))));
        borderpane.setTop(titleAnchor);

        setContent(borderpane);
        setStyle("-fx-effect: dropshadow(gaussian, #000000C0, 4, 0, 0, 1); -fx-background-color: #ffffff; -fx-background-radius: 3px;");

    }

    public void setContent(Node node) {
        setCenter(node);
    }
}
