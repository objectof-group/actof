package net.objectof.actof.widgets.network.nodes;


import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.widgets.network.Coordinate;
import net.objectof.actof.widgets.network.INetworkEdge;
import net.objectof.actof.widgets.network.NetworkEdge;
import net.objectof.actof.widgets.network.NetworkNode;
import net.objectof.actof.widgets.network.NetworkPane;


public class NetworkTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        NetworkPane<NetworkNode> pane = new NetworkPane<NetworkNode>() {

            @Override
            protected Coordinate position(NetworkNode node) {
                return ((NetworkNode) node).getPosition();
            }

            @Override
            protected Set<NetworkEdge<NetworkNode>> edges(NetworkNode node) {
                return node.getEdges();
            }
        };
        pane.setPadding(new Insets(6));

        for (int i = 0; i < 10; i++) {
            NetworkNode child = new HandlerNode();
            child.getPosition().set(rand(0, 600), rand(0, 600));

            while (rand() > 0.25) {
                if (pane.getNodes().size() > 0) {
                    Node refnode = pane.getNodes().get(rand(0, pane.getNodes().size() - 1));
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