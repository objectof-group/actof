package net.objectof.actof.repospy;


import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.objectof.actof.common.component.AbstractDisplay;
import net.objectof.actof.common.component.Resource;
import net.objectof.actof.common.component.ResourceDisplay;
import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.repospy.controllers.history.HistoryController;
import net.objectof.actof.repospy.controllers.navigator.NavigatorController;
import net.objectof.actof.repospy.controllers.review.ReviewController;
import net.objectof.actof.repospy.resource.RepositoryResource;
import net.objectof.connector.Connector;
import net.objectof.model.query.Query;
import net.objectof.model.query.parser.QueryBuilder;


public class RepoSpyController extends AbstractDisplay implements ResourceDisplay {

    public RepositoryController repository;
    public SearchController search;
    public NavigatorController navigator;
    public HistoryController history;

    private RepositoryResource resource;

    @Override
    public void initializeDisplay() throws Exception {
        repository = new RepositoryController(getChangeBus());
        search = new SearchController(repository, getChangeBus());
        history = new HistoryController(getChangeBus());
        navigator = NavigatorController.load(getChangeBus());

        navigator.copySettings(this);
        navigator.setTop(false);
        navigator.initializeDisplay();
        navigator.setTopController(this);

    }

    @Override
    public void onShowDisplay() throws Exception {
        navigator.onShowDisplay();
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

    public Connector showConnect() throws IOException {
        return ConnectionController.showConnectDialog(getDisplayStage());
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
    public Node getDisplayNode() {
        return navigator.getDisplayNode();
    }

    @Override
    public String getTitle() {
        return "RepoSpy";
    }

}
