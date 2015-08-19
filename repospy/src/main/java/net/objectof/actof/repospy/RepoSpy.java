package net.objectof.actof.repospy;


import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class RepoSpy extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        RepoSpyController spy = new RepoSpyController();
        spy.setDisplayStage(primaryStage);
        spy.initializeDisplay();

        primaryStage.setTitle(spy.getTitle());
        primaryStage.getIcons().add(new Image(RepoSpy.class.getResource("RepoSpy.png").openStream()));
        primaryStage.setOnCloseRequest(event -> {
            if (spy.history.getChanges().isEmpty()) { return; }

            Action reallyquit = Dialogs.create()
                    .title("Exit RepoSpy")
                    .message("Exit RepoSpy with ununcommitted changes?")
                    .masthead("You have uncommittted changes")
                    .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
                    .showConfirm();

            if (reallyquit != Dialog.ACTION_YES) {
                event.consume();
            }

        });

        Scene scene = new Scene((Parent) spy.getDisplayNode());
        primaryStage.setScene(scene);
        primaryStage.show();

        spy.onShowDisplay();

    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
