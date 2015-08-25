package net.objectof.actof.minion.components.server;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.component.LifeCycle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.impl.AbstractLoadedDisplay;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.components.server.change.ServerStartChange;
import net.objectof.actof.minion.components.server.change.ServerStopChange;
import net.objectof.actof.minion.components.spring.change.HandlerChange;
import net.objectof.actof.widgets.StatusLight;
import net.objectof.actof.widgets.StatusLight.Status;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.corc.web.v2.impl.IHttpRequest;


public class ServerController extends AbstractLoadedDisplay implements ResourceEditor, Resource {

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

    // Jetty Server Components
    private Server server;
    private ContextHandler context;
    private SessionHandler sessions;
    private int counter = 0;
    private AbstractHandler handler;

    private StatusLight statuslight;

    private int port = 8080;

    @Override
    public void construct() {

        statuslight = new StatusLight("Server Off");
        topbox.getChildren().add(statuslight);

        stop.setDisable(true);
        start.setDisable(true);
        porttext.setDisable(true);
        portlabel.setDisable(true);

        getToolbars().addAll(toolbar.getChildren());
        toolbar.getChildren().clear();
        topbox.getChildren().remove(toolbar);

        getChangeBus().listen(HandlerChange.class, this::setHandler);
    }

    private void initServer() {
        server = new Server(port);

        HashSessionIdManager idmanager = new HashSessionIdManager();
        server.setSessionIdManager(idmanager);

        context = new ContextHandler("/");
        server.setHandler(context);

        HashSessionManager manager = new HashSessionManager();
        sessions = new SessionHandler(manager);
        context.setHandler(sessions);

        if (handler != null) {
            sessions.setHandler(handler);
        }

        server.addLifeCycleListener(new LifeCycle.Listener() {

            @Override
            public void lifeCycleStarting(LifeCycle event) {
                // TODO Auto-generated method stub

            }

            @Override
            public void lifeCycleStarted(LifeCycle event) {
                start.setDisable(true);
                stop.setDisable(false);
            }

            @Override
            public void lifeCycleFailure(LifeCycle event, Throwable cause) {
                // TODO Auto-generated method stub

            }

            @Override
            public void lifeCycleStopping(LifeCycle event) {
                // TODO Auto-generated method stub

            }

            @Override
            public void lifeCycleStopped(LifeCycle event) {
                start.setDisable(false);
                stop.setDisable(true);
            }
        });
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setHandler(Handler<HttpRequest> handler) {

        setHandler(new AbstractHandler() {

            @Override
            public void handle(String target, Request base, HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException {

                addOutput(request.getMethod() + ": " + request.getPathInfo(), true);

                IHttpRequest req = new IHttpRequest("" + counter++, this, request, response);
                handler.execute(req, req);
                base.setHandled(true);
            }
        });
    }

    public void setHandler(AbstractHandler handler) {

        start.setDisable(false);
        porttext.setDisable(false);
        portlabel.setDisable(false);

        this.handler = handler;
    }

    public void setHandler(HandlerChange change) {
        setHandler(change.getHandler());
    }

    private void addOutput(String message) {
        addOutput(message, false);
    }

    private void addOutput(String message, boolean crossthread) {

        Runnable r = () -> output.getItems().add(message);

        if (crossthread) {
            Platform.runLater(r);
        } else {
            r.run();
        }
    }

    public void stop() {
        try {
            statuslight.setStatus(Status.OFF, "Server Off");
            if (server != null) {
                if (server.isRunning()) {
                    addOutput("Shut Down Jetty Server");
                }
                server.stop();
            }
            getChangeBus().broadcast(new ServerStopChange());
        }
        catch (Exception e) {
            statuslight.setStatus(Status.BAD, e.getMessage());
        }
    }

    public void start() {
        output.getItems().clear();
        try {
            port = Integer.parseInt(porttext.getText());
            initServer();
            server.start();
            addOutput("Started Jetty Server on Port " + port);
            String path = "http://localhost:" + port + "/";
            statuslight.setStatus(Status.GOOD, "Server On: " + path);
            getChangeBus().broadcast(new ServerStartChange(path));
        }
        catch (Exception e) {
            statuslight.setStatus(Status.BAD, e.getMessage());
            e.printStackTrace();
        }
    }

    public static ServerController load() throws IOException {
        return FXUtil.loadFX(ServerController.class, "Server.fxml");
    }

    @Override
    public Display getDisplay() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Repository REST Server";
    }

    @Override
    public ObservableList<Action> getActions() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<Resource> getResources() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public boolean isForResource() {
        return true;
    }

    @Override
    public void setForResource(boolean forResource) {}

    @Override
    public Resource getResource() {
        return this;
    }

    @Override
    public void setResource(Resource resource) {}

    @Override
    public void loadResource() throws Exception {}

    @Override
    public ResourceEditor getEditor() throws Exception {
        return this;
    }

    @Override
    public ResourceEditor createDisplay() throws Exception {
        return this;
    }

}
