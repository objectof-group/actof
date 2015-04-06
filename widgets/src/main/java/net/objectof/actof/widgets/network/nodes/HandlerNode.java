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

        setContent(pane);

    }
}
