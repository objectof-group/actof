package net.objectof.actof.web.server;


import java.io.IOException;
import java.util.Optional;

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
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.component.resource.impl.IAction;
import net.objectof.actof.common.component.resource.impl.TransientResource;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.web.app.change.HandlerChange;
import net.objectof.actof.web.client.WebClient;
import net.objectof.actof.widgets.StatusLight;


public class WebServerEditor extends AbstractLoadedEditor implements ResourceEditor, Display {

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

    private WebServer minionServer;
    private WebServerResource resource;

    private Action actionShowClient = new IAction("REST Client", () -> {
        try {
            WebClient client = WebClient.load();
            return Optional.of(new TransientResource(client));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    });

    private InvalidationListener logListener = (Observable list) -> {
        Platform.runLater(() -> output.getItems().setAll(minionServer.getLog()));
    };

    public WebServerEditor() {

    }

    @Override
    public void construct() throws Exception {

        setTitle("Web Server");

        getDisplayStage().setOnCloseRequest(event -> stop());

        getChangeBus().listen(HandlerChange.class, minionServer::setHandler);

        statuslight = new StatusLight("Server Off");
        topbox.getChildren().add(statuslight);

        getToolbars().addAll(toolbar.getChildren());
        toolbar.getChildren().clear();
        topbox.getChildren().remove(toolbar);

        dismissedProperty().addListener(event -> stop());

        getActions().add(actionShowClient);

    }

    public void start() {
        minionServer.start();
    }

    public void stop() {
        minionServer.stop();
    }

    public static WebServerEditor load() throws IOException {
        return FXUtil.loadFX(WebServerEditor.class, "WebServerEditor.fxml");
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
    public Resource getTargetResource() {
        return resource;
    }

    @Override
    public void setTargetResource(Resource resource) {
        this.resource = (WebServerResource) resource;
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

    public WebServer getMinionServer() {
        return minionServer;
    }

    @Override
    protected void onResourceAdded(Resource res) throws Exception {}

}
