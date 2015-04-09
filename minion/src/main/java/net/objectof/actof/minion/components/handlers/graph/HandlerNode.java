package net.objectof.actof.minion.components.handlers.graph;


import javafx.scene.layout.Pane;
import net.objectof.actof.minion.common.minionhandler.MinionHandler;
import net.objectof.actof.widgets.network.INetworkNode;
import net.objectof.actof.widgets.network.NetworkPane;


public class HandlerNode extends INetworkNode {

    private HandlerIcon badge;
    private HandlerBody mainPanel;
    private NetworkPane parent;
    private MinionHandler handler;

    public HandlerNode(MinionHandler handler, NetworkPane<MinionHandler> parent) {
        super(handler);

        this.handler = handler;
        this.parent = parent;

        setStyle("-fx-background-color: null;");
        setPickOnBounds(false);
        setPrefSize(0, 0);

        badge = new HandlerIcon(handler);
        badge.setPickOnBounds(false);

        mainPanel = new HandlerBody(parent, handler);

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

    public MinionHandler getHandler() {
        return handler;
    }

}
