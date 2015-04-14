package net.objectof.actof.connectorui.parametereditor;


import java.io.File;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import net.objectof.connector.parameter.Parameter;


public class DirectoryParameterEditor extends HBox implements ParameterEditor {

    private TextField text = new TextField();
    private Button browse = new Button("\u2026");
    private Parameter param;

    public DirectoryParameterEditor(Parameter param, Window owner) {

        this.param = param;

        text.setPromptText(param.getTitle());
        text.setText(param.getValue());

        browse.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();

            File dir = chooser.showDialog(owner);
            if (dir != null) {
                text.setText(dir.getAbsolutePath());
                param.setValue(dir.getAbsolutePath());
            }
        });

        text.setOnKeyReleased(event -> {
            param.setValue(text.getText());
        });

        browse.setStyle("-fx-padding: 5;");
        getChildren().add(text);
        getChildren().add(browse);
        setSpacing(3);
        HBox.setHgrow(text, Priority.ALWAYS);

    }

    @Override
    public Node asNode() {
        return this;
    }

    @Override
    public Parameter getParameter() {
        return param;
    }

    @Override
    public void setCreate(boolean create) {}

}
