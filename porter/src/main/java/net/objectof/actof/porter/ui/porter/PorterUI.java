package net.objectof.actof.porter.ui.porter;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class PorterUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PorterUIController porterController = PorterUIController.load();

        Scene scene = new Scene((Parent) porterController.getFXNode());
        primaryStage.getIcons().add(new Image(PorterUI.class.getResource("Porter.png").openStream()));
        primaryStage.setScene(scene);
        primaryStage.setTitle("ObjectOf Porter");
        primaryStage.show();

        porterController.onShowDisplay();
    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }

}
