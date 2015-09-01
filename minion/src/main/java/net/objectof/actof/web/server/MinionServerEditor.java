package net.objectof.actof.web.server;


import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.editor.impl.AbstractLoadedEditor;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.components.spring.change.HandlerChange;
import net.objectof.actof.widgets.StatusLight;


public class MinionServerEditor extends AbstractLoadedEditor implements ResourceEditor, Display {

    @FXML
    private BorderPane topPane;
    @FXML
    private HBox toolbar;
    @FXML
    private ListView<String> output;
    @FXML
    private Button start, stop;
    @FXML
    private TextField porttext;
    @FXML
    private Label portlabel;
    @FXML
    private VBox topbox;

    private StatusLight statuslight;

    private MinionServer minionServer;
    private MinionServerResource resource;

    private InvalidationListener logListener = (Observable list) -> {
        Platform.runLater(() -> output.getItems().setAll(minionServer.getLog()));
    };

    public MinionServerEditor() {

    }

    @Override
    public void construct() throws Exception {

        getDisplayStage().setOnCloseRequest(event -> stop());

        getChangeBus().listen(HandlerChange.class, minionServer::setHandler);

        statuslight = new StatusLight("Server Off");
        topbox.getChildren().add(statuslight);

        getToolbars().addAll(toolbar.getChildren());
        toolbar.getChildren().clear();
        topbox.getChildren().remove(toolbar);

        dismissedProperty().addListener(event -> stop());

    }

    public void start() {
        minionServer.start();
    }

    public void stop() {
        minionServer.stop();
    }

    public static MinionServerEditor load() throws IOException {
        return FXUtil.loadFX(MinionServerEditor.class, "MinionServerEditor.fxml");
    }

    @Override
    public Display getDisplay() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Server";
    }

    @Override
    public boolean isForResource() {
        return true;
    }

    @Override
    public void setForResource(boolean forResource) {}

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = (MinionServerResource) resource;
    }

    @Override
    public void loadResource() throws Exception {
        minionServer = resource.getServer();

        minionServer.getLog().addListener(logListener);
        minionServer.setChangeBus(getChangeBus());

        minionServer.statusMessageProperty().addListener(
                change -> statuslight.setStatus(minionServer.getStatusLight(), minionServer.getStatusMessage()));
        minionServer.statusLightProperty().addListener(
                change -> statuslight.setStatus(minionServer.getStatusLight(), minionServer.getStatusMessage()));

        start.disableProperty().bind(minionServer.startableProperty().not());
        stop.disableProperty().bind(minionServer.stoppableProperty().not());

    }

    public MinionServer getMinionServer() {
        return minionServer;
    }

    @Override
    protected void onResourceAdded(Resource res) throws Exception {}

}
