package net.objectof.actof.ide;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.common.component.Display;
import net.objectof.actof.common.controller.change.IChangeController;
import net.objectof.actof.common.window.ActofWindow;


public class ActofIDE extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ActofWindow window = ActofWindow.load();
        window.setDisplayStage(primaryStage);
        window.initializeDisplay();

        Display ide = ActofIDEController.load(new IChangeController());
        ide.setDisplayStage(primaryStage);
        ide.initializeDisplay();

        window.getSubDisplays().add(ide);

        primaryStage.setScene(new Scene((Parent) window.getDisplayNode()));
        primaryStage.show();
        window.onShowDisplay();

    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
