package net.objectof.actof.repospy;


import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.objectof.actof.common.controller.ITopController;
import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.repospy.changes.EditingChange;
import net.objectof.actof.repospy.controllers.history.HistoryController;
import net.objectof.actof.repospy.controllers.navigator.NavigatorController;
import net.objectof.actof.repospy.controllers.review.ReviewController;
import net.objectof.connector.Connector;
import net.objectof.model.query.Query;
import net.objectof.model.query.parser.QueryBuilder;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;


public class RepoSpyController extends ITopController {

    public RepositoryController repository = new RepositoryController(getChangeBus());
    public SearchController search = new SearchController(repository, getChangeBus());
    public NavigatorController navigator;
    public HistoryController history = new HistoryController(getChangeBus());

    public Stage primaryStage;

    public RepoSpyController(Stage stage) {
        primaryStage = stage;
    }

    /*************************************************************
     * 
     * UI Component Methods
     * 
     * @throws IOException
     * 
     *************************************************************/

    public void initUI() throws IOException {
        primaryStage.setTitle("ObjectOf RepoSpy");
        navigator = showNavigator();
        navigator.setTopController(this);
    }

    public void connect(Connector connector) throws Exception {
        // make new repo connection
        repository.connect(connector);
    }

    public NavigatorController showNavigator() throws IOException {

        NavigatorController controller = NavigatorController.load(getChangeBus());

        Scene scene = new Scene((Parent) controller.getNode());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(RepoSpy.class.getResource("RepoSpy.png").openStream()));

        primaryStage.setOnCloseRequest(event -> {
            if (history.get().isEmpty()) { return; }

            Action reallyquit = Dialogs.create().title("Exit RepoSpy")
                    .message("Exit RepoSpy with ununcommitted changes?").masthead("You have uncommittted changes")
                    .actions(Dialog.Actions.YES, Dialog.Actions.NO).showConfirm();

            if (reallyquit != Dialog.Actions.YES) {
                event.consume();
            }

        });

        primaryStage.show();

        return controller;
    }

    public void showReview(ObservableList<EditingChange> changes) throws IOException {

        ReviewController review = new ReviewController(history, getChangeBus());
        ScrollPane scroll = new ScrollPane(review);
        scroll.setStyle("-fx-background-color:transparent;");
        scroll.setFitToWidth(true);

        Stage connectStage = new Stage(StageStyle.UTILITY);
        connectStage.setTitle("Review Changes");
        // connectStage.initModality(Modality.NONE);
        connectStage.initOwner(primaryStage);
        connectStage.setScene(new Scene(scroll));
        connectStage.showAndWait();

    }

    public Connector showConnect() throws IOException {
        return ConnectionController.showConnectDialog(primaryStage);
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
            e.printStackTrace();
            search.setQuery(null);
            return;
        }
    }

}
