package net.objectof.actof.schemaspy;


import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.objectof.actof.common.window.ActofWindow;


public class SchemaSpy extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("ObjectOf SchemaSpy");

        SchemaSpyController spy = new SchemaSpyController();
        spy.setDisplayStage(primaryStage);
        spy.construct();

        primaryStage.getIcons().add(new Image(SchemaSpy.class.getResource("view/icons/SchemaSpy.png").openStream()));
        primaryStage.setOnCloseRequest(event -> {
            if (!spy.view.modified) { return; }

            Action reallyquit = Dialogs.create()
                    .title("Exit SchemaSpy")
                    .message("Exit SchemaSpy with unsaved changes?")
                    .masthead("You have unsaved changes")
                    .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
                    .showConfirm();

            if (reallyquit != Dialog.ACTION_YES) {
                event.consume();
            }

        });

        ActofWindow window = new ActofWindow(primaryStage, spy);
        window.setSize(1000, 470);
        window.show();

    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
