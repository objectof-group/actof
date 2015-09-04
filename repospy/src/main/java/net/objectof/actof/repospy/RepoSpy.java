package net.objectof.actof.repospy;


import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.objectof.actof.common.icons.ActofIcons;
import net.objectof.actof.common.window.ActofWindow;
import net.objectof.actof.repospy.resource.RepositoryResource;


public class RepoSpy extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        RepositoryResource spy = new RepositoryResource();
        RepoSpyController editor = (RepoSpyController) spy.getEditor();
        editor.getToolbars();

        Button connect = new Button("Connect", ActofIcons.getCustomIcon(RepoSpy.class, "icons/connect.png"));
        connect.setTooltip(new Tooltip("Connect to an objectof repository"));
        connect.getStyleClass().add("tool-bar-button");
        connect.setOnAction(event -> editor.onConnect());

        editor.getToolbars().add(0, connect);

        primaryStage.setTitle(spy.getTitle());
        primaryStage.getIcons().add(new Image(RepoSpy.class.getResource("RepoSpy.png").openStream()));
        primaryStage.setOnCloseRequest(event -> {
            if (editor.history.getChanges().isEmpty()) { return; }

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

        editor.setStage(primaryStage);

        ActofWindow window = new ActofWindow(primaryStage, spy);
        window.setSize(1000, 470);
        window.getEditorPane().setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        window.show();

    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
