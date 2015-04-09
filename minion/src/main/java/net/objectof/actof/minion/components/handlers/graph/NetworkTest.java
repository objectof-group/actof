package net.objectof.actof.minion.components.handlers.graph;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.objectof.actof.widgets.network.INetworkEdge;
import net.objectof.actof.widgets.network.INetworkVertex;
import net.objectof.actof.widgets.network.NetworkEdge;
import net.objectof.actof.widgets.network.NetworkPane;
import net.objectof.actof.widgets.network.NetworkVertex;
import net.objectof.actof.widgets.network.edgestyles.CubicEdgeStyle;


public class NetworkTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        NetworkPane pane = new NetworkPane();
        pane.setEdgeStyle(new CubicEdgeStyle());
        // pane.setPadding(new Insets(6));

        for (int i = 0; i < 10; i++) {
            INetworkVertex child = new HandlerNode(pane, null);
            child.setX(rand(0, 600));
            child.setY(rand(0, 600));

            pane.getVertices().add(child);

        }

        for (NetworkVertex vertex : pane.getVertices()) {
            while (rand() > 0.35) {
                int index = rand(0, pane.getVertices().size() - 1);
                NetworkVertex refnode = pane.getVertices().get(index);
                NetworkEdge edge = new INetworkEdge(vertex, refnode);
                edge.setColor(Color.DARKGRAY);
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