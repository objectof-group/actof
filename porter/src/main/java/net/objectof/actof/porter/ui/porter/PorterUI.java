package net.objectof.actof.porter.ui.porter;


import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.common.controller.change.IChangeController;


public class PorterUI extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        PorterUIController porterController = PorterUIController.load(new IChangeController());

        Scene scene = new Scene((Parent) porterController.getNode());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
