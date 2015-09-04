package net.objectof.actof.minion;


import java.io.IOException;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
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
import net.objectof.actof.web.app.SpringController;
import net.objectof.actof.web.client.WebClient;
import net.objectof.actof.web.server.WebServerEditor;


public class Minion extends Application implements Display, Editor {

    public static final String SETTING_PATH = "net.objectof.actof.minion.path";

    private Region displayNode;
    private boolean top = true;

    private MinionController window;

    private ChangeController change = new IChangeController();

    private WebServerEditor server;
    private HandlerController handlers;
    private SpringController spring;
    private WebClient rest;
    private ClasspathController classpath;

    private ObjectProperty<Stage> stageProperty = new SimpleObjectProperty<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        setStage(primaryStage);
        stage();
        minionWindow();

        classpathTab();
        springTab();
        serverTab();
        restTab();
        handlerTab();

        getStage().show();

    }

    @Override
    public ChangeController getChangeBus() {
        return change;
    }

    private void stage() throws IOException {
        getStage().setTitle("Minion Web Server");
        getStage().getIcons().add(new Image(Minion.class.getResource("view/Minion.png").openStream()));
    }

    private void minionWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/Minion.fxml"));
        displayNode = loader.load();
        getStage().setScene(new Scene(displayNode));

        window = loader.getController();

    }

    private void serverTab() throws Exception {
        server = WebServerEditor.load();
        server.setChangeBus(getChangeBus());
        window.addTab(server.getFXRegion(), "Server");
    }

    private void springTab() throws IOException {
        spring = SpringController.load(getChangeBus());
        window.addTab(spring.getNode(), "Spring");
    }

    private void restTab() throws IOException {
        rest = WebClient.load();
        window.addTab(rest.getFXRegion(), "REST");
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
    public Region getFXRegion() {
        return displayNode;
    }

    @Override
    public String getTitle() {
        return "Minion";
    }

    @Override
    public ObjectProperty<Stage> stageProperty() {
        return stageProperty;
    }

    @Override
    public void setChangeBus(ChangeController bus) {
        this.change = bus;
    }

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

    @Override
    public BooleanProperty dismissibleProperty() {
        return null;
    }

    @Override
    public BooleanProperty dismissedProperty() {
        return null;
    }

    @Override
    public StringProperty titleProperty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<Resource> resourceProperty() {
        // TODO Auto-generated method stub
        return null;
    }

}
