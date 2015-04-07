package net.objectof.actof.minion.testgraph;


import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import net.objectof.actof.widgets.network.INetworkVertex;


public class HandlerNode extends INetworkVertex {

    private IconBadge badge;
    private HandlerPanel mainPanel;

    public HandlerNode() {

        setStyle("-fx-background-color: null;");
        setPickOnBounds(false);
        setPrefSize(0, 0);

        Image img = new Image(HandlerNode.class.getResourceAsStream("icons/generic-24.png"));
        badge = new IconBadge(img, BadgeColor.BLUE.toColor());
        badge.setPickOnBounds(false);

        mainPanel = new HandlerPanel();

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
