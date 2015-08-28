package net.objectof.actof.ide;


import javafx.application.Application;
import javafx.stage.Stage;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.window.ActofWindow;


public class ActofIDE extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ActofWindow window = ActofWindow.load();
        window.setDisplayStage(primaryStage);
        window.construct();
        window.getFXRegion().setPrefHeight(470);
        window.getFXRegion().setPrefWidth(1000);

        Editor ide = ActofIDEController.load();
        ide.setDisplayStage(primaryStage);
        ide.construct();

        window.setEditor(ide);
        window.show();

    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
