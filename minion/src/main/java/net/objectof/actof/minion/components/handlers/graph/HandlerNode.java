package net.objectof.actof.minion.components.handlers.graph;


import java.io.FileNotFoundException;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import net.objectof.actof.minion.classpath.minionhandler.MinionHandler;
import net.objectof.actof.minion.classpath.minionhandler.MinionHandlerColor;
import net.objectof.actof.widgets.network.INetworkVertex;
import net.objectof.actof.widgets.network.NetworkPane;


public class HandlerNode extends INetworkVertex {

    private HandlerIcon badge;
    private HandlerUI mainPanel;
    private NetworkPane parent;
    private MinionHandler handler;

    public HandlerNode(NetworkPane parent, MinionHandler handler) throws FileNotFoundException {

        this.handler = handler;

        setStyle("-fx-background-color: null;");
        setPickOnBounds(false);
        setPrefSize(0, 0);
        this.parent = parent;

        badge = new HandlerIcon(handler, MinionHandlerColor.BLUE.toColor());
        badge.setPickOnBounds(false);

        mainPanel = new HandlerUI(parent, this, handler);

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

    public void remove() {
        parent.getVertices().remove(this);
    }

    public void setContent(Node node) {
        mainPanel.setContent(node);
    }

    public void setColor(Color color) {
        badge.setColor(color);
        mainPanel.setColor(color);
    }

    public MinionHandler getHandler() {
        return handler;
    }

}
