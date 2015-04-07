package net.objectof.actof.minion.testgraph;


import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


// Demonstrates the JavaFX node mouseTransparent and pickOnBounds properties.
public class LayerClick extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ToggleButton testButton = new ToggleButton("");

        VBox layer1 = new VBox();
        layer1.getChildren().add(testButton);

        AnchorPane layer2 = new AnchorPane();
        layer2.getChildren().add(new ListView<String>());
        layer2.setShape(new Circle(100, 100, 100, Color.FIREBRICK));
        layer2.setOpacity(0.7);

        StackPane stack = new StackPane();
        stack.getChildren().setAll(layer1, layer2);
        stack.setStyle("-fx-background-color: azure;");

        VBox layout = new VBox();
        layout.getChildren().setAll(stack, createControls(testButton, layer2));

        stage.setScene(new Scene(layout));
        stage.show();
    }

    private VBox createControls(ToggleButton controlledButton, Node controlledNode) {
        controlledButton.textProperty().bind(
                Bindings.when(controlledNode.mouseTransparentProperty())
                        .then("Completely Clickable")
                        .otherwise(
                                Bindings.when(controlledNode.pickOnBoundsProperty()).then("Not Clickable")
                                        .otherwise("Partially clickable")));

        CheckBox enableMouseTransparency = new CheckBox("Enable MouseTransparency");
        enableMouseTransparency.setSelected(controlledNode.isMouseTransparent());
        controlledNode.mouseTransparentProperty().bind(enableMouseTransparency.selectedProperty());

        CheckBox enablePickOnBounds = new CheckBox("Enable Pick On Bounds");
        enablePickOnBounds.setSelected(controlledNode.isPickOnBounds());
        controlledNode.pickOnBoundsProperty().bind(enablePickOnBounds.selectedProperty());

        VBox controls = new VBox(10);
        controls.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        controls.getChildren().addAll(enableMouseTransparency, enablePickOnBounds);

        return controls;
    }
}