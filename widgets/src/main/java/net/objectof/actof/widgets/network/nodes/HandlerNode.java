package net.objectof.actof.widgets.network.nodes;


import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import net.objectof.actof.widgets.network.INetworkVertex;


public class HandlerNode extends INetworkVertex {

    private WindowBar windowBar = new WindowBar();

    public HandlerNode() {

        windowBar.setTitle("Title");

        BorderPane pane = new BorderPane();
        pane.setTop(windowBar);

        ListView<String> list = new ListView<>();
        pane.setCenter(list);
        list.setPrefSize(100, 100);

        setStyle("-fx-effect: dropshadow(gaussian, #000000, 5, -2, 0, 1); -fx-background-color: -fx-background; -fx-background-radius: 5px;");

        setContent(pane);

    }
}
