package net.objectof.actof.minion;


import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.display.Panel;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.minion.components.classpath.ClasspathController;
import net.objectof.actof.minion.components.handlers.HandlerController;
import net.objectof.actof.minion.components.rest.RestController;
import net.objectof.actof.minion.components.server.MinionServerEditor;
import net.objectof.actof.minion.components.spring.SpringController;


public class Minion extends Application implements Display, Editor {

    public static final String SETTING_PATH = "net.objectof.actof.minion.path";

    private Parent displayNode;
    private boolean top = true;;

    private MinionController window;

    private ChangeController change = new IChangeController();

    private MinionServerEditor server;
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

    private void serverTab() throws Exception {
        server = MinionServerEditor.load();
        server.setChangeBus(getChangeBus());
        server.construct();
        window.addTab(server.getFXNode(), "Server");
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
    public Node getFXNode() {
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
    public void construct() throws Exception {}

    @Override
    public ObservableList<Node> getToolbars() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<Panel> getPanels() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public Display getDisplay() {
        return this;
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
    public void setTitle(String title) {}

}
