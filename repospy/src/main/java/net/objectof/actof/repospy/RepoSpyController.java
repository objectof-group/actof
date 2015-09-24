package net.objectof.actof.repospy;


import java.io.IOException;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.impl.AbstractEditor;
import net.objectof.actof.common.component.editor.impl.ResourcePanel;
import net.objectof.actof.common.component.resource.Action;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.component.resource.impl.IAction;
import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.actof.common.controller.repository.RepositoryReplacedChange;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.repospy.controllers.history.HistoryController;
import net.objectof.actof.repospy.controllers.navigator.NavigatorController;
import net.objectof.actof.repospy.controllers.review.ReviewController;
import net.objectof.actof.repospy.resource.RepositoryResource;
import net.objectof.actof.web.server.WebServerResource;
import net.objectof.actof.widgets.ActofDialogs;
import net.objectof.connector.Connector;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.corc.IRepoHandler;
import net.objectof.model.query.Query;
import net.objectof.model.query.parser.QueryBuilder;


public class RepoSpyController extends AbstractEditor {

    public RepositoryController repository;
    public SearchController search;
    public NavigatorController navigator;
    public HistoryController history;

    Action searchAction = new IAction("Search", () -> navigator.toggleSearchBar());
    Action dumpAction = new IAction("Dump to JSON", () -> navigator.onDump());
    Action loadAction = new IAction("Load from JSON", () -> navigator.onLoad());
    Action restAction = new IAction("Run REST Server", this::restServer);

    public RepoSpyController() throws IOException {

        getChangeBus().listen(RepositoryReplacedChange.class, () -> {
            dumpAction.setEnabled(true);
            loadAction.setEnabled(true);
            restAction.setEnabled(true);
        });

        repository = new RepositoryController(getChangeBus());
        search = new SearchController(repository, getChangeBus());
        history = new HistoryController(getChangeBus());
        navigator = NavigatorController.load();
        navigator.setChangeBus(getChangeBus());
        navigator.setStage(getStage());
        navigator.setTopController(this);

        dumpAction.setEnabled(false);
        loadAction.setEnabled(false);
        restAction.setEnabled(false);

        getActions().add(searchAction);
        getActions().add(dumpAction);
        getActions().add(loadAction);
        getActions().add(restAction);

        resourceProperty().addListener(event -> loadResource());

    }

    @Override
    protected void onResourceAdded(Resource res) {
        try {
            Editor e = res.getEditor();
            if (e == null) { return; }

            e.setChangeBus(getChangeBus());
            e.setStage(getStage());
            e.setResource(res);
            ResourcePanel panel = new ResourcePanel(res);
            getPanels().add(panel);

            panel.dismissedProperty().addListener(e2 -> getPanels().remove(panel));
            e.dismissedProperty().addListener(e2 -> getResources().remove(res));

        }
        catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
    }

    private void loadResource() {
        RepositoryResource res = (RepositoryResource) getResource();
        try {
            connect(res.getConnector());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*************************************************************
     * 
     * UI Component Methods
     * 
     * @throws IOException
     * 
     *************************************************************/

    public void connect(Connector connector) throws Exception {
        // make new repo connection
        repository.connect(connector);
    }

    public void showReview() throws IOException {

        ReviewController review = new ReviewController(history, getChangeBus());
        ScrollPane scroll = new ScrollPane(review);
        scroll.setStyle("-fx-background-color:transparent;");
        scroll.setFitToWidth(true);

        scroll.setPrefSize(600, 400);
        Stage connectStage = new Stage(StageStyle.UTILITY);
        connectStage.setTitle("Review Changes");
        // connectStage.initModality(Modality.NONE);
        connectStage.initOwner(getStage());
        connectStage.setScene(new Scene(scroll));
        connectStage.showAndWait();

    }

    public static Connector showConnect(Stage stage) throws IOException {
        return ConnectionController.showConnectDialog(stage);
    }

    public Optional<Resource> restServer() {
        WebServerResource res = new WebServerResource();
        Handler<HttpRequest> rest = new IRepoHandler(repository.getRepo());
        res.getServer().setHandler(rest);
        return Optional.of(res);
    }

    /*************************************************************
     * 
     * Database Interaction Methods
     * 
     *************************************************************/

    public void doQuery(String queryText) {

        try {
            Query query = QueryBuilder.build(queryText, repository.getStagingTx());
            search.setQuery(query);
        }
        catch (IllegalArgumentException | UnsupportedOperationException e) {
            search.setQuery(null);
            return;
        }
    }

    @Override
    public String getTitle() {
        return "RepoSpy";
    }

    @Override
    public Display getDisplay() {
        return navigator;
    }

    public void onConnect() {

        if (history.hasHistory()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Discard Changes?");
            alert.setHeaderText("Discard uncommitted changes?");
            alert.setContentText("You cannot undo this operation.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) { return; }
        }

        try {
            Connector conn = RepoSpyController.showConnect(getStage());
            if (conn == null) { return; }
            connect(conn);
        }
        catch (Exception e) {
            ActofDialogs.exceptionDialog(e);
        }
    }

}
