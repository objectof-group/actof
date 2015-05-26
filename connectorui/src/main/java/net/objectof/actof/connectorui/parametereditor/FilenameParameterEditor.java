package net.objectof.actof.connectorui.parametereditor;


import java.io.File;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import net.objectof.connector.Parameter;


public class FilenameParameterEditor extends HBox implements ParameterEditor {

    private TextField text = new TextField();
    private Button browse = new Button("\u2026");
    private boolean create = false;
    private Parameter param;

    public FilenameParameterEditor(Parameter param, Window owner) {

        this.param = param;

        text.setPromptText(param.getTitle());
        text.setText(param.getValue());

        browse.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            String oldFilename = param.getValue();
            if (oldFilename.length() > 0) {
                File oldFile = new File(oldFilename);
                chooser.setInitialDirectory(oldFile.getParentFile());
            }

            File file;
            if (create) {
                file = chooser.showSaveDialog(owner);
            } else {
                file = chooser.showOpenDialog(owner);
            }

            if (file != null) {
                text.setText(file.getAbsolutePath());
                param.setValue(file.getAbsolutePath());
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
    public void setCreate(boolean create) {
        this.create = create;
    }

}
