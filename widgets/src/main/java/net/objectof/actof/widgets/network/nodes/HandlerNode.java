package net.objectof.actof.widgets.network.nodes;


import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import net.objectof.actof.widgets.network.NetworkNode;


public class HandlerNode extends NetworkNode {

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
