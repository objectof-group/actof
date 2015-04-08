package net.objectof.actof.minion.components.handlers.graph;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import net.objectof.actof.minion.classpath.MinionHandler;
import net.objectof.actof.widgets.network.INetworkVertex;


public class HandlerNode extends INetworkVertex {

    private HandlerIcon badge;
    private HandlerUI mainPanel;

    public HandlerNode(MinionHandler handler) throws FileNotFoundException {

        setStyle("-fx-background-color: null;");
        setPickOnBounds(false);
        setPrefSize(0, 0);

        // Image img = new
        // Image(HandlerNode.class.getResourceAsStream("icons/generic-24.png"));
        Image img = new Image(new FileInputStream(handler.getIcon()));
        badge = new HandlerIcon(img, HandlerIconColor.BLUE.toColor());
        badge.setPickOnBounds(false);

        mainPanel = new HandlerUI(this, handler);

        Pane pane = new Pane(badge);
        pane.setStyle("-fx-background-color: null;");
        pane.setPickOnBounds(false);
        pane.setPrefSize(0, 0);

        makeHandle(badge);

        badge.setOnMouseClicked(event -> {
            if (!event.isStillSincePress() || event.getClickCount() != 2) { return; }

            if (pane.getChildren().contains(mainPanel)) {
                pane.getChildren().setAll(badge);
                setWidth(badge.getWidth());
                setHeight(badge.getHeight());
            } else {
                pane.getChildren().setAll(mainPanel, badge);

            }
        });

        super.setContent(pane);

    }

    public void setContent(Node node) {
        mainPanel.setContent(node);
    }
}
