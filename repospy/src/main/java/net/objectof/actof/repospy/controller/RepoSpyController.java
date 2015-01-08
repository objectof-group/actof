package net.objectof.actof.repospy.controller;


import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.objectof.actof.common.controller.ITopController;
import net.objectof.actof.common.controller.repository.RepositoryController;
import net.objectof.actof.common.controller.search.SearchController;
import net.objectof.actof.common.util.FXUtil;
import net.objectof.actof.connectorui.ConnectionController;
import net.objectof.actof.repospy.RepoSpy;
import net.objectof.actof.repospy.changes.EditingChange;
import net.objectof.actof.repospy.controller.history.HistoryController;
import net.objectof.actof.repospy.controller.navigator.NavigatorController;
import net.objectof.actof.repospy.controller.review.ReviewController;
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

        NavigatorController controller = FXUtil.load(NavigatorController.class, "../../view/Navigator.fxml",
                getChangeBus());

        Scene scene = new Scene((Parent) controller.getNode());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(RepoSpy.class.getResource("view/icons/repospy.png").openStream()));


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

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(RepoSpy.class.getResource("view/TransactionReview.fxml"));
        Parent page = loader.load();

        Stage connectStage = new Stage(StageStyle.UTILITY);
        connectStage.setTitle("Review Changes");
        // connectStage.initModality(Modality.WINDOW_MODAL);
        connectStage.initOwner(primaryStage);
        connectStage.setScene(new Scene(page));
        ReviewController controller = loader.getController();
        controller.setChanges(changes);

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
            search.setQuery(null);
            return;
        }
    }




}
