package net.objectof.actof.repospy;


import java.io.IOException;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.objectof.actof.common.component.display.Display;
import net.objectof.actof.common.component.editor.ResourceEditor;
import net.objectof.actof.common.component.editor.impl.AbstractEditor;
import net.objectof.actof.common.component.editor.impl.EditorPanel;
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
import net.objectof.actof.web.server.MinionServerResource;
import net.objectof.connector.Connector;
import net.objectof.corc.Handler;
import net.objectof.corc.web.v2.HttpRequest;
import net.objectof.model.corc.IRepoHandler;
import net.objectof.model.query.Query;
import net.objectof.model.query.parser.QueryBuilder;


public class RepoSpyController extends AbstractEditor implements ResourceEditor {

    public RepositoryController repository;
    public SearchController search;
    public NavigatorController navigator;
    public HistoryController history;

    private boolean forResource = false;
    private RepositoryResource resource;

    Action searchAction = new IAction("Search", () -> navigator.toggleSearchBar());
    Action dumpAction = new IAction("Dump to JSON", () -> navigator.onDump());
    Action loadAction = new IAction("Load from JSON", () -> navigator.onLoad());
    Action restAction = new IAction("Run REST Server", this::restServer);

    @Override
    public void construct() throws Exception {

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
        navigator.setDisplayStage(getDisplayStage());
        navigator.setTopController(this);
        navigator.construct();

        dumpAction.setEnabled(false);
        loadAction.setEnabled(false);
        restAction.setEnabled(false);

        getActions().add(searchAction);
        getActions().add(dumpAction);
        getActions().add(loadAction);
        getActions().add(restAction);

    }

    @Override
    protected void onResourceAdded(Resource res) {
        try {
            ResourceEditor e = res.getEditor();
            if (e == null) { return; }

            e.setChangeBus(getChangeBus());
            e.setDisplayStage(getDisplayStage());
            e.construct();
            e.setResource(res);
            e.loadResource();
            EditorPanel panel = new EditorPanel(e);
            getPanels().add(panel);

            panel.dismissedProperty().addListener(e2 -> getPanels().remove(panel));
            e.dismissedProperty().addListener(e2 -> getResources().remove(res));

        }
        catch (Exception e1) {
            e1.printStackTrace();
            return;
        }
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = (RepositoryResource) resource;
    }

    @Override
    public RepositoryResource getResource() {
        return resource;
    }

    @Override
    public void loadResource() throws Exception {
        connect(resource.getConnector());
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
        connectStage.initOwner(getDisplayStage());
        connectStage.setScene(new Scene(scroll));
        connectStage.showAndWait();

    }

    public static Connector showConnect(Stage stage) throws IOException {
        return ConnectionController.showConnectDialog(stage);
    }

    public Optional<Resource> restServer() {
        MinionServerResource res = new MinionServerResource();
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
    public boolean isForResource() {
        return forResource;
    }

    @Override
    public void setForResource(boolean forResource) {
        this.forResource = forResource;
    }

    @Override
    public Display getDisplay() {
        return navigator;
    }

}
