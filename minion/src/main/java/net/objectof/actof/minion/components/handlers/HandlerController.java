package net.objectof.actof.minion.components.handlers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.classpath.minionhandler.MinionHandler;
import net.objectof.actof.minion.classpath.sources.MinionSource;
import net.objectof.actof.minion.components.classpath.change.ClasspathChange;
import net.objectof.actof.minion.components.handlers.graph.HandlerNode;
import net.objectof.actof.minion.components.handlers.ui.HandlerCell;
import net.objectof.actof.widgets.network.NetworkPane;
import net.objectof.actof.widgets.network.edgestyles.CubicEdgeStyle;


public class HandlerController extends IActofUIController {

    @FXML
    ListView<MinionHandler> handlers;

    @FXML
    private BorderPane top;

    @Override
    @FXML
    protected void initialize() throws IOException, URISyntaxException {

        NetworkPane<MinionHandler> network = new NetworkPane<>();
        network.setNodeFunction(handler -> new HandlerNode(handler, network));
        network.setEdgeStyle(new CubicEdgeStyle());
        top.setCenter(network);

        handlers.setCellFactory(listview -> new HandlerCell());
        handlers.setOnMouseClicked(event -> {
            if (event.getClickCount() != 2) { return; }
            MinionHandler handler = new MinionHandler(handlers.getSelectionModel().getSelectedItem());
            network.getVertices().add(handler);
        });

    }

    @Override
    public void ready() {
        getChangeBus().listen(ClasspathChange.class, this::setHandlers);
    }

    private void setHandlers(ClasspathChange change) {
        try {
            _setHandlers(change);
        }
        catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void _setHandlers(ClasspathChange change) throws URISyntaxException, MalformedURLException {

        handlers.getItems().clear();

        for (MinionSource source : change.getClasspath()) {
            for (MinionHandler handler : source.getHandlers()) {
                handlers.getItems().add(handler);
            }
        }

    }

    public static HandlerController load(ChangeController changes) throws IOException {
        return FXUtil.load(HandlerController.class, "Handlers.fxml", changes);
    }

}
