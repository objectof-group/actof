package net.objectof.actof.minion;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.objectof.actof.common.controller.TopController;
import net.objectof.actof.common.controller.change.ChangeController;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.minion.components.rest.RestController;
import net.objectof.actof.minion.components.server.ServerController;
import net.objectof.actof.minion.components.spring.SpringController;


public class Minion extends Application implements TopController {

    private MinionController window;

    private IChangeController change = new IChangeController();

    private ServerController server;
    // private HandlerController handlers;
    private SpringController spring;
    private RestController rest;
    // private RepoSelectionController repo;

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage();
        minionWindow();

        // repoTab();
        // handlerTab();
        springTab();
        serverTab();
        restTab();

        stage.setOnCloseRequest(event -> {
            server.stop();
        });

        stage.show();

    }

    @Override
    public ChangeController getChangeBus() {
        return change;
    }

    private void stage() {
        stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Minion Web Server");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(null);
    }

    private void minionWindow() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/Minion.fxml"));
        Parent page = loader.load();
        stage.setScene(new Scene(page));

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

    /*
     * private void handlerTab() throws IOException { handlers =
     * HandlerController.load(getChangeBus()); window.addTab(handlers.getNode(),
     * "Handlers"); }
     */
    private void restTab() throws IOException {
        rest = RestController.load(getChangeBus());
        window.addTab(rest.getNode(), "REST");
    }

    /*
     * private void repoTab() throws IOException { repo =
     * RepoSelectionController.load(getChangeBus());
     * repo.setTopController(this); window.addTab(repo.getNode(), "Repository");
     * }
     */

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

    public ServerController getServer() {
        return server;
    }

}
