package net.objectof.actof.minion;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.component.Resource;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.minion.components.classpath.ClasspathController;
import net.objectof.actof.minion.components.handlers.HandlerController;
import net.objectof.actof.minion.components.rest.RestController;
import net.objectof.actof.minion.components.server.ServerController;
import net.objectof.actof.minion.components.spring.SpringController;


public class Minion extends Application implements Display {

    public static final String SETTING_PATH = "net.objectof.actof.minion.path";

    private Parent displayNode;
    private boolean top = true;;

    private MinionController window;

    private ChangeController change = new IChangeController();

    private ServerController server;
    private HandlerController handlers;
    private SpringController spring;
    private RestController rest;
    private ClasspathController classpath;

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        stage();
        minionWindow();

        classpathTab();
        springTab();
        serverTab();
        restTab();
        handlerTab();

        stage.setOnCloseRequest(event -> {
            server.stop();
        });

        stage.show();

    }

    @Override
    public ChangeController getChangeBus() {
        return change;
    }

    private void stage() throws IOException {
        stage.setTitle("Minion Web Server");
        stage.getIcons().add(new Image(Minion.class.getResource("view/Minion.png").openStream()));
    }

    private void minionWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/Minion.fxml"));
        displayNode = loader.load();
        stage.setScene(new Scene(displayNode));

        window = loader.getController();

    }

    private void serverTab() throws IOException {
        server = ServerController.load(getChangeBus());
        window.addTab(server.getNode(), "Server");
    }

    private void springTab() throws IOException {
        spring = SpringController.load(getChangeBus());
        window.addTab(spring.getNode(), "Spring");
    }

    private void restTab() throws IOException {
        rest = RestController.load(getChangeBus());
        window.addTab(rest.getNode(), "REST");
    }

    private void handlerTab() throws IOException {
        handlers = HandlerController.load(getChangeBus());
        window.addTab(handlers.getNode(), "Handlers");
    }

    private void classpathTab() throws IOException {
        classpath = ClasspathController.load(getChangeBus());
        window.addTab(classpath.getNode(), "Classpath");
    }

    public static void main(String[] args) {

        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

    @Override
    public Node getDisplayNode() {
        return displayNode;
    }

    @Override
    public String getTitle() {
        return "Minion";
    }

    @Override
    public Stage getDisplayStage() {
        return stage;
    }

    @Override
    public void setDisplayStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setChangeBus(ChangeController bus) {
        this.change = bus;
    }

    @Override
    public void initialize() throws Exception {}

    @Override
    public void onShow() throws Exception {}

    @Override
    public boolean isTop() {
        return top;
    }

    @Override
    public void setTop(boolean top) {
        this.top = top;
    }

    @Override
    public Resource getResource() {
        return null;
    }

    @Override
    public void setResource(Resource resource) {
        throw new UnsupportedOperationException();
    }

}
