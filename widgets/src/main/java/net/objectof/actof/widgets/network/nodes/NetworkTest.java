package net.objectof.actof.widgets.network.nodes;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.objectof.actof.widgets.network.INetworkEdge;
import net.objectof.actof.widgets.network.INetworkVertex;
import net.objectof.actof.widgets.network.NetworkEdge;
import net.objectof.actof.widgets.network.NetworkPane;
import net.objectof.actof.widgets.network.NetworkVertex;


public class NetworkTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        NetworkPane pane = new NetworkPane();
        pane.setPadding(new Insets(6));

        for (int i = 0; i < 10; i++) {
            INetworkVertex child = new INetworkVertex();
            child.setX(rand(0, 600));
            child.setY(rand(0, 600));
            child.setPrefSize(100, 100);

            child.setBackground(new Background(new BackgroundFill(new Color(rand(), rand(), rand(), 1),
                    new CornerRadii(5), new Insets(0))));
            child.setStyle("-fx-effect: dropshadow(gaussian, #000000, 5, -2, 0, 1);");
            pane.getVertices().add(child);

        }

        for (NetworkVertex vertex : pane.getVertices()) {
            while (rand() > 0.35) {
                int index = rand(0, pane.getVertices().size() - 1);
                NetworkVertex refnode = pane.getVertices().get(index);
                NetworkEdge edge = new INetworkEdge(vertex, refnode);
                edge.setColor(Color.DARKGRAY);
                edge.setDestOffset(rand(0, 100), rand(0, 100));
                edge.setSourceOffset(rand(0, 100), rand(0, 100));
                vertex.getEdges().add(edge);
            }
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