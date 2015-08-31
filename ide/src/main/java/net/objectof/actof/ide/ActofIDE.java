package net.objectof.actof.ide;


import javafx.application.Application;
import javafx.stage.Stage;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.window.ActofWindow;


public class ActofIDE extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Editor ide = ActofIDEController.load();
        ide.setDisplayStage(primaryStage);
        ide.construct();

        ActofWindow window = new ActofWindow(primaryStage, ide);
        window.setSize(1000, 470);
        window.show();

    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
