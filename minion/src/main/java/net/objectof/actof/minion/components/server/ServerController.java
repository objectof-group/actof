package net.objectof.actof.minion.components.server;


import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.objectof.actof.common.controller.IActofUIController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.minion.components.server.change.ServerStartChange;
import net.objectof.actof.minion.components.server.change.ServerStopChange;
import net.objectof.actof.minion.components.spring.change.BeansChange;
import net.objectof.actof.widgets.StatusLight;
import net.objectof.actof.widgets.StatusLight.Status;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.corc.web.v2.impl.IHttpRequest;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.component.LifeCycle;


public class ServerController extends IActofUIController {

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
    private int counter = 0;
    private Handler<HttpRequest> handler;

    private StatusLight statuslight;

    private int port = 8080;

    @Override
    @FXML
    protected void initialize() {

        statuslight = new StatusLight("Server Off");
        topbox.getChildren().add(statuslight);

        stop.setDisable(true);
        start.setDisable(true);
        porttext.setDisable(true);
        portlabel.setDisable(true);
    }

    @Override
    public void ready() {
        getChangeBus().listen(BeansChange.class, beansChange -> {
            Handler<HttpRequest> handler = (Handler<HttpRequest>) beansChange.getRoot();
            setHandler(handler);
        });
    }

    private void initServer() {
        server = new Server(port);

        HashSessionIdManager idmanager = new HashSessionIdManager();
        server.setSessionIdManager(idmanager);

        ContextHandler context = new ContextHandler("/");
        server.setHandler(context);

        // Create the SessionHandler (wrapper) to handle the sessions
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessions = new SessionHandler(manager);
        context.setHandler(sessions);

        sessions.setHandler(new AbstractHandler() {

            @Override
            public void handle(String target, Request base, HttpServletRequest request, HttpServletResponse response)
                    throws IOException, ServletException {

                addOutput(request.getMethod() + ": " + request.getPathInfo(), true);

                IHttpRequest req = new IHttpRequest("" + counter++, this, request, response);
                handler.execute(req, req);
                base.setHandled(true);
            }
        });

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

        if (handler == null) {
            // if the new handler is null, shut down the server
            stop();
            start.setDisable(true);
            stop.setDisable(true);
            porttext.setDisable(true);
            portlabel.setDisable(true);
        } else if (this.handler == null) {
            // if the handler was null before this, turn things on
            start.setDisable(false);
            porttext.setDisable(false);
            portlabel.setDisable(false);
        }

        this.handler = handler;

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

    public static ServerController load(ChangeController changes) throws IOException {
        return FXUtil.load(ServerController.class, "Server.fxml", changes);
    }

}
