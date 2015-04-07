package net.objectof.actof.minion.testgraph;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import net.objectof.actof.widgets.KeyValuePane;


public class HandlerPanel extends BorderPane {

    public HandlerPanel() {
        KeyValuePane panel = new KeyValuePane();
        panel.put("name", new TextField("Name"));
        panel.put("date", new TextField("Date"));
        panel.put("", new Button("OK"));
        panel.setHgap(3);
        panel.setVgap(3);
        setPadding(new Insets(18));

        setStyle("-fx-effect: dropshadow(gaussian, #000000C0, 4, 0, 0, 1); -fx-background-color: #ffffff; -fx-background-radius: 3px;");

        setContent(panel);

    }

    public void setContent(Node node) {
        setCenter(node);
    }
}
