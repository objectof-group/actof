package net.objectof.actof.widgets.network.nodes;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.widgets.network.INetworkEdge;
import net.objectof.actof.widgets.network.INetworkVertex;
import net.objectof.actof.widgets.network.NetworkPane;
import net.objectof.actof.widgets.network.NetworkVertex;


public class NetworkTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        NetworkPane pane = new NetworkPane();
        pane.setPadding(new Insets(6));

        for (int i = 0; i < 10; i++) {
            INetworkVertex child = new HandlerNode();
            child.getPosition().set(rand(0, 600), rand(0, 600));

            while (rand() > 0.25) {
                if (pane.getVertices().size() > 0) {
                    NetworkVertex refnode = pane.getVertices().get(rand(0, pane.getVertices().size() - 1));
                    child.getEdges().add(new INetworkEdge(child, refnode));
                }
            }

            pane.add(child);

        }

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(700);
        primaryStage.setHeight(700);
        primaryStage.show();

    }

    private static double rand() {
        return Math.random();
    }

    private static int rand(int min, int range) {
        return min + (int) (rand() * range);
    }

    public static void main(String[] args) {
        launch(args);
    }

}