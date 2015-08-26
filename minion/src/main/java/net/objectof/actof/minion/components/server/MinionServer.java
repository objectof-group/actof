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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.objectof.actof.common.component.feature.ChangeBusAware;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.minion.components.server.change.ServerStartChange;
import net.objectof.actof.minion.components.server.change.ServerStopChange;
import net.objectof.actof.minion.components.spring.change.HandlerChange;
import net.objectof.actof.widgets.StatusLight;
import net.objectof.actof.widgets.StatusLight.Status;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.corc.web.v2.impl.IHttpRequest;


public class MinionServer implements ChangeBusAware {

    private ChangeController changebus = new IChangeController();

    // Jetty Server Components
    private Server server;
    private ContextHandler context;
    private SessionHandler sessions;
    private int counter = 0;
    private AbstractHandler handler;

    private int port = 8080;

    private BooleanProperty startable = new SimpleBooleanProperty(false);
    private BooleanProperty stoppable = new SimpleBooleanProperty(false);
    private ObservableList<String> log = FXCollections.observableArrayList();
    private StringProperty statusMessage = new SimpleStringProperty("Server Off");
    private ObjectProperty<StatusLight.Status> statusLight = new SimpleObjectProperty<StatusLight.Status>(Status.OFF);

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
            public void lifeCycleStarting(LifeCycle event) {}

            @Override
            public void lifeCycleStarted(LifeCycle event) {
                stoppable.set(true);
                startable.set(false);
            }

            @Override
            public void lifeCycleFailure(LifeCycle event, Throwable cause) {}

            @Override
            public void lifeCycleStopping(LifeCycle event) {}

            @Override
            public void lifeCycleStopped(LifeCycle event) {
                stoppable.set(false);
                startable.set(true);
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

                log(request.getMethod() + ": " + request.getPathInfo());

                IHttpRequest req = new IHttpRequest("" + counter++, this, request, response);
                handler.execute(req, req);
                base.setHandled(true);
            }
        });
    }

    public void setHandler(AbstractHandler handler) {
        startable.set(true);
        this.handler = handler;
    }

    public void setHandler(HandlerChange change) {
        setHandler(change.getHandler());
    }

    public void stop() {
        try {
            statusLight.set(Status.OFF);
            statusMessage.set("Server Off");

            if (server != null) {
                if (server.isRunning()) {
                    log("Shut Down Jetty Server");
                }
                server.stop();
            }
            getChangeBus().broadcast(new ServerStopChange());
        }
        catch (Exception e) {
            statusLight.set(Status.BAD);
            statusMessage.set(e.getMessage());
        }
    }

    public void start() {
        log.clear();
        try {
            initServer();
            server.start();
            log("Started Jetty Server on Port " + port);
            String path = "http://localhost:" + port + "/";
            statusLight.set(Status.GOOD);
            statusMessage.set("Server On: " + path);
            getChangeBus().broadcast(new ServerStartChange(path));
        }
        catch (Exception e) {
            statusLight.set(Status.BAD);
            statusMessage.set(e.getMessage());
            e.printStackTrace();
        }
    }

    private synchronized void log(String message) {
        getLog().add(message);
    }

    public ReadOnlyBooleanProperty startableProperty() {
        return startable;
    }

    public ReadOnlyBooleanProperty stoppableProperty() {
        return stoppable;
    }

    public boolean isStartable() {
        return startableProperty().get();
    }

    public boolean isStoppable() {
        return stoppableProperty().get();
    }

    public final StringProperty statusMessageProperty() {
        return this.statusMessage;
    }

    public final java.lang.String getStatusMessage() {
        return this.statusMessageProperty().get();
    }

    public final void setStatusMessage(final java.lang.String status) {
        this.statusMessageProperty().set(status);
    }

    public final ObjectProperty<StatusLight.Status> statusLightProperty() {
        return this.statusLight;
    }

    public final net.objectof.actof.widgets.StatusLight.Status getStatusLight() {
        return this.statusLightProperty().get();
    }

    public final void setStatusLight(final net.objectof.actof.widgets.StatusLight.Status statuslight) {
        this.statusLightProperty().set(statuslight);
    }

    @Override
    public ChangeController getChangeBus() {
        return changebus;
    }

    @Override
    public void setChangeBus(ChangeController bus) {
        this.changebus = bus;
    }

    public ObservableList<String> getLog() {
        return log;
    }

}
