package net.objectof.actof.ide;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.controller.change.IChangeController;


public class ActofIDE extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Display ide = ActofIDEController.load(new IChangeController());

        ide.setDisplayStage(primaryStage);
        ide.initializeDisplay();

        primaryStage.setScene(new Scene((Parent) ide.getDisplayNode()));
        primaryStage.show();
        ide.onShowDisplay();

    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
