package net.objectof.actof.common.window;


import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.impl.EditorPane;


public class ActofWindow {

    private EditorPane editorPane;
    private Editor editor;
    private Stage stage;

    public ActofWindow(Editor editor) throws Exception {
        this(new Stage(), editor);
    }

    public ActofWindow(Stage stage, Editor editor) throws Exception {
        this.editor = editor;
        this.stage = stage;

        editorPane = EditorPane.load();
        editorPane.setDisplayStage(stage);
        editorPane.construct();
        editorPane.setEditor(editor);
    }

    public void show() {
        Scene scene = new Scene(editorPane.getFXRegion());
        stage.setScene(scene);
        stage.show();
    }

    public void setSize(int width, int height) {
        editorPane.getFXRegion().setPrefHeight(height);
        editorPane.getFXRegion().setPrefWidth(width);
    }

}
