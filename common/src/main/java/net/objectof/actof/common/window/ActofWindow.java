package net.objectof.actof.common.window;


import javafx.scene.Scene;
import javafx.stage.Stage;
import net.objectof.actof.common.component.editor.Editor;
import net.objectof.actof.common.component.editor.impl.ResourceView;
import net.objectof.actof.common.component.resource.Resource;
import net.objectof.actof.common.component.resource.impl.TransientResource;


public class ActofWindow {

    private ResourceView editorPane;
    private Resource resource;
    private Stage stage;

    public ActofWindow(Editor editor) throws Exception {
        this(new TransientResource(editor));
    }

    public ActofWindow(Stage stage, Editor editor) throws Exception {
        this(stage, new TransientResource(editor));
    }

    public ActofWindow(Resource resource) throws Exception {
        this(new Stage(), resource);
    }

    public ActofWindow(Stage stage, Resource resource) throws Exception {
        this.resource = resource;
        this.stage = stage;

        editorPane = ResourceView.load();
        editorPane.setDisplayStage(stage);
        editorPane.construct();
        editorPane.setResource(resource);
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

    public ResourceView getEditorPane() {
        return editorPane;
    }

}
